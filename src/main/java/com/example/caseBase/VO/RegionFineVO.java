package com.example.caseBase.VO;

import lombok.Data;

@Data
public class RegionFineVO {

    public RegionFineVO(String name) {
        this.name = name;
        this.fine=0;
        this.value=0;
    }

    //省份名，写死的。
    private String name;

    //罚单数量
    int value;

    //罚单总额
    long fine;

}
