package com.example.caseBase.DAO;

import com.example.caseBase.PO.Punishment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PunishmentRepository extends JpaRepository<Punishment, Long>, JpaSpecificationExecutor<Punishment> {

}
