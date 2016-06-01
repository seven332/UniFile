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

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class DocumentRandomAccessFile extends RandomAccessFile {

    private static final Field FIELD_FD;
    private static final Method METHOD_CLOSE;

    private ParcelFileDescriptor mPfd;

    static {
        Field field;
        try {
            field = RandomAccessFile.class.getDeclaredField("fd");
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            field = null;
        }
        FIELD_FD = field;

        Method method;
        try {
            Class<?> clazz = Class.forName("libcore.io.IoUtils");
            method = clazz.getMethod("close", FileDescriptor.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            method = null;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            method = null;
        }
        METHOD_CLOSE = method;
    }

    private DocumentRandomAccessFile(File file, String mode, ParcelFileDescriptor pfd) throws FileNotFoundException {
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

    public static RandomAccessFile create(Context context, Uri uri, String mode) throws IOException {
        // Check field
        if (FIELD_FD == null || METHOD_CLOSE == null) {
            throw new IOException("Can't get reflection stuff");
        }

        // Get FileDescriptor
        ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, mode);
        if (pfd == null) {
            throw new IOException("Can't get ParcelFileDescriptor");
        }
        FileDescriptor fd = pfd.getFileDescriptor();
        if (fd == null) {
            throw new IOException("Can't get FileDescriptor");
        }

        // Get temp file
        File dir = context.getCacheDir();
        if (dir == null) {
            throw new IOException("Can't get cache dir");
        }
        File temp = null;
        for (int i = 0; i < 100; i++) {
            temp = new File(dir, Integer.toString(i));
            if (temp.isFile() || !temp.exists()) {
                break;
            }
            temp = null;
        }
        if (temp == null) {
            throw new IOException("Can't create temp file");
        }

        // Create RandomAccessFile
        RandomAccessFile randomAccessFile = new DocumentRandomAccessFile(temp, mode, pfd);

        // Close old FileDescriptor
        try {
            Object obj = FIELD_FD.get(randomAccessFile);
            if (obj instanceof FileDescriptor) {
                METHOD_CLOSE.invoke(null, (FileDescriptor) obj);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            randomAccessFile.close();
            throw new IOException(e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            randomAccessFile.close();
            throw new IOException(e.getMessage());
        }

        // Set new FileDescriptor
        try {
            FIELD_FD.set(randomAccessFile, fd);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            randomAccessFile.close();
            throw new IOException(e.getMessage());
        }

        return randomAccessFile;
    }
}
