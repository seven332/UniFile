/*
 * Copyright 2015 Hippo Seven
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
import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

class MediaFile extends UniFile {

    private final Context mContext;
    private final Uri mUri;

    MediaFile(Context context, Uri uri) {
        super(null);

        mContext = context.getApplicationContext();
        mUri = uri;
    }

    @Override
    public UniFile createFile(String displayName) {
        return null;
    }

    @Override
    public UniFile createDirectory(String displayName) {
        return null;
    }

    @Override
    @NonNull
    public Uri getUri() {
        return mUri;
    }

    public static boolean isMediaUri(Context context, Uri uri) {
        return null != MediaContract.getName(context, uri);
    }

    @Override
    public String getName() {
        return MediaContract.getName(mContext, mUri);
    }

    @Override
    public String getType() {
        return MediaContract.getType(mContext, mUri);
    }

    @Override
    public boolean isDirectory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isFile() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long lastModified() {
        return MediaContract.lastModified(mContext, mUri);
    }

    @Override
    public long length() {
        return MediaContract.length(mContext, mUri);
    }

    @Override
    public boolean canRead() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean canWrite() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean ensureDir() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean ensureFile() {
        throw new UnsupportedOperationException();
    }

    @Override
    public UniFile subFile(String displayName) {
        return null;
    }

    @Override
    public boolean delete() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean exists() {
        throw new UnsupportedOperationException();
    }

    @Override
    public UniFile[] listFiles() {
        return null;
    }

    @Override
    public UniFile[] listFiles(FilenameFilter filter) {
        return null;
    }

    @Override
    public UniFile findFile(String displayName) {
        return null;
    }

    @Override
    public boolean renameTo(String displayName) {
        throw new UnsupportedOperationException();
    }

    @NonNull
    @Override
    public OutputStream openOutputStream() throws IOException {
        OutputStream os = mContext.getContentResolver().openOutputStream(mUri);
        if (os == null) {
            throw new IOException("Can't open OutputStream");
        }
        return os;
    }

    @NonNull
    @Override
    public OutputStream openOutputStream(boolean append) throws IOException {
        OutputStream os = mContext.getContentResolver().openOutputStream(mUri, append ? "wa" : "w");
        if (os == null) {
            throw new IOException("Can't open OutputStream");
        }
        return os;
    }

    @NonNull
    @Override
    public InputStream openInputStream() throws IOException {
        InputStream is = mContext.getContentResolver().openInputStream(mUri);
        if (is == null) {
            throw new IOException("Can't open InputStream");
        }
        return is;
    }

    @NonNull
    @Override
    public RandomAccessFile createRandomAccessFile(String mode) throws IOException {
        throw new IOException("Can't create random read file");
    }
}
