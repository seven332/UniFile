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

/*
 * Created by Hippo on 8/15/2016.
 */

import android.content.Context;
import android.net.Uri;

import com.hippo.unifile.example.TestCreator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class UriTestCreator implements TestCreator {

    private Context mContext;
    private Uri mUri;

    public UriTestCreator(Context context, Uri uri) {
        mContext = context;
        mUri = uri;
    }

    @Override
    public RandomAccessFile createRandomAccessFile(String mode) throws IOException {
        return UriRandomAccessFile.create(mContext, mUri, mode);
    }

    @Override
    public InputStream openInputStream() throws IOException {
        InputStream is = mContext.getContentResolver().openInputStream(mUri);
        if (is == null) {
            throw new IOException("Can't open InputStream");
        }
        return is;
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return UriOutputStream.create(mContext, mUri, "w");
    }
}
