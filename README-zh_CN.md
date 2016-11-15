# BeTranslate

[English](README.md) | 中文

从 Excel 生成 Android 中 'string.xml' 的多语言辅助工具.

## Screenshots

<img src="/screenshots/betranslate_screen_record_0.9.0.gif" alt="screenshots/betranslate_screen_record_0.9.0.gif" width="705" height="478" />

## 安装

* 在 IntelliJ 插件商店搜索 BeTranslate 并安装
* 下载 [BeTranslate.zip](BeTranslate.zip), 在 IntelliJ 中选择从磁盘安装

## 使用

### 第一步

#### 快捷键

option + T (OS X), alt + T (WIN)

确保需要格式化的 Excel 文件如下图所示格式(一列 key, 一列或多列 value)

<img src="/screenshots/betranslate_record_excel.png" alt="screenshots/betranslate_record_excel.png" width="788" height="472" />

### 第二步

配置一些必须的信息:

|Field|description
|:---:|:---:|
| Choose Excel file | 需要格式化的 Excel 文件
| Translate to | 目标语言
| Sheet index | 工作表 index(the first sheet index = 0)
| Start row | 格式化 Excel 的起始行 index(默认第一行开始, index = 0)
| End row | 格式化 Excel 的终止行 index(默认当前选择工作表中的最后一行)
| Key col | 'key' 列的 index(index start with 0)
| Value col | 'value' 列的 index(index start with 0)
| Output directory | 输出文件文件夹路径
| Need fill with the refer xml? | 勾选此项, 将会根据选择的 'string.xml' 文件填充 Excel 没有的数据
| Ignore [translatable="false"] | 勾选此项以及上一项将忽略被标记为 [translatable="false"] 的条目

## Change logs

###1.0.5(2016-11-15)
- Support edit the target language
     
###1.0.4(2016-11-11)
- Support ignore [translatable="false"] attribute
- Fix bugs

###1.0.3(2016-9-14)
- Support resize window
- Fix bugs

###1.0.2(2016-9-13)
- Support fill 'string.xml'
- Support export 'string.xml' to Excel
- Fix bugs

###0.9.0(2016-9-10)
- Alpha version(0.9.0)

## About
If you have any questions, contact me: [lujun.byte#gmail.com](mailto:lujun.byte@gmail.com).

## License

[Apache](LICENSE)
