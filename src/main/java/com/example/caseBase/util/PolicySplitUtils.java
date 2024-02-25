package com.example.caseBase.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 政策文本分割工具类
 */
public class PolicySplitUtils {
    static char[] cnArr = new char[]{'一', '二', '三', '四', '五', '六', '七', '八', '九'};
    static String cnArrString = "一二三四五六七八九十";

    public static List<List> split(String text) {
        List<String> texts = Arrays.stream(text.split("\n")).collect(Collectors.toList());
        return split(texts);
    }

    public static List<List> split(List<String> texts) {
        for (String line : texts) {
            line = line.replaceAll(" ", " ");
            line = line.replaceAll("　", " ");
            line = line.replaceAll("\\s", "");
            if (line.startsWith("第一条")) {
                return splitType1(texts);
            } else if (line.startsWith("一、")) {
                return splitType2(texts);
            }
        }
        System.out.println("parse error");
        List<List> res = new ArrayList<>();
        StringBuilder content = new StringBuilder();
        for (String text : texts) {
            content.append(text);
        }
        res.add(new ArrayList(Arrays.asList(1, 1, content)));
        return res;
    }

    // type: 第一章 第一条
    private static List<List> splitType1(List<String> texts) {
        List<List> res = new ArrayList();
        StringBuilder resLine = new StringBuilder();
        int chapter = 1;
        int article = 0;
        int currLine;
        boolean isContent = false;
        for (currLine = 0; currLine < texts.size(); currLine++) {
            String line = texts.get(currLine);
            String tmpLine = line.replaceAll("　", " ");
            // tmpLine = tmpLine.replaceAll("\\s", "");
            if (tmpLine.length() > 0 && tmpLine.charAt(0) == '第') {
                isContent = true;
                if (tmpLine.indexOf("章") > 0 && tmpLine.indexOf("章") < 10) {
                    if (resLine.length() > 0) {
                        res.add(Arrays.asList(chapter, article, resLine.toString()));
                        resLine.setLength(0);
                    }
                    chapter = chineseNumToArabicNumTo(tmpLine.substring(1, tmpLine.indexOf("章")));
                } else if (tmpLine.indexOf("条") > 0 && tmpLine.indexOf("条") < 10) {
                    if (resLine.length() > 0) {
                        res.add(Arrays.asList(chapter, article, resLine.toString()));
                        resLine.setLength(0);
                    }
                    int newArticle = chineseNumToArabicNumTo(tmpLine.substring(1, tmpLine.indexOf("条")));
                    if (newArticle - article != 1) {
                        chapter = -1;
                    }
                    article = newArticle;
                    resLine.append(tmpLine);
                } else {
                    if (tmpLine.charAt(2) != '节') {
                        resLine.append(tmpLine);
                    }
                }
            } else {
                if (isContent) {
                    resLine.append(tmpLine);
                }
            }
        }
        if (resLine.length() > 0) {
            res.add(Arrays.asList(chapter, article, resLine.toString()));
        }
        return res;
    }

    // type: 一、
    private static List<List> splitType2(List<String> texts) {
        List<List> res = new ArrayList<>();
        StringBuilder resLine = new StringBuilder();
        int chapter = 1;
        int article = 0;
        int currLine;
        boolean isContent = false;
        for (currLine = 0; currLine < texts.size(); currLine++) {
            String line = texts.get(currLine);
            String tmpLine = line.replaceAll("　", " ");
            // tmpLine = tmpLine.replaceAll("\\s", "");
            if (tmpLine.length() > 0 && cnArrString.contains(String.valueOf(tmpLine.charAt(0)))) {
                isContent = true;
                if (resLine.length() > 0) {
                    res.add(Arrays.asList(chapter, article, resLine.toString()));
                    resLine.setLength(0);
                }
                article = chineseNumToArabicNumTo(tmpLine.substring(0, tmpLine.indexOf("、")));
            } else {
                if (isContent) {
                    resLine.append(tmpLine);
                }
            }
        }
        if (resLine.length() > 0) {
            res.add(Arrays.asList(chapter, article, resLine.toString()));
        }
        return res;
    }

    /**
     * 将数字转换为中文数字， 这里只写到了百
     *
     * @param intInput
     * @return
     */
    private static String arabicNumToChineseNum(int intInput) {
        String si = String.valueOf(intInput);
        String sd = "";
        if (si.length() == 1) {
            if (intInput == 0) {
                return sd;
            }
            sd += cnArr[intInput - 1];
            return sd;
        } else if (si.length() == 2) {
            if (si.substring(0, 1).equals("1")) {
                sd += "十";
                if (intInput % 10 == 0) {
                    return sd;
                }
            } else
                sd += (cnArr[intInput / 10 - 1] + "十");
            sd += arabicNumToChineseNum(intInput % 10);
        } else if (si.length() == 3) {
            sd += (cnArr[intInput / 100 - 1] + "百");
            if (String.valueOf(intInput % 100).length() < 2) {
                if (intInput % 100 == 0) {
                    return sd;
                }
                sd += "零";
            }
            sd += arabicNumToChineseNum(intInput % 100);
        }
        return sd;
    }

    private static int chineseNumToArabicNumTo(String input) {
        int res = 0;
        if (input.length() == 1) {
            res = cnArrString.indexOf(input) + 1;
        } else if (input.length() == 2) {
            if (input.charAt(0) == '十') {
                res += 10;
                res += cnArrString.indexOf(String.valueOf(input.charAt(1))) + 1;
            } else if (input.charAt(1) == '十') {
                res += (cnArrString.indexOf(String.valueOf(input.charAt(0))) + 1) * 10;
            } else if (input.charAt(1) == '百') {
                res += (cnArrString.indexOf(String.valueOf(input.charAt(0))) + 1) * 100;
            }
        } else if (input.length() == 3) {
            res += cnArrString.indexOf(String.valueOf(input.charAt(2))) + 1;
            res += (cnArrString.indexOf(String.valueOf(input.charAt(0))) + 1) * 10;
        } else if (input.length() == 4) {
            if (input.charAt(2) == '零') {
                res += (cnArrString.indexOf(String.valueOf(input.charAt(3))) + 1);
                res += (cnArrString.indexOf(String.valueOf(input.charAt(0))) + 1) * 100;
            } else {
                res += (cnArrString.indexOf(String.valueOf(input.charAt(2))) + 1) * 10;
                res += (cnArrString.indexOf(String.valueOf(input.charAt(0))) + 1) * 100;
            }
        } else if (input.length() == 5) {
            res += cnArrString.indexOf(String.valueOf(input.charAt(4))) + 1;
            res += (cnArrString.indexOf(String.valueOf(input.charAt(2))) + 1) * 10;
            res += (cnArrString.indexOf(String.valueOf(input.charAt(0))) + 1) * 100;
        }
        return res;
    }

    public static void main(String[] args) {
        // String text1 = ReadDoc.readDoc("D:\\存放\\互联网实践\\迭代二\\标准版内规\\信贷管理部--制度\\《贷款担保管理办法（试行）》.doc");
        String text2 = ReadDoc.readDoc("D:\\存放\\互联网实践\\迭代二\\标准版内规\\计划财务部-制度\\《银行账户利率风险管理办法》.docx");
        List<List> res = PolicySplitUtils.split(text2);
        for (List a : res) {
            System.out.println(a.get(2).toString().length());
            System.out.println("----------");
        }
        // System.out.println(PolicySplitUtils.chineseNumToArabicNumTo("一百三十九"));
    }
}
