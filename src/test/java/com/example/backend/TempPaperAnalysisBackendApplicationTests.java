package com.example.backend;

import com.example.backend.Objects.Link;
import com.example.backend.Objects.Node;
import com.example.backend.Objects.Paper;
import com.example.backend.Service.KeyWordSequenceService;
import com.example.backend.Service.PaperService;
import com.example.backend.utils.GraphUtil;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

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

    @Test
    void testNode() throws IOException {
        Node.num=0;
        List<Paper> papers = paperService.getPapers();

        List<Node> nodes = paperService.getNodesForInstitutes(papers);

        GraphUtil.selectNodeByMinValue(nodes,2);
        List<Link> links = paperService.getLinks(papers,nodes);

        System.out.println(paperService.splitNodesByUnionAndGenJson(nodes,links,1));

    }

}
