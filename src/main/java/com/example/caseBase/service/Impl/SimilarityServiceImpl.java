package com.example.caseBase.service.Impl;

import com.example.caseBase.DAO.PolicyKeywordsRepository;
import com.example.caseBase.DAO.PolicyRepository;
import com.example.caseBase.PO.Policy;
import com.example.caseBase.PO.PolicyKeyword;
import com.example.caseBase.VO.SimilarityVO;
import com.example.caseBase.service.IRService;
import com.example.caseBase.service.SimilarityService;
import com.example.caseBase.util.TextRankKeyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SimilarityServiceImpl implements SimilarityService {

    @Autowired
    private PolicyKeywordsRepository policyKeywordsRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private IRService irService;

    // todo
    public List<SimilarityVO> getSimilarityList(String inputFileName, String inputText, String isDifferent, double similarityLimit, int matchNum) {
        // 计算出inputText的词频
        Map<String, Double> inputTextKeywords = TextRankKeyword.getKeyword(inputFileName, inputText);
        // <pid，similarity>
        Map<Integer, Double> pidSimilarity = new HashMap<>();
        List<String> departments = new ArrayList<>();
        departments.add("电子银行部");departments.add("风险管理部");departments.add("合规管理部");
        departments.add("计划财务部");departments.add("金融市场部");departments.add("人力资源部");
        departments.add("三农业务部");departments.add("授信评审部");departments.add("信贷管理部");
        departments.add("信息科技部");departments.add("运营管理部");
        Map<String, Double> departSimilarity = new HashMap<>();
        for (String department : departments) {
            Map<String, Double> departmentKeywords = TextRankKeyword.getKeyword("", department);
            double similarity = irService.calSimilarity(inputTextKeywords, departmentKeywords);
            departSimilarity.put(department, similarity);
        }
        List<Map.Entry<String, Double>> similarDepartment = new ArrayList<>(departSimilarity.entrySet());
        similarDepartment.sort((o1, o2) -> {
            if ((o2.getValue() - o1.getValue()) > 0)
                return 1;
            else if ((o2.getValue() - o1.getValue()) == 0)
                return 0;
            else
                return -1;
        });
        List<String> similarDepartments = new ArrayList<>();
        // 寻找部门相似前n
        for (int i = 0; i < 4; i++) {
            similarDepartments.add(similarDepartment.get(i).getKey());
        }
        for (String department : similarDepartments) {
            List<Policy> policies = policyRepository.findByDepartment(department);
            HashSet<String> titles = new HashSet<>();
            for (Policy policy : policies) {
                titles.add(policy.getTitle());
            }
            Map<String, Double> titleSimilarity = new HashMap<>();
            for (String title : titles) {
                Map<String, Double> titleKeywords = TextRankKeyword.getKeyword(title, "");
                double similarity = irService.calSimilarity(inputTextKeywords, titleKeywords);
                titleSimilarity.put(title, similarity);
            }
            List<Map.Entry<String, Double>> similarTitle = new ArrayList<>(titleSimilarity.entrySet());
            similarTitle.sort((o1, o2) -> {
                if ((o2.getValue() - o1.getValue()) > 0)
                    return 1;
                else if ((o2.getValue() - o1.getValue()) == 0)
                    return 0;
                else
                    return -1;
            });
            List<String> similarTitles = new ArrayList<>();
            // 寻找标题前n相似的
            for (int i = 0; i < 3; i++) {
                similarTitles.add(similarTitle.get(i).getKey());
            }
            for (String title : similarTitles) {
                List<Policy> byTitleAndDepartment = policyRepository.findByTitleAndDepartment(title, department);
                for (Policy policy : byTitleAndDepartment) {
                    List<PolicyKeyword> allByPid = policyKeywordsRepository.findAllByPid(policy.getId());
                    HashMap<String, Double> policyTextKeywords = new HashMap<>();
                    int count = 0;
                    for (PolicyKeyword policyKeyword : allByPid) {
                        policyTextKeywords.put(policyKeyword.getKeyword(), (double) policyKeyword.getTermFrequency());
                        count++;
                        if (count >= 6) break;
                    }
                    double similarity = irService.calSimilarity(inputTextKeywords, policyTextKeywords);
                    pidSimilarity.put(policy.getId(), similarity);
                }
            }
        }
        List<Map.Entry<Integer, Double>> listData = new ArrayList<>(pidSimilarity.entrySet());
        listData.sort((o1, o2) -> {
            if ((o2.getValue() - o1.getValue()) > 0)
                return 1;
            else if ((o2.getValue() - o1.getValue()) == 0)
                return 0;
            else
                return -1;
        });
//        System.out.println("listData = " + listData);
        // 在相关标题下寻找最接近的4个结果(标题各异)
        List<SimilarityVO> ret = new ArrayList<>();
        if(Objects.equals(isDifferent, "false")) {
            // 精度模式
            for (int i = 0; i < matchNum; i++) {
                if (listData.get(i).getValue() >= similarityLimit) {
                    SimilarityVO similarityVO = new SimilarityVO();
                    int pid = listData.get(i).getKey();
                    Policy byId = policyRepository.getById(pid);
                    similarityVO.setRuleFileName(byId.getTitle());
                    similarityVO.setRuleText(byId.getContent());
                    similarityVO.setSimilarity(listData.get(i).getValue());
                    ret.add(similarityVO);
                }
            }
        } else {
            // 广度模式
            Set<String> titles = new HashSet<>();
            for (Map.Entry<Integer, Double> listDatum : listData) {
                int pid = listDatum.getKey();
                Policy byId = policyRepository.findById(pid);
                String title = byId.getTitle();
                if (!titles.contains(title) && listDatum.getValue() >= similarityLimit) {
                    titles.add(title);
                    SimilarityVO similarityVO = new SimilarityVO();
                    similarityVO.setRuleFileName(title);
                    similarityVO.setRuleText(byId.getContent());
                    similarityVO.setSimilarity(listDatum.getValue());
                    ret.add(similarityVO);
                }
                if (titles.size() > matchNum - 1 || listDatum.getValue() < similarityLimit) break;
            }
        }
        return ret;
    }
}
