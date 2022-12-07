package com.example.backend;

import com.example.backend.Objects.Link;
import com.example.backend.Objects.Node;
import com.example.backend.Objects.Paper;
import com.example.backend.Service.KeyWordSequenceService;
import com.example.backend.Service.PaperService;
import com.example.backend.utils.GraphUtil;
import com.example.backend.utils.MarkovClustering;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        List<Node> nodes = paperService.getNodesByType(papers,"keyword");
        GraphUtil.selectNodeByMinValue(nodes,2);
        List<Link> links = paperService.getLinksByType(papers,nodes,"keyword");
        System.out.println(MarkovClustering.getResult(nodes,links,2,2));

    }

}
