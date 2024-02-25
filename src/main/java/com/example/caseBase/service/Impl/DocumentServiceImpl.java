package com.example.caseBase.service.Impl;

import com.example.caseBase.DAO.DocumentRepository;
import com.example.caseBase.PO.Doc;
import com.example.caseBase.PO.Litigant;
import com.example.caseBase.VO.DocVO;
import com.example.caseBase.dto.QueryBody;
import com.example.caseBase.service.DocumentService;
import com.example.caseBase.util.ESUtil;
import com.example.caseBase.util.ExtractInformation;
import com.example.caseBase.util.SnowFlakeGenerateIdWorker;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
//指定发布webService的接口类，此类也需要接入@WebService注解
public class DocumentServiceImpl implements DocumentService {

    @Resource
    ESUtil esUtil;

    @Resource
    DocumentRepository documentRepository;

    @Resource
    ExtractInformation extractInformation;


    @Override
    public Doc saveOneDoc(Doc doc) {
        //save和delete操作应该同时影响MYSQL和ES
        try {
            esUtil.addDocument(doc, "document");
            Doc doc1 = documentRepository.save(doc);
            extractInformation.extract(doc1);
            return doc;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Doc buildOneDoc(DocVO docVO) {
        //新建DocId
        docVO.setDocId(SnowFlakeGenerateIdWorker.nextId());

        Doc docPO = new Doc(docVO);
        return this.saveOneDoc(docPO);
    }

    @Override
    public Doc findByDocId(Long docId) {
        Doc doc = documentRepository.findByDocId(docId);
        if (doc != null) {
            System.out.println("----------查询成功-----------");
            return doc;
        } else {
            System.out.println("----------该文书不存在----------");
            return null;
        }
    }

    @Override
    public void deleteByDocId(Long docId) {
        if (documentRepository.findByDocId(docId) != null) {
            documentRepository.deleteByDocId(docId);
            System.out.println("----------删除成功-----------");
        } else {
            System.out.println("----------该文书不存在----------");
        }
    }


    @Override
    public Doc Import(List<String> list) throws IOException {
        String name = list.get(0);
        String docNum = list.get(1);
        //对当事人进行处理并对type进行判断
        Litigant litigantPerson = new Litigant(list.get(2));
        litigantPerson.setUnit(list.get(3));
        Litigant litigantUnit = new Litigant(list.get(4));
        litigantUnit.setChargeMan(list.get(4));
        List<Litigant> litigants = new ArrayList<>();
        if (list.get(2).length() == 0 && list.get(3).length() == 0) {
            litigants.add(litigantUnit);
        } else if (list.get(4).length() == 0 && list.get(5).length() == 0) {
            litigants.add(litigantPerson);
        } else {
            litigants.add(litigantPerson);
            litigants.add(litigantUnit);
        }

        int type = 0;
        if (list.get(2).length() != 0) {
            if (list.get(4).length() == 0) {
                type = 0;
            } else {
                type = 2;
            }
        } else type = 1;
        String cause = list.get(6);
        String basis = list.get(7);
        String decision = list.get(8);
        String organ = list.get(9);
        String date = list.get(10);
        int state = 0;//导入的文档默认是未发布的

        DocVO docVO = new DocVO();
        docVO.setName(name);
        docVO.setDocNum(docNum);
        docVO.setType(type);
        docVO.setLitigants(litigants);
        docVO.setCause(cause);
        docVO.setBasis(basis);
        docVO.setDecision(decision);
        docVO.setOrgan(organ);
        docVO.setDate(date);
        docVO.setState(state);
        docVO.setHtml(null);
        docVO.setContent(null);

        return buildOneDoc(docVO);
    }

    @Override
    public Doc updateOneDoc(DocVO doc) {
        Doc docPO = new Doc(doc);

        try {
            return this.saveOneDoc(docPO);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void releaseDocs(List<Doc> releaseDocList) {
        for (Doc doc : releaseDocList) {
            this.saveOneDoc(doc);
        }
    }

    @Override
    public List<Doc> queryDocs(QueryBody body) {
        System.out.println(body.ToString());
        List<Doc> docs = new ArrayList<>();
        try {
            Map<Long, String> docIdList = esUtil.queryDocument(body);
            System.out.println("docIdList:" + docIdList.size());
            for (Long id : docIdList.keySet()) {
                System.out.println("id:" + id);
                Doc doc = documentRepository.findByDocId(id);
                if (docIdList.get(id).length() < 10) {
                    doc.setHighLightedContent((doc.getContent().length() > 150) ? doc.getContent().substring(0, 140) : doc.getContent());
                } else {
                    System.out.println("doc:" + doc);
                    System.out.println("docIdList.get(id):" + docIdList.get(id));
                    doc.setHighLightedContent(docIdList.get(id));
                }
                docs.add(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        List<Doc> records = new PageInfo<>(docs.size(), docs).getRecords();
//        for (int i =0; i < records.size(); i++) {
//            docs.add(records.get(i));
//        }
        return docs;
    }


}
