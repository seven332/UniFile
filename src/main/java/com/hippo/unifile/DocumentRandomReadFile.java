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
import android.os.ParcelFileDescriptor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class DocumentRandomReadFile extends UniRandomReadFile {

    private FileChannel mChannel;

    public DocumentRandomReadFile(Context context, Uri treeUri) throws FileNotFoundException {
        ParcelFileDescriptor descriptor = context.getContentResolver().openFileDescriptor(treeUri, "r");
        FileInputStream fis = new FileInputStream(descriptor.getFileDescriptor());
        mChannel = fis.getChannel();
    }

    @Override
    public int read(byte[] buffer, int byteOffset, int byteCount) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer, byteOffset, byteCount);
        return mChannel.read(byteBuffer);
    }

    @Override
    public void seek(long offset) throws IOException {
        mChannel.position(offset);
    }

    @Override
    public long position() throws IOException {
        return mChannel.position();
    }

    @Override
    public long length() throws IOException {
        return mChannel.size();
    }

    @Override
    public void close() throws IOException {
        mChannel.close();
    }
}
