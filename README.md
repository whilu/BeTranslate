# BeTranslate

Generate Android string resource from Excel.

## Screenshots

<img src="/screenshots/betranslate_screen_record_0.9.0.gif" alt="screenshots/betranslate_screen_record_0.9.0.gif" width="705" height="478" />

## Install

* Search 'BeTranslate' on Browse repositores, install...
* Download [BeTranslate.zip](BeTranslate.zip), install this plugin from disk...

## Usage

### Step 1

#### Shortcut

option + T (OS X), alt + T (WIN)

Make sure your translation excel format like the below(A key column, and some value columns)

<img src="/screenshots/betranslate_record_excel.png" alt="screenshots/betranslate_record_excel.png" width="788" height="472" />

### Step 2

Configure your translate information:

|Field|description
|:---:|:---:|
| Choose Excel file | The Excel file path you want to generate
| Translate to | The language to be generate
| Sheet index | The sheet index(the first sheet index = 0)
| Start row | The row for start generate(default is the first row, index = 0)
| End row | The last row number for generate(default the last row in the sheet)
| Key col | The column index for the 'key' column(index start with 0)
| Value col | The column index for the 'value' column(index start with 0)
| Output directory | The output directory for generated file
| Need fill with the refer xml? | If you check this and set a refer 'xml' file path, this will help to fill the output resource file

## Change logs
     
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
