package com.example.caseBase.DAO;

import com.example.caseBase.PO.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Integer> {
    Policy findById(int id);

    void deleteById(int id);

    List<Policy> findByDepartment(String department);

    List<Policy> findByTitleAndDepartment(String title, String department);
}
