package co.lujun.betranslate;

import java.io.*;

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
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by lujun on 16/8/30.
 */
public class BeTranslate {

    public static void main(String[] args) {
//        try {
//            Workbook workbook = Workbook.getWorkbook(new File("src/co/lujun/kor.xlsx"));
//            Sheet sheet = workbook.getSheet(0);
//
//            System.out.println("sheet 0 cols = " + sheet.getColumns());
//            System.out.println("sheet 0 rows = " + sheet.getRows());
//
//            for (int i = 0; i < sheet.getRows(); i++) {
//                Cell keyCell = sheet.getCell(i, 0);
//                Cell valueCell = sheet.getCell(i, 1);
//                System.out.println("key = " + keyCell.getContents());
//                System.out.println("value = " + valueCell.getContents());
//            }
//        }catch (IOException e){
//            e.printStackTrace();
//        }catch (BiffException e){
//            e.printStackTrace();
//        }

        try {
            Workbook workbook;
            if (isExcel2007("kor.xlsx")){
                workbook = new XSSFWorkbook(new File("src/co/lujun/kor.xlsx"));
            }else {
                workbook = new HSSFWorkbook(new FileInputStream(new File("src/co/lujun/kor.xlsx")));
            }

            Sheet sheet = workbook.getSheetAt(0);

//            System.out.println("sheet 0 rows = " + sheet.getPhysicalNumberOfRows());

            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

            Element resource = document.createElement("resource");
            resource.setAttribute("xmlns:tools", "http://schemas.android.com/tools");

            document.appendChild(resource);

            for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
//                System.out.println("row " + i + " cols = " + row.getPhysicalNumberOfCells());

                Cell keyCell = row.getCell(0);
                Cell valueCell = row.getCell(1);

//                System.out.println("key = " + keyCell.getStringCellValue());
//                System.out.println("value = " + valueCell.getStringCellValue());

                Element element = document.createElement("string");
                element.setAttribute("name", keyCell.getStringCellValue());
                element.appendChild(document.createTextNode(valueCell.getStringCellValue()));
                resource.appendChild(element);
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource domSource = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            PrintWriter printWriter = new PrintWriter(new FileOutputStream("string.xml"));
            StreamResult result = new StreamResult(printWriter);
            transformer.transform(domSource, result);
        }catch (IOException e){
            e.printStackTrace();
        }catch (InvalidFormatException e){
            e.printStackTrace();
        }catch (ParserConfigurationException e){
            e.printStackTrace();
        }catch (TransformerConfigurationException e){
            e.printStackTrace();
        }catch (TransformerException e){
            e.printStackTrace();
        }

        parseXml("string.xml");
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

    public static boolean isExcel2007(String fileName){
        return fileName.matches("^.+\\\\.(?i)(xlsx)$");
    }
}
