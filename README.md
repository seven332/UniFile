# UniFile
这是 support-v4 包中 DocumentFile 的修改版。

+ 删除了 DocumentFile.fromSingleUri
+ 删除了 DocumentFile.createFile 中 mimeType 参数
+ 添加 DocumentFile.contains
+ 添加 DocumentFile.openOutputStream，DocumentFile.openInputStream
+ 修改 createFile，createDirectory 特性，避免出现文件名后添加 (1) 的现象
