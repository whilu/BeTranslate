# BeTranslate

Generate Android string resource from Excel.

## Screenshots

<img src="/screenshots/betranslate_screen_record_0.9.0.gif" alt="screenshots/betranslate_screen_record_0.9.0.gif" width="705" height="478" />

## Install

Download 'BeTranslate.zip', install this plugin from disk...

## Usage

### Step 1

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

## Change logs
###0.9.0(2016-9-10)
- Alpha version(0.9.0)

## About
If you have any questions, contact me: [lujun.byte#gmail.com](mailto:lujun.byte@gmail.com).

## License

[Apache](LICENSE)
