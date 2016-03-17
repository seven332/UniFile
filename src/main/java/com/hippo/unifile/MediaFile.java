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

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.hippo.yorozuya.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MediaFile extends UniFile {

    private Context mContext;
    private Uri mUri;

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
    public Uri getUri() {
        return mUri;
    }

    public static String getPath(Context context, Uri uri) {
        String path = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Will return "image:x*"
            String wholeID = null;
            try {
                wholeID = DocumentsContract.getDocumentId(uri);
            } catch (Exception e) {
                // Ignore;
            }
            if (wholeID != null) {
                String id;
                int index = wholeID.indexOf(':');
                if (index < 0) {
                    id = wholeID;
                } else {
                    id = wholeID.substring(index + 1);
                }
                String[] column = {MediaStore.Images.Media.DATA};
                // where id is equal to
                String sel = MediaStore.Images.Media._ID + "=?";
                Cursor cursor = null;
                try {
                    cursor = context.getContentResolver().
                            query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    column, sel, new String[]{id}, null);
                    if (cursor != null) {
                        int columnIndex = cursor.getColumnIndex(column[0]);
                        if (cursor.moveToFirst()) {
                            path = cursor.getString(columnIndex);
                        }
                    }
                } catch (Exception e) {
                    // Ignore
                } finally {
                    IOUtils.closeQuietly(cursor);
                }
            }
        }

        if (path == null) {
            String[] projection = {MediaStore.MediaColumns.DATA};
            ContentResolver cr = context.getContentResolver();
            Cursor cursor = null;
            try {
                cursor = cr.query(uri, projection, null, null, null);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        path = cursor.getString(0);
                    }
                }
            } catch (Exception e) {
                // Ignore
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        return path;
    }

    @Override
    public String getName() {
        String path = getPath(mContext, mUri);
        if (path == null) {
            return null;
        }
        int index = path.lastIndexOf('/');
        if (index < 0) {
            return path;
        } else {
            return path.substring(index + 1);
        }
    }

    @Override
    public String getType() {
        return mContext.getContentResolver().getType(mUri);
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public boolean isFile() {
        return false;
    }

    @Override
    public long lastModified() {
        return 0;
    }

    @Override
    public long length() {
        return 0;
    }

    @Override
    public boolean canRead() {
        return false;
    }

    @Override
    public boolean canWrite() {
        return false;
    }

    @Override
    public boolean ensureDir() {
        return false;
    }

    @Override
    public boolean ensureFile() {
        return false;
    }

    @Override
    public UniFile subFile(String displayName) {
        return null;
    }

    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public boolean exists() {
        return true;
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
        return false;
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
    public UniRandomReadFile createRandomReadFile() throws IOException {
        throw new IOException("Can't create random read file");
    }
}
