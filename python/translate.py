import xlrd
data = xlrd.open_workbook('kor.xlsx')
print data.sheet_names()

table = data.sheets()[0]

print table.nrows
print table.ncols

from xml.dom.minidom import Document
doc = Document()
resource = doc.createElement('resource')
resource.setAttribute('xmlns:tools', "http://schemas.android.com/tools")
doc.appendChild(resource)

for rownum in range(table.nrows):
 string = doc.createElement('string')
 string.setAttribute('name', table.cell_value(rownum, 0))
 string.appendChild(doc.createTextNode(table.cell_value(rownum, 2)))
 resource.appendChild(string)

print doc.toprettyxml()