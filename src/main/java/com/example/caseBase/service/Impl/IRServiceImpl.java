package com.example.caseBase.service.Impl;

import com.example.caseBase.DAO.PolicyRepository;
import com.example.caseBase.PO.Policy;
import com.example.caseBase.VO.PolicyMatchVO;
import com.example.caseBase.VO.PolicyRetrievalVO;
import com.example.caseBase.service.IRService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

// todo:修改实现VSM来进行文本相似度的计算和排序
@Service
public class IRServiceImpl implements IRService {

    @Autowired
    PolicyRepository policyRepository;

    @Override
    public PolicyMatchVO retrieval(PolicyRetrievalVO policyRetrievalVO) {
        PolicyMatchVO res = new PolicyMatchVO();
        // // 1. 提取关键词
        // Map<String, Float> keywords = TextRankKeyword.getKeyword("", policyRetrievalVO.getText());
        // if(keywords.size()<10){
        //     res.setFound(false);
        //     res.setMessage("句子过短，请重新选择");
        //     return res;
        // }
        // // 2. 关键词匹配
        // List<Policy> policies = policyRepository.findByPiId(policyRetrievalVO.getPiId());
        // if(!checkSplit(policies)){
        //     res.setFound(false);
        //     res.setMessage("当前政策文本不支持匹配");
        //     return res;
        // }
        // System.out.println(keywords);
        // List<Pair<Float, Integer>> scoresIndexPair = retrievalByTFIDF(keywords, policies);
        // // 3. 分析结果
        // float maxScore = 0;
        // for (int i = 0; i < scoresIndexPair.size(); i++) {
        //     maxScore = scoresIndexPair.get(i).getLeft() > maxScore ? scoresIndexPair.get(i).getLeft() : maxScore;
        // }
        // if (maxScore == 0) {
        //     res.setFound(false);
        //     res.setMessage("未找到匹配项");
        //     return res;
        // }else{
        //     res.setFound(true);
        //     res.setPolicyDoc(contactByTFIDF(policies, scoresIndexPair).toString());
        //     res.setPolicyMatchItems(getListByTFIDF(policies, scoresIndexPair));
        //     res.setMessage("匹配成功");
        // }
        return res;
    }

    /**
     * 根据TF-IDF检索匹配项
     *
     * @param keywords       关键词-权重
     * @param policies 政策分割
     * @return List<Pair < Float, Integer>> 列表，得分:index，index为policySplitPOS对应
     */
    private List<Pair<Float, Integer>> retrievalByTFIDF(Map<String, Float> keywords, List<Policy> policies) {
        List<Map<String, Float>> TFs = new ArrayList<>();
        List<Pair<Float, Integer>> scoresIndexPair = new ArrayList<>();
//         Map<String, Integer> wordCounter = new HashMap<>();
//         for (String keyword : keywords.keySet()) {
//             wordCounter.put(keyword, 0);
//         }
//         int itemCnt = 0;
//         for (Policy splitPO : policies) {
//             Map<String, Float> tf = new HashMap<>();
//             if (splitPO.isItem()) {
//                 itemCnt++;
//                 for (String keyword : keywords.keySet()) {
//                     int cnt = countStr(splitPO.getText(), keyword);
//                     tf.put(keyword, (float) cnt/TextRankKeyword.countWord(splitPO.getText()));
// //                    tf.put(keyword, (float) cnt);
//                     if (cnt > 0) {
//                         wordCounter.put(keyword, wordCounter.get(keyword) + 1);
//                     }
//                 }
//             }
//             TFs.add(tf);
//         }
//         for (int i = 0; i < policies.size(); i++) {
//             float score = 0;
//             if (policies.get(i).isItem()) {
//                 for (String keyword : keywords.keySet()) {
//                     float idf = (float) Math.log((float) itemCnt / (wordCounter.get(keyword) + 1));
//                     if (idf < 0) {
//                         idf = 0;
//                     }
//                     score += keywords.get(keyword) * TFs.get(i).get(keyword) * idf;
//                 }
//             }
//             scoresIndexPair.add(Pair.of(score, i));
//             System.out.println("==============" + score + "==============");
//             System.out.println(policies.get(i).getText());
//         }
        return scoresIndexPair;
    }

