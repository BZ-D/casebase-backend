package com.example.caseBase.VO;

import lombok.Data;

import java.util.List;

@Data
public class PolicyMatchVO {
    boolean found;
    String policyDoc;
    List<String> policyMatchItems;
    String message;
}
