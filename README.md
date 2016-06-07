# UniFile

UniFile 是基于 `android.support.v4.provider.DocumentFile`，而且更好用。

区别有：
* 添加 listFiles(FilenameFilter)
* 添加 openOutputStream()，openOutputStream(boolean)，openInputStream()
* 添加 createRandomAccessFile(String)
* 添加 subFile(String)
* 添加其他文件 uri 支持
* 删除了 createFile 中 mimeType 参数
* 修改 createFile，createDirectory 特性，避免出现文件名后添加 (1) 的现象

The UniFile is forked from `android.support.v4.provider.DocumentFile`, but more powerful.

The differences:
* Add listFiles(FilenameFilter)
* Add openOutputStream()，openOutputStream(boolean)，openInputStream()
* Add createRandomAccessFile(String)
* Add subFile(String)
* Add other file uri support
* Remove mimeType in createFile function
* Avoid filename ending with (1) in createFile，createDirectory


# Usage

在最外面的 `build.gradle` 里加上 jitpack，别加到 buildscript 里了。

Add jitpack repository in top `build.gradle`, DO **NOT** ADD IT TO buildscript.

    allprojects {
        repositories {
            ...
            maven { url "https://jitpack.io" }
        }
    }

在项目 `build.gradle` 里添加 UniFile 依赖。

Add UniFile as dependency in project `build.gradle`.

    dependencies {
        ...
        compile 'com.github.seven332:unifile:0.1.4'
    }


# License

    Copyright (C) 2015-2016 Hippo Seven

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
