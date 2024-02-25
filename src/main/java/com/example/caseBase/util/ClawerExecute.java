package com.example.caseBase.util;

import com.example.caseBase.service.DocumentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;

/**
 * Description:
 *
 * @author OrangeSummer
 * @date Created on 2022/10/2
 */
@Component
public class ClawerExecute implements CommandLineRunner {

    @Resource
    private DocumentService documentService;

    @Value("${clawer}")
    private Boolean clawer;

    @Override
    public void run(String... args) {
        if (clawer){
            new Thread(() -> {
                Clawer clawer = new Clawer(documentService);
                try {
                    clawer.getDocURL(1);
                    clawer.getDocURL(2);
                } catch (IOException | ParseException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }
}
