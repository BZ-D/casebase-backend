package com.example.caseBase.DAO;

import com.example.caseBase.PO.Litigant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LitigantRepository extends JpaRepository<Litigant, Long> {

    Litigant getLitigantByName(String name);

    List<Litigant> findAllByDocId(Long id);

}
