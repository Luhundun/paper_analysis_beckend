package com.example.backend.Controller;

import com.example.backend.Objects.KeyWordSequenceNode;
import com.example.backend.Objects.Link;
import com.example.backend.Objects.Node;
import com.example.backend.Objects.Paper;
import com.example.backend.Service.KeyWordSequenceService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;

import com.example.backend.utils.GraphUtil;

/**
 * @ClassName: requestJsonController
 * @Description:
 * @Author: luning
 * @Date: 2022/11/2 14:50
 * @Version: v1.0
 */
@Controller
@CrossOrigin
public class requestJsonController {

    @Resource
    private KeyWordSequenceService keyWordSequenceService;

    @ResponseBody
    @RequestMapping(value = "/getPicJson",produces = "application/json;charset=UTF-8")
    public String getPicJson(@RequestParam(value = "related",defaultValue = "1") Integer related,
                             @RequestParam(value = "beginYear",defaultValue = "2017") Integer beginYear,
                             @RequestParam(value = "endYear",defaultValue = "2022") Integer endYear,
                             @RequestParam(value = "minValue",defaultValue = "2") Integer minValue) throws IOException {
        KeyWordSequenceNode.num=0;
        String filePath;
        switch(related){
            case 4:
                filePath="src/main/resources/static/cnki_低成本发动机_篇关摘.txt";break;
            case 3:
                filePath="src/main/resources/static/cnki_发动机装配_篇关摘.txt";break;
            case 2:
                filePath="src/main/resources/static/cnki_数据增强.txt";break;
            case 1:
            default:
                filePath="src/main/resources/static/cnki_可解释.txt";break;
        }
        ArrayList<Paper> papers = keyWordSequenceService.getPapers(filePath);
        keyWordSequenceService.selectPaperByYear(papers,beginYear,endYear);
        ArrayList<Node> nodes = keyWordSequenceService.getNodes(papers);
        keyWordSequenceService.selectNodeByMinValue(nodes,minValue);
        ArrayList<Link> links = keyWordSequenceService.getLinks(papers,nodes);

        //删除孤立点
        GraphUtil.deleteSingleNode(nodes,links);

        keyWordSequenceService.genarateXY(nodes,beginYear,endYear);
        GraphUtil.adjustLineWidth(links);


        return keyWordSequenceService.genJson(nodes,links,beginYear,endYear);
    }
}
