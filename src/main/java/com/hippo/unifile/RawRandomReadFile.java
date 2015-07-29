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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

class RawRandomReadFile extends UniRandomReadFile {

    private final RandomAccessFile mFile;

    public RawRandomReadFile(File file) throws FileNotFoundException {
        mFile = new RandomAccessFile(file, "r");
    }

    @Override
    public int read(byte[] buffer, int byteOffset, int byteCount) throws IOException {
        return mFile.read(buffer, byteOffset, byteCount);
    }

    @Override
    public void seek(long offset) throws IOException {
        mFile.seek(offset);
    }

    @Override
    public long position() throws IOException {
        return mFile.getFilePointer();
    }

    @Override
    public long length() throws IOException {
        return mFile.length();
    }

    @Override
    public void close() throws IOException {
        mFile.close();
    }
}
