package com.example.backend;

import com.example.backend.Objects.Paper;
import com.example.backend.Service.KeyWordSequenceService;
import com.example.backend.Service.PaperService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;

@SpringBootTest
class TempPaperAnalysisBackendApplicationTests {


    @Autowired
    PaperService paperService;
    @Autowired
    KeyWordSequenceService keyWordSequenceService;

    @Test
    void contextLoads() throws IOException {
        System.out.println(paperService.list());
        paperService.save(new Paper());
    }

}
