package com.example.backend.Controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.backend.Objects.*;
import com.example.backend.Service.KeyWordSequenceService;
import com.example.backend.Service.PaperService;
import com.example.backend.utils.MarkovClustering;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    @Resource
    private PaperService paperService;

    @ResponseBody
    @GetMapping(value = "/getPicJson",produces = "application/json;charset=UTF-8")
    public String getPicJson(@RequestParam(value = "related",defaultValue = "1") Integer related,
                             @RequestParam(value = "beginYear",defaultValue = "2017") Integer beginYear,
                             @RequestParam(value = "endYear",defaultValue = "2022") Integer endYear,
                             @RequestParam(value = "labelSize",defaultValue = "1") Integer labelSize,
                             @RequestParam(value = "nodeSize",defaultValue = "9") Integer nodeSize,
                             @RequestParam(value = "minValue",defaultValue = "2") Integer minValue) throws IOException {
        KeyWordSequenceNode.num=0;
//        String filePath;
//        switch(related){
//            case 4:
//                filePath="src/main/resources/static/cnki_低成本发动机_篇关摘.txt";break;
//            case 3:
//                filePath="src/main/resources/static/cnki_发动机装配_篇关摘.txt";break;
//            case 2:
//                filePath="src/main/resources/static/cnki_数据增强.txt";break;
//            case 1:
//            default:
//                filePath="src/main/resources/static/cnki_可解释.txt";break;
//        }
//        ArrayList<TempPaper> papers = keyWordSequenceService.getPapers(filePath);
        QueryWrapper<Paper> wrapper = new QueryWrapper<>();
        wrapper.ge("year",beginYear).le("year",endYear).orderByAsc("year");

        List<Paper> papers = paperService.list(wrapper);
//        keyWordSequenceService.selectPaperByYear(papers,beginYear,endYear);
        ArrayList<Node> nodes = keyWordSequenceService.getNodes(papers);
        GraphUtil.selectNodeByMinValue(nodes,minValue);
        ArrayList<Link> links = keyWordSequenceService.getLinks(papers,nodes);

        //删除孤立点
        GraphUtil.deleteSingleNode(nodes,links);
//        paperService.adjustNodeSizeWithABaseValue(nodes,nodeSize,labelSize); //时序图调整大小方法与其他图不同
        ArrayList<Category> categories = keyWordSequenceService.genarateXY(nodes,beginYear,endYear);
        GraphUtil.adjustLineWidth(links);

        return keyWordSequenceService.genJson(nodes,links,categories);
    }

    @ResponseBody
    @PostMapping(value = "/getPicJsonBySearch",produces = "application/json;charset=UTF-8")
    public String getPicJsonBySearch(@RequestParam(value = "paper",defaultValue = "{}") String rawPapers,
                                     @RequestParam(value = "beginYear",defaultValue = "2017") Integer beginYear,
                                     @RequestParam(value = "endYear",defaultValue = "2022") Integer endYear,
                                     @RequestParam(value = "labelSize",defaultValue = "1") Integer labelSize,
                                     @RequestParam(value = "nodeSize",defaultValue = "9") Integer nodeSize,
                                     @RequestParam(value = "minValue",defaultValue = "2") Integer minValue) throws IOException {


        List<Paper> papers = paperService.getPapers(rawPapers);
//        List<Paper> papers = paperService.getPapers(rawPapers);
        if(papers.size()==0){
            return  keyWordSequenceService.genJson(null,null,null);
        }
        keyWordSequenceService.selectPaperByYear(papers,beginYear,endYear);
        ArrayList<Node> nodes = keyWordSequenceService.getNodes(papers);
        GraphUtil.selectNodeByMinValue(nodes,minValue);
        ArrayList<Link> links = keyWordSequenceService.getLinks(papers,nodes);
        //删除孤立点
        GraphUtil.deleteSingleNode(nodes,links);
//        paperService.adjustNodeSizeWithABaseValue(nodes,nodeSize,labelSize); //时序图调整大小方法与其他图不同
        ArrayList<Category> categories = keyWordSequenceService.genarateXY(nodes,beginYear,endYear);
        GraphUtil.adjustLineWidth(links);
        return keyWordSequenceService.genJson(nodes,links,categories);
    }

    @ResponseBody
    @RequestMapping(value = "/getInsJson",produces = "application/json;charset=UTF-8")
    public String getInsJson(@RequestParam(value = "related",defaultValue = "1") Integer related,
                             @RequestParam(value = "limit",defaultValue = "1") Integer limit,
                             @RequestParam(value = "labelSize",defaultValue = "1") Integer labelSize,
                             @RequestParam(value = "nodeSize",defaultValue = "9") Integer nodeSize,
                             @RequestParam(value = "minValue",defaultValue = "2") Integer minValue) throws IOException {
        Node.num = 0;
        List<Paper> papers = paperService.getPapers();

        List<Node> nodes = paperService.getNodesByType(papers,"institue");
        paperService.adjustNodeSizeWithABaseValue(nodes,nodeSize,labelSize);
        GraphUtil.selectNodeByMinValue(nodes,minValue);
        paperService.adjustNodeSizeWithABaseValue(nodes,nodeSize,labelSize);
        List<Link> links = paperService.getLinksByType(papers,nodes,"institue");
        GraphUtil.adjustLineWidth(links);

        return paperService.splitNodesByUnionAndGenJson(nodes,links,limit);
    }

    @ResponseBody
    @RequestMapping(value = "/getAuthJson",produces = "application/json;charset=UTF-8")
    public String getAuthJson(@RequestParam(value = "related",defaultValue = "1") Integer related,
                              @RequestParam(value = "limit",defaultValue = "1") Integer limit,
                              @RequestParam(value = "labelSize",defaultValue = "1") Integer labelSize,
                              @RequestParam(value = "nodeSize",defaultValue = "9") Integer nodeSize,
                              @RequestParam(value = "minValue",defaultValue = "2") Integer minValue) throws IOException {
        Node.num = 0;
        List<Paper> papers = paperService.getPapers();

        List<Node> nodes = paperService.getNodesByType(papers,"author");

        GraphUtil.selectNodeByMinValue(nodes,minValue);
        paperService.adjustNodeSizeWithABaseValue(nodes,nodeSize,labelSize);
        List<Link> links = paperService.getLinksByType(papers,nodes,"author");
        GraphUtil.adjustLineWidth(links);
        return paperService.splitNodesByUnionAndGenJson(nodes,links,limit);
    }

    @ResponseBody
    @RequestMapping(value = "/getAuthBySearch",produces = "application/json;charset=UTF-8")
    public String getAuthJsonBySearch(@RequestParam(value = "paper",defaultValue = "{}") String rawPapers,
                                      @RequestParam(value = "limit",defaultValue = "1") Integer limit,
                                      @RequestParam(value = "labelSize",defaultValue = "1") Integer labelSize,
                                      @RequestParam(value = "nodeSize",defaultValue = "9") Integer nodeSize,
                                      @RequestParam(value = "minValue",defaultValue = "2") Integer minValue) throws IOException {
        Node.num = 0;
        List<Paper> papers = paperService.getPapers(rawPapers);

        List<Node> nodes = paperService.getNodesByType(papers,"author");

        GraphUtil.selectNodeByMinValue(nodes,minValue);
        paperService.adjustNodeSizeWithABaseValue(nodes,nodeSize,labelSize);
        List<Link> links = paperService.getLinksByType(papers,nodes,"author");
        GraphUtil.adjustLineWidth(links);
        return paperService.splitNodesByUnionAndGenJson(nodes,links,limit);
    }

    @ResponseBody
    @RequestMapping(value = "/getKeyJson",produces = "application/json;charset=UTF-8")
    public String getKeyJson(@RequestParam(value = "related",defaultValue = "1") Integer related,
                              @RequestParam(value = "limit",defaultValue = "1") Integer limit,
                             @RequestParam(value = "labelSize",defaultValue = "1") Integer labelSize,
                             @RequestParam(value = "nodeSize",defaultValue = "9") Integer nodeSize,
                             @RequestParam(value = "minValue",defaultValue = "2") Integer minValue) throws IOException {
        Node.num = 0;
        List<Paper> papers = paperService.getPapers();

        List<Node> nodes = paperService.getNodesByType(papers,"keyword");
        //删除与搜索同名的关键字
        GraphUtil.deleteNode(nodes,"可解释性");
        paperService.adjustNodeSizeWithABaseValue(nodes,nodeSize,labelSize);
        GraphUtil.selectNodeByMinValue(nodes,minValue);
        List<Link> links = paperService.getLinksByType(papers,nodes,"keyword");
        GraphUtil.adjustLineWidth(links);
        return paperService.splitNodesByUnionAndGenJson(nodes,links,limit);
    }

    @ResponseBody
    @RequestMapping(value = "/getKeyJsonBySearch",produces = "application/json;charset=UTF-8")
    public String getKeyJsonBySearch(@RequestParam(value = "power",defaultValue = "2") Integer power,
                                     @RequestParam(value = "inflation",defaultValue = "2") Integer inflation,
                                     @RequestParam(value = "labelSize",defaultValue = "1") Integer labelSize,
                                     @RequestParam(value = "nodeSize",defaultValue = "9") Integer nodeSize,
                                     @RequestParam(value = "limit",defaultValue = "1") Integer limit,
                             @RequestParam(value = "minValue",defaultValue = "2") Integer minValue) throws IOException {
        Node.num = 0;
        List<Paper> papers = paperService.getPapers();
        List<Node> nodes = paperService.getNodesByType(papers,"keyword");
        GraphUtil.selectNodeByMinValue(nodes,minValue);
        List<Link> links = paperService.getLinksByType(papers,nodes,"keyword");
        GraphUtil.deleteSingleNode(nodes,links);
        paperService.adjustNodeSizeWithABaseValue(nodes,nodeSize,labelSize);
        GraphUtil.adjustLineWidth(links);
        List<Category> categories = MarkovClustering.getResult(nodes,links,power,inflation);
        return JSON.toJSONString(new ReturnedJson(nodes,links,categories));
    }
}
