package co.lujun.betranslate;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: lujun(http://blog.lujun.co)
 * Date: 2016-8-30 10:01
 */
public class BeTranslateUtil {

    public static boolean doTranslate(String inputFilePath, String outputPath, String translateLang, int sheetIndex,
                                      int startRow, int endRow, int keyColumn, int valueColumn, boolean needFill,
                                      String referXmlPath) {
        boolean translateResult = false;
        List<String> keys = new ArrayList<String>();
        try {
            Workbook workbook/* = WorkbookFactory.create(new File(inputFilePath))*/;
            if (inputFilePath.matches("[\\s\\S]*?([^\\/]*?\\.xlsx)")){
                workbook = new XSSFWorkbook(inputFilePath);
            }else {
                workbook = new HSSFWorkbook(new FileInputStream(new File(inputFilePath)));
            }

            Sheet sheet = workbook.getSheetAt(sheetIndex);
            endRow = endRow < 0 ? sheet.getPhysicalNumberOfRows() : endRow;

            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element resource = document.createElement("resources");
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
                keys.add(keyCell.getStringCellValue());
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
            translateResult = needFill ? fillTranslation(keys, referXmlPath, filePath) : true;

            // print as XML
            if (translateResult) {
                printXml(filePath);
            }
        }catch (IOException e){
            e.printStackTrace();
        }catch (ParserConfigurationException e){
            e.printStackTrace();
        }catch (TransformerConfigurationException e){
            e.printStackTrace();
        }catch (TransformerException e){
            e.printStackTrace();
        }/*catch (InvalidFormatException e){
            e.printStackTrace();
        }*/
        return translateResult;
    }

    public static boolean fillTranslation(List<String> keys, String referXmlFilePath, String needFillXmlFilePath){
        boolean result = false;
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(referXmlFilePath);
            Node element = document.getChildNodes().item(0);
            NodeList nodeList;
            if (element != null && (nodeList = element.getChildNodes()) != null) {
                Document fillDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                        .parse(needFillXmlFilePath);
                Node resources = fillDocument.getChildNodes().item(0);
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node child = nodeList.item(i);
                    NamedNodeMap map = child.getAttributes();
                    Node node = map.item(0);
                    if (node != null && !keys.contains(node.getNodeValue())){
                        keys.add(node.getNodeValue());
                        Element childString = fillDocument.createElement("string");
                        childString.setAttribute("name", node.getNodeValue());
                        childString.appendChild(fillDocument.createTextNode(child.getNodeValue()));
                        resources.appendChild(element);
                    }
                }
                result = true;
            }
        }catch (ParserConfigurationException e){
            e.printStackTrace();
        }catch (SAXException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }

    public static boolean fillTranslation(String originXmlFilePath, String referXmlFilePath, String needFillXmlFilePath){
        List<String> keys = new ArrayList<String>();
        // TODO get keys from xml file

        return fillTranslation(keys, referXmlFilePath, needFillXmlFilePath);
    }

    public static void printXml(String fileName){
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = bufferedReader.readLine()) != null){
                System.out.println(line);
            }
            bufferedReader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
