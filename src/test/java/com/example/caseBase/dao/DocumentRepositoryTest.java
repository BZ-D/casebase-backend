package com.example.caseBase.dao;

import com.example.caseBase.CaseBaseApplication;
import com.example.caseBase.DAO.DocumentRepository;
import com.example.caseBase.PO.Doc;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CaseBaseApplication.class)
public class DocumentRepositoryTest {

    @Autowired
    public DocumentRepository documentRepository;

    @Test
    public void testFindByDocId() {
        Doc byDocId = documentRepository.findByDocId(1055205L);
        System.out.println(byDocId);
    }

    @Test
    @Transactional
    public void testDeleteByDocId() {
        Doc doc = documentRepository.findByDocId(1055205L);
        System.out.println(doc);
        documentRepository.deleteByDocId(1055205L);
        Doc doc2 = documentRepository.findByDocId(1055205L);
        System.out.println(doc2);
    }
}
