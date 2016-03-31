# UniFile
这是 support-v4 包中 DocumentFile 的修改版。

+ 删除了 createFile 中 mimeType 参数
+ 添加 openOutputStream，openInputStream
+ 修改 createFile，createDirectory 特性，避免出现文件名后添加 (1) 的现象
+ 提升 findFile 效率
