package com.sy.filesearch_esdemo.utils;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.*;
import java.nio.charset.Charset;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;


/**
 * @ClassName FileUtil
 * @Description
 * @Author sunyu
 * @Date 2023/10/12 14:08
 * @Version 1.0
 **/
public class FileUtil {


    public static String readWord(String filePath) {
        String[] split = filePath.split("\\.");
        String content = "";
        if (split[split.length - 1].equalsIgnoreCase("doc")) {
            content = FileUtil.readWordDoc(filePath);
        } else if (split[split.length - 1].equalsIgnoreCase("docx")){
            content = FileUtil.readWordDocX(filePath);
        }
        return content;
    }

    /**
     * 读取word文档中后缀为doc的文件
     * @param filePath
     * @return
     */
    public static String readWordDoc(String filePath){
        String content = null;
        InputStream input;
        try {
            input = Files.newInputStream(Paths.get(filePath));
            WordExtractor wex = new WordExtractor(input);
            content = wex.getText();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return content;
    }

    /**
     * 读取word文档中后缀为docx的文件
     * @param filePath
     * @return
     */
    public static String readWordDocX(String filePath){
        String content = null;
        OPCPackage opcPackage = null;
        try {
            opcPackage = POIXMLDocument.openPackage(filePath);
            XWPFDocument xwpf = new XWPFDocument(opcPackage);
            POIXMLTextExtractor poiText = new XWPFWordExtractor(xwpf);
            content = poiText.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }


    /**
     * 读取Excel文件内容
     * @param filePath
     * @return
     */
    public static String readExcel(String filePath){
        StringBuilder content = new StringBuilder();
        try {
            FileInputStream excelFile = new FileInputStream(filePath); // 替换为你的文件路径
            Workbook workbook;
            String[] split = filePath.split("\\.");
            if (split[split.length-1].equalsIgnoreCase("xlsx")) {
                workbook = new XSSFWorkbook(excelFile);
            } else if (split[split.length-1].equalsIgnoreCase("xls")) {
                workbook = new HSSFWorkbook(excelFile);
            } else {
                throw new IllegalArgumentException("不支持的文件类型");
            }

            // 循环遍历每个 sheet 页
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                System.out.println("Sheet名称: " + sheet.getSheetName());

                // 遍历每一行
                Iterator<Row> rowIterator = sheet.iterator();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();

                    // 遍历每个单元格
                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();

                        // 根据单元格类型获取数据
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_STRING:
                                content.append(cell.getStringCellValue()).append("\t");
                                System.out.print(cell.getStringCellValue() + "\t");
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                content.append(cell.getNumericCellValue()).append("\t");
                                System.out.print(cell.getNumericCellValue() + "\t");
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                content.append(cell.getBooleanCellValue()).append("\t");
                                System.out.print(cell.getBooleanCellValue() + "\t");
                                break;
                            case Cell.CELL_TYPE_BLANK:
                                System.out.print("BLANK\t");
                                break;
                            default:
                                System.out.print("OTHER\t");
                                break;
                        }
                    }
                    System.out.println();
                }
            }

            excelFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }


    /**
     * txt文本文件  转pdf文件
     * @param textPath   F:/data/te616.txt
     * @param pdfPath  F:/data/aet618.pdf
     * @throws DocumentException
     * @throws IOException
     */
    public static void textToPdf(String textPath,String pdfPath) throws DocumentException, IOException {
        Document doc = new Document();
        OutputStream os = new FileOutputStream(new File(pdfPath));
        PdfWriter.getInstance(doc, os);
        doc.open();
        //指定 使用内置的中文字体
        BaseFont baseFont =
                BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
        Font font = new Font(baseFont,12,Font.NORMAL);
        //指定输出编码为UTF-8
        InputStreamReader isr = new InputStreamReader(
                new FileInputStream(new File(textPath)), Charset.forName("UTF-8"));
        BufferedReader br = new BufferedReader(isr);
        String str = "";
        while((str = br.readLine()) != null){
            doc.add(new Paragraph(str,font));
        }
        isr.close();
        br.close();
        doc.close();
    }


    /**
     * 读取pdf文件的内容
     * @param filePath  F:/data/aet618.pdf
     * @return  String
     */
    public static String readPDF(String filePath){
        StringBuilder result = new StringBuilder();
        try {
            PdfReader reader = new PdfReader(filePath);
            int countPage = reader.getNumberOfPages();
            for(int i=1;i<=countPage;i++){
                result.append(PdfTextExtractor.getTextFromPage(reader, i));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }


    /**
     * 将内容输出到文件
     * @param content
     * @param outputFilePath
     */
    public static void writeContentToFile(String content, String outputFilePath) {
        try {
            File outputFile = new File(outputFilePath);
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }

            // 使用UTF-8编码写入文件
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
            writer.write(content);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
