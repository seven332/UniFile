/*
 * Copyright 2016 Hippo Seven
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hippo.unifile;

import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class UriRandomAccessFile extends RandomAccessFile {

    private static final String TAG = UriRandomAccessFile.class.getSimpleName();

    private static final Field FIELD_FD;
    private static final Method METHOD_CLOSE;

    static {
        Field field;
        try {
            field = RandomAccessFile.class.getDeclaredField("fd");
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            Log.e(TAG, "Can't get field RandomAccessFile.fd : " + e);
            field = null;
        }
        FIELD_FD = field;

        Method method;
        try {
            Class<?> clazz = Class.forName("libcore.io.IoUtils");
            method = clazz.getMethod("close", FileDescriptor.class);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "Can't get class libcore.io.IoUtils: " + e);
            method = null;
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "Can't get method libcore.io.IoUtils.close(FileDescriptor): " + e);
            method = null;
        }
        METHOD_CLOSE = method;
    }

    private ParcelFileDescriptor mPfd;

    private UriRandomAccessFile(File file, String mode, ParcelFileDescriptor pfd) throws FileNotFoundException {
        super(file, mode);
        mPfd = pfd;
    }

    @Override
    public void close() throws IOException {
        if (mPfd != null) {
            mPfd.close();
            mPfd = null;
        }
        super.close();
    }

    @NonNull
    static RandomAccessFile create(Context context, Uri uri, String mode) throws IOException {
        // Check reflection stuff
        if (FIELD_FD == null || METHOD_CLOSE == null) {
            throw new IOException("Can't get reflection stuff");
        }

        // Get FileDescriptor
        ParcelFileDescriptor pfd;
        try {
            pfd = context.getContentResolver().openFileDescriptor(uri, mode);
        } catch (SecurityException e) {
            throw new IOException("Can't get ParcelFileDescriptor", e);
        }
        if (pfd == null) {
            throw new IOException("Can't get ParcelFileDescriptor");
        }
        FileDescriptor fd;
        try {
            fd = pfd.getFileDescriptor();
        } catch (SecurityException e) {
            pfd.close();
            throw new IOException("Can't get FileDescriptor", e);
        }
        if (fd == null) {
            pfd.close();
            throw new IOException("Can't get FileDescriptor");
        }

        // Get temp file
        File temp;
        try {
            temp = File.createTempFile("UriRandomAccessFile", ".placeholder");
        } catch (IOException | SecurityException e) {
            throw new IOException("Can't create temp file", e);
        }

        // Create RandomAccessFile
        RandomAccessFile randomAccessFile;
        try {
            randomAccessFile = new UriRandomAccessFile(temp, mode, pfd);
        } catch (FileNotFoundException e) {
            pfd.close();
            throw new IOException("Can't create UriRandomAccessFile", e);
        }

        // Close old FileDescriptor
        try {
            Object obj = FIELD_FD.get(randomAccessFile);
            if (obj instanceof FileDescriptor) {
                METHOD_CLOSE.invoke(null, (FileDescriptor) obj);
            }
        } catch (IllegalAccessException e) {
            pfd.close();
            randomAccessFile.close();
            throw new IOException("Failed to invoke libcore.io.IoUtils.close(FileDescriptor)", e);
        } catch (InvocationTargetException e) {
            pfd.close();
            randomAccessFile.close();
            throw new IOException("Failed to invoke libcore.io.IoUtils.close(FileDescriptor)", e);
        }
        // Delete temp file
        temp.delete();

        // Set new FileDescriptor
        try {
            FIELD_FD.set(randomAccessFile, fd);
        } catch (IllegalAccessException e) {
            pfd.close();
            randomAccessFile.close();
            throw new IOException(e.getMessage());
        }

        return randomAccessFile;
    }
}
