package co.lujun.betranslate;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Author: lujun(http://blog.lujun.co)
 * Date: 2016-8-30 10:01
 */
public class BeTranslateUtil {

    public static boolean doTranslate(String inputFilePath, String outputPath, String translateLang, int sheetIndex,
                                   int startRow, int endRow, int keyColumn, int valueColumn) {
        boolean translateResult = false;
        try {
            Workbook workbook;
            if (!inputFilePath.matches("^.+\\\\.(?i)(xlsx)$")){
                workbook = new XSSFWorkbook(inputFilePath);
            }else {
                workbook = new HSSFWorkbook(new FileInputStream(new File(inputFilePath)));
            }

            Sheet sheet = workbook.getSheetAt(sheetIndex);
            endRow = endRow < 0 ? sheet.getPhysicalNumberOfRows() : endRow;

            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element resource = document.createElement("resource");
            resource.setAttribute("xmlns:tools", "http://schemas.android.com/tools");
            document.appendChild(resource);

            for (int i = startRow; i < endRow; i++) {
                Row row = sheet.getRow(i);
                int cellNumInRow = row.getPhysicalNumberOfCells();
                if (cellNumInRow <= 0){
                    continue;
                }
                keyColumn = cellNumInRow > keyColumn ? keyColumn : cellNumInRow;
                valueColumn = cellNumInRow > valueColumn ? valueColumn : cellNumInRow;

                Cell keyCell = row.getCell(keyColumn);
                Cell valueCell = row.getCell(valueColumn);

                Element element = document.createElement("string");
                element.setAttribute("name", keyCell.getStringCellValue());
                element.appendChild(document.createTextNode(valueCell.getStringCellValue()));
                resource.appendChild(element);
            }
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource domSource = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            String filePath = outputPath + "/values-" + translateLang + "/strings.xml";
            File file = new File(filePath);
            if (!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            PrintWriter printWriter = new PrintWriter(new FileOutputStream(filePath, false));
            StreamResult result = new StreamResult(printWriter);
            transformer.transform(domSource, result);
            translateResult = true;

            // print as XML
            parseXml(filePath);
        }catch (IOException e){
            e.printStackTrace();
        }catch (ParserConfigurationException e){
            e.printStackTrace();
        }catch (TransformerConfigurationException e){
            e.printStackTrace();
        }catch (TransformerException e){
            e.printStackTrace();
        }
        return translateResult;
    }

    public static void parseXml(String fileName){
        try{
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(fileName);

            NodeList list = document.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                NodeList nodeList = node.getChildNodes();
                for (int j = 0; j < nodeList.getLength(); j++) {
                    Node childNode = nodeList.item(j);
                    for (int k = 0; k < childNode.getChildNodes().getLength(); k++) {
                        Node tmpNode = nodeList.item(j);
                        System.out.println("key = " + tmpNode.getAttributes().getNamedItem("name").getTextContent() +
                                ", value = " + tmpNode.getTextContent());
                    }
                }
            }
        }catch (ParserConfigurationException e){
            e.printStackTrace();
        }catch (SAXException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static String convertCell2String(Cell cell){
        String cellStr = null;
        switch (cell.getCellType()){
            case Cell.CELL_TYPE_STRING:
                cellStr = cell.getStringCellValue();
                break;

            case Cell.CELL_TYPE_BOOLEAN:
                cellStr = String.valueOf(cell.getBooleanCellValue());
                break;

            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)){
                    SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy", Locale.US);
                    Date date = null;
                    try {
                        date = format.parse(cell.getDateCellValue().toString());
                    }catch (ParseException e){
                        e.printStackTrace();
                    }
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    cellStr = simpleDateFormat.format(date);
                }else {
                    cellStr = String.valueOf(cell.getNumericCellValue());
                }
                break;

            case Cell.CELL_TYPE_FORMULA:
                cellStr = cell.getCellFormula().toString();
                break;
        }

        return cellStr;
    }
}
