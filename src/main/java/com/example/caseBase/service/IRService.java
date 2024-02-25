package com.example.caseBase.service;


import com.example.caseBase.VO.PolicyMatchVO;
import com.example.caseBase.VO.PolicyRetrievalVO;

import java.util.Map;

public interface IRService {
    PolicyMatchVO retrieval(PolicyRetrievalVO policyRetrievalVO);

    double calSimilarity(Map<String, Double> text1, Map<String, Double> text2);
}
