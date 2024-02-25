package com.example.caseBase.DAO;

import com.example.caseBase.PO.PolicyKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description:
 *
 * @author OrangeSummer
 * @date Created on 2023/2/5
 */

@Repository
public interface PolicyKeywordsRepository extends JpaRepository<PolicyKeyword, Integer> {
    List<PolicyKeyword> findAllByPid(int pid);
}