    private List<String> getListByTFIDF(List<Policy> policies, List<Pair<Float, Integer>> scoresIndexPair) {
        Collections.sort(scoresIndexPair, new Comparator<Pair<Float, Integer>>() {
            @Override
            public int compare(Pair<Float, Integer> o1, Pair<Float, Integer> o2) {
                return (o1.getLeft() - o2.getLeft() > 0 ? -1 : 1);
            }
        });
        List<String> res = new ArrayList<>();
        // for (Pair<Float, Integer> pair : scoresIndexPair) {
        //     if (pair.getLeft() > 0) {
        //         res.add(policies.get(pair.getRight()).getText());
        //     }
        // }
        return res;
    }

    private StringBuilder contactByTFIDF(List<Policy> policies, List<Pair<Float, Integer>> scoresIndexPair) {
//        Collections.sort(scoresIndexPair, new Comparator<Pair<Float, Integer>>() {
//            @Override
//            public int compare(Pair<Float, Integer> o1, Pair<Float, Integer> o2) {
//                return (o1.getLeft() - o2.getLeft() > 0 ? -1 : 1);
//            }
//        });
        float maxScore = 0;
        for (int i = 0; i < scoresIndexPair.size(); i++) {
            maxScore = scoresIndexPair.get(i).getLeft() > maxScore ? scoresIndexPair.get(i).getLeft() : maxScore;
        }
        StringBuilder res = new StringBuilder();
        if (maxScore == 0) {
            return res;
        }
        // for (int i = 0; i < policies.size(); i++) {
        //     Policy split = policies.get(i);
        //     if (split.isItem()) {
        //         if (scoresIndexPair.get(i).getLeft() / maxScore > 0.5) {
        //             res.append("<highlight>");
        //             res.append(split.getText());
        //             res.append("</highlight>");
        //         } else {
        //             res.append(split.getText());
        //         }
        //     } else {
        //         res.append(split.getText());
        //     }
        // }
        return res;
    }

    /**
     * 检查划分是否支持匹配
     * @param policies
     * @return
     */
    private boolean checkSplit(List<Policy> policies){
        if(policies.size()==0){
            return false;
        }
        // for(Policy split: policies){
        //     if(split.isItem()){
        //         return true;
        //     }
        // }
        return false;
    }

    /**
     * count how many str2 is contained in str1
     *
     * @param str1
     * @param str2
     * @return
     */
    private int countStr(String str1, String str2) {
        int counter = 0;
        while (str1.contains(str2)) {
            counter++;
            str1 = str1.substring(str1.indexOf(str2) + str2.length());
        }
        return counter;
    }


    /**
     * 以VSM的特征值计算余弦相似度
     * @param text1 xx=0.981格式的String数组
     * @param text2 xx=0.981格式的String数组
     * @return 余弦相似度
     */
    public double calSimilarity(Map<String, Double> text1, Map<String, Double> text2){
        Map<String,Double> keywords1 = text1;
        Map<String,Double> keywords2 = text2;

        double sim = 0;   //相似度
        double sum = 0;   //相同特征的权重相乘之和
        double len1 = 0;
        double len2 = 0;
        for(Map.Entry<String,Double> keyword : keywords1.entrySet()){
            String f = keyword.getKey();
            double value = keyword.getValue();
            if(keywords2.containsKey(f)){
                sum += value * keywords2.get(f);
            }
            len1 += value * value;
        }
        for(Map.Entry<String,Double> keyword : keywords2.entrySet()){
            double value = keyword.getValue();
            len2 += value * value;
        }
        sim = sum / (Math.sqrt(len1) * Math.sqrt(len2));
        return sim;
    }

    public static void main(String[] args) {
        IRServiceImpl irService = new IRServiceImpl();
        Map<String, Double> map1 = new HashMap<>();
        map1.put("这个", 1.2);
        map1.put("那个", 1.3);
        map1.put("没有", 0.8);
        Map<String, Double> map2 = new HashMap<>();
        map2.put("这个", 1.2);
        map2.put("那个", 1.3);
        map2.put("没有", 0.9);
        double similarity = irService.calSimilarity(map1, map2);
        System.out.println("similarity = " + similarity);
    }
}

