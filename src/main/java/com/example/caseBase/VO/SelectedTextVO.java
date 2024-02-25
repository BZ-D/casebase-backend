package com.example.caseBase.VO;

import lombok.Data;

@Data
public class SelectedTextVO {

    String inputFileName;

    String inputText;

    String isDifferent;

    double similarityLimit;

    int matchNum;
}
