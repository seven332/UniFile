# UniFile

UniFile 是基于 `android.support.v4.provider.DocumentFile`，而且更好用。

The UniFile is forked from `android.support.v4.provider.DocumentFile`, but more powerful.

区别有：
* 添加其他文件 uri 支持
    * 支持所有 ContentProvider 的 uri
    * 支持 asset 文件的 uri，如 `file:///android_asset/text/uccu.txt`
    * 支持 resource 文件的 uri，如 `android.resource://com.hippo.unifile.example/2130903040`
* 添加 getFilePath()
* 添加 listFiles(FilenameFilter)
* 添加 openOutputStream()，openOutputStream(boolean append)，openInputStream()
* 添加 createRandomAccessFile(String mode)
* 删除了 createFile 中 mimeType 参数
* 修改 createFile，createDirectory 特性，避免出现文件名后添加 (1) 的现象

The differences:
* Add other file uri support
    * Support all uri from ContentProvider
    * Support all asset file uri, like `file:///android_asset/text/uccu.txt`
    * Support all resource file uri, like `android.resource://com.hippo.unifile.example/2130903040`
* Add getFilePath()
* Add listFiles(FilenameFilter)
* Add openOutputStream()，openOutputStream(boolean append)，openInputStream()
* Add createRandomAccessFile(String mode)
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
        compile 'com.github.seven332:unifile:1.0.0'
    }

在代码中使用：

Use UniFile in your code:

```java
// 从 Uri 创建 UniFile
// Create UniFile from Uri
file = UniFile.fromUri(context, uri);

// 从 File 创建 UniFile
// Create UniFile from File
file = UniFile.fromFile(f);

// 从 asset path 创建 UniFile
// Create UniFile from asset path
file = UniFile.fromAsset(assetManager, path);

// 从 resource id 创建 UniFile
// Create UniFile from resource id
file = UniFile.fromResource(context, resId);

// 获取原始文件路径
// Get origin file path
path = file.getFilePath();

// 创建随机访问文件
// Create random access file
raf = file.createRandomAccessFile("rw");
```

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
