package com.example.caseBase.util;

import com.example.caseBase.DAO.PolicyRepository;
import com.example.caseBase.PO.Policy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description: springboot启动时创建新线程进行内规导入
 *
 * @author OrangeSummer
 * @date Created on 2023/2/6
 */
@Component
public class PolicyImport implements CommandLineRunner {

    @Resource
    private PolicyRepository policyRepository;

    @Value("${policy-import}")
    private Boolean policyImport;

    @Override
    public void run(String... args) throws Exception {
        if (policyImport) {
            new Thread(() -> {
                try {
                    String path = "D:\\存放\\互联网实践\\迭代二\\标准版内规\\";
                    List<String> filePaths = ReadDoc.getFilePaths(path);
                    System.out.println(filePaths);
                    for (String filePath : filePaths) {
                        System.out.println("read " + filePath);
                        String temp[] = filePath.split("\\\\");
                        String title = temp[temp.length - 1].split("\\.")[0];
                        String department = temp[temp.length - 2].split("-")[0];
                        String text = ReadDoc.readDoc(filePath);
                        List<List> contents = PolicySplitUtils.split(text);
                        for (List content : contents) {
                            Policy policy = new Policy();
                            policy.setDepartment(department);
                            policy.setTitle(title);
                            policy.setChapter((Integer) content.get(0));
                            policy.setArticle((Integer) content.get(1));
                            policy.setContent(content.get(2).toString());
                            policyRepository.save(policy);
                            System.out.println(policy);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
