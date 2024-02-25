package com.example.caseBase.util;

import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 读取doc和docx文件
 *
 * @author OrangeSummer
 * @date Created on 2023/2/5
 */
public class ReadDoc {
    public static String readDoc(String path) {
        String text = "";
        try {
            if (path.endsWith(".doc")) {
                FileInputStream fileInputStream = new FileInputStream(path);
                WordExtractor doc = new WordExtractor(fileInputStream);
                text = doc.getText();
                fileInputStream.close();
            } else if (path.endsWith(".docx")) {
                FileInputStream fileInputStream = new FileInputStream(path);
                XWPFWordExtractor docx = new XWPFWordExtractor(new XWPFDocument(fileInputStream));
                text = docx.getText();
                fileInputStream.close();
            } else {
                System.out.println("此文件不是word文件！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    public static List<String> getFilePaths(String path) {
        List<String> res = new ArrayList<>();
        File file = new File(path);
        // 如果这个路径是文件夹
        if (file.isDirectory()) {
            // 获取路径下的所有文件
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                // 如果还是文件夹 递归获取里面的文件 文件夹
                if (files[i].isDirectory()) {
                    // System.out.println("目录：" + files[i].getPath());
                    //继续读取文件夹里面的所有文件
                    res.addAll(getFilePaths(files[i].getPath()));
                } else {
                    String temp = files[i].getPath();
                    if (temp.endsWith(".doc") || temp.endsWith(".docx")) {
                        res.add(temp);
                    }
                    // System.out.println("文件：" + temp);
                }
            }
        } else {
            String temp = file.getPath();
            if (temp.endsWith(".doc") || temp.endsWith(".docx")) {
                res.add(temp);
            }
            // System.out.println("文件：" + temp);
        }
        // System.out.println(res);
        return res;
    }

    public static void main(String[] args) {
        String path = "D:\\存放\\互联网实践\\迭代二\\标准版内规\\";
        String temp = "D:\\存放\\互联网实践\\迭代二\\标准版内规\\人力资源部—制度\\《中层管理人员任用办法》.docx";
        System.out.println(ReadDoc.readDoc(temp));
    }
}
