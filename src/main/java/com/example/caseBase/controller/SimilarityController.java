package com.example.caseBase.controller;

import com.example.caseBase.VO.ResponseVO;
import com.example.caseBase.VO.SelectedTextVO;
import com.example.caseBase.service.SimilarityService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/similarity")
@CrossOrigin(origins = {"http://localhost:8080"},allowCredentials = "true")
public class SimilarityController {

    @Resource
    SimilarityService similarityService;

    @PostMapping("/getSimilarList")
    public ResponseVO getSimilarList(@RequestBody SelectedTextVO selectedTextVO) {
        return ResponseVO.buildSuccess(similarityService.getSimilarityList(selectedTextVO.getInputFileName(), selectedTextVO.getInputText(),
                selectedTextVO.getIsDifferent(), selectedTextVO.getSimilarityLimit(), selectedTextVO.getMatchNum()));
    }

}
