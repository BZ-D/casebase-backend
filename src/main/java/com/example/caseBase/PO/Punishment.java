package com.example.caseBase.PO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "punishment")
public class Punishment implements Serializable {

    @javax.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private Long case_id;

    private Long litigant_id;

    private String litigantName;

    private String docName;

    private String docNum;

    private String province;

    private String city;
    //1: 总局、 2: 本级级、3: 分局级
    private Integer level;

    private String type;

    private String organ;

    private Integer fine;

    @Column(columnDefinition = "text")
    private String cause;

    private Date date;

    private String basis;

}
