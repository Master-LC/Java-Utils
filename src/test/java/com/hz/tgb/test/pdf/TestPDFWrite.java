package com.hz.tgb.test.pdf;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * PDF 生成
 *
 * PDFBox教程 - https://www.yiibai.com/pdfbox/
 *
 * Created by hezhao on 2018/9/29 16:00
 */
public class TestPDFWrite {

    public static void main(String[] args) throws IOException {

//        createPage();

//        settingPDFAttr();

//        saveDocument();

//        saveManyLineDocument();

        mergeDocument();

    }

    /**
     * 创建页面
     */
    public static void createPage() throws IOException {
        // 创建文档
        PDDocument document = new PDDocument();

        for (int i=0; i<10; i++) {
            // 创建页面
            PDPage blankPage = new PDPage();

            // 添加
            document.addPage( blankPage );
        }

        // 保存
        document.save("D:/my_doc.pdf");
        System.out.println("PDF created");

        // 关闭
        document.close();
    }

    /**
     * 设置文档属性
     */
    public static void settingPDFAttr() throws IOException {
        // 创建文档
        PDDocument document = new PDDocument();

        // 创建页面
        PDPage blankPage = new PDPage();

        // 添加
        document.addPage( blankPage );

        // 文档属性
        PDDocumentInformation pdd = document.getDocumentInformation();

        //Setting the author of the document
        pdd.setAuthor("Yiibai.com");

        // Setting the title of the document
        pdd.setTitle("一个简单的文档标题");

        //Setting the creator of the document
        pdd.setCreator("PDF Examples");

        //Setting the subject of the document
        pdd.setSubject("文档标题");

        //Setting the created date of the document
        Calendar date = new GregorianCalendar();
        date.set(2017, 11, 5);
        pdd.setCreationDate(date);
        //Setting the modified date of the document
        date.set(2018, 10, 5);
        pdd.setModificationDate(date);

        //Setting keywords for the document
        pdd.setKeywords("pdfbox, first example, my pdf");

        // 保存
        document.save("D:/doc_attributes.pdf");

        System.out.println("Properties added successfully ");

        //Closing the document
        document.close();
    }

    /**
     * 添加文档
     * @throws IOException
     */
    public static void saveDocument() throws IOException {

        // 1、加载现有文件
//        File file = new File("D:/my_doc.pdf");
//        PDDocument document = PDDocument.load(file);
//        // 获取所需的页面
//        PDPage page = document.getPage(0);

        // 2、创建新文件
        PDDocument document = new PDDocument();
        // 创建页面
        PDPage page = new PDPage();
        document.addPage(page);

        // 准备内容流
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // 开始文本
        contentStream.beginText();

        // 字体
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);

        // 设置文本的位置
        contentStream.newLineAtOffset(25, 500);

        String text = "This is the sample document and we are adding content to it. - By yiibai.com";

        // 插入文本
        contentStream.showText(text);

        // 结束文本
        contentStream.endText();
        System.out.println("Content added");

        // 关闭内容流
        contentStream.close();

        // 保存文档
        document.save(new File("D:/new-doc-text.pdf"));

        // 关闭文件
        document.close();
    }

    /**
     * 添加多行文档
     * @throws IOException
     */
    public static void saveManyLineDocument() throws IOException {

        // 1、加载现有文件
        File file = new File("D:/new-doc-text.pdf");
        PDDocument document = PDDocument.load(file);

        // 页数
        int pages = document.getNumberOfPages();
        System.out.println(pages);

        // 获取所需的页面
//        PDPage page = document.getPage(0);

        // 追加内容
        PDPage page = new PDPage();
        document.addPage(page);


        // 2、创建新文件
//        PDDocument document = new PDDocument();
//        // 创建页面
//        PDPage page = new PDPage();
//        document.addPage(page);

        // 准备内容流
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // 开始文本
        contentStream.beginText();

        // 字体
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);

        // 设置文本引导
        contentStream.setLeading(14.5f);

        // 设置文本的位置
        contentStream.newLineAtOffset(25, 725);

        String text = "This is the sample document and we are adding content to it. - By yiibai.com";

        // 插入文本
        contentStream.showText(text);

        String text1 = "This is an example of adding text to a page in the pdf document. we can add as many lines";
        String text2 = "as we want like this using the ShowText()  method of the ContentStream class";

        // 插入文本
        contentStream.showText(text1);

        // 使用newline()插入多个字符串
        contentStream.newLine();
        contentStream.newLine();
        contentStream.showText(text2);
        contentStream.newLine();

        // 结束文本
        contentStream.endText();
        System.out.println("Content added");

        // 关闭内容流
        contentStream.close();

        // 保存文档
        document.save(new File("D:/new-doc-text-lines.pdf"));

        // 关闭文件
        document.close();
    }

    /**
     * 合并多个PDF文档
     * @throws IOException
     */
    public static void mergeDocument() throws IOException {

        // 1、加载现有文件
        File file1 = new File("D:/new-doc-text.pdf");
        PDDocument doc1 = PDDocument.load(file1);

        File file2 = new File("D:/new-doc-image.pdf");
        PDDocument doc2 = PDDocument.load(file2);

        // 实例化PDFMergerUtility类
        PDFMergerUtility PDFmerger = new PDFMergerUtility();

        // 设置目标文件
        PDFmerger.setDestinationFileName("D:/merged.pdf");

        // 设置源文件
        PDFmerger.addSource(file1);
        PDFmerger.addSource(file2);

        // 合并文档
        PDFmerger.mergeDocuments();

        System.out.println("Documents merged");

        // 关闭文档
        doc1.close();
        doc2.close();
    }

}
