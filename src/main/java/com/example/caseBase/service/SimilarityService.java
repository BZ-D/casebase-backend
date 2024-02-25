package com.example.caseBase.service;

import com.example.caseBase.VO.SimilarityVO;

import java.util.List;

public interface SimilarityService {

    List<SimilarityVO> getSimilarityList(String inputFileName, String inputText, String isDifferent, double similarityLimit, int matchNum);

}
