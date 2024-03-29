package com.example.caseBase.VO;


import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j; //log.info()

import java.io.Serializable;

@Data
@Slf4j
public class ResponseVO implements Serializable {

    @NonNull
    private Boolean success;

    private String message;

    private Object content;

    public static ResponseVO buildSuccess(Object content){
        ResponseVO response = new ResponseVO(true);
        response.setContent(content);
        return response;
    }

    public static ResponseVO buildFailure(String message){
        ResponseVO response = new ResponseVO(false);
        response.setMessage(message);
        log.info(message);
        return response;
    }

}
