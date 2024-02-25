package com.example.caseBase.PO;


import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "litigant")
public class Litigant implements Serializable {
    @javax.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    Long docId;

    String name;

    String province;

    String type;

    String unit;

    String chargeMan;

    public Litigant(String name) {
        this.name = name;
    }

    public Litigant() {
    }

    public Litigant(String name, String str2, int type, Doc doc) {
        this.name = name;
        this.docId = doc.docId;
        if (type == 0) {
            //create a litigant as a person.
            this.unit = str2;
        } else {
            //create a litigant as an organization.
            this.chargeMan = str2;
        }
    }
}
