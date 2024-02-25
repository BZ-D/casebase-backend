package com.example.caseBase.DAO;

import com.example.caseBase.PO.Doc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface DocumentRepository extends JpaRepository<Doc, Long>, JpaSpecificationExecutor<Doc> {

    @Query(value = "select * from document as d where d.doc_id = ?1", nativeQuery = true)
    Doc findByDocId(Long docId);

    @Modifying
    @Transactional(rollbackOn=Exception.class)
    @Query(value = "delete from document as d where d.doc_id = ?1", nativeQuery = true)
    void deleteByDocId(Long docId);

}