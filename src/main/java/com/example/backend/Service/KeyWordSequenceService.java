package com.example.backend.Service;

import com.example.backend.Objects.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @ClassName: KeyWordSequenceService
 * @Description:
 * @Author: luning
 * @Date: 2022/11/6 16:38
 * @Version: v1.0
 */
public interface KeyWordSequenceService {
    public static Random random = new Random();

    ArrayList<TempPaper> getPapers(String path) throws IOException;

//    //通过限制发表年份来筛选文章
//    ArrayList<TempPaper> selectPaperByYear(ArrayList<TempPaper> papers, int begin, int end);

    //通过限制发表年份来筛选文章
    ArrayList<Paper> selectPaperByYear(ArrayList<Paper> papers, int begin, int end);

    //通过限制出现次数来筛选关键词节点,并且去掉link中的source节点
    List<Node> selectNodeByMinValue(List<Node> nodes, int minvalue);

    ArrayList<Node> getNodes(List<Paper> papers);

    ArrayList<Link> getLinks(List<Paper> papers, ArrayList<Node> nodes);

    ArrayList<Category> genarateXY(ArrayList<Node> nodes, int beginYear, int endYear);

    //节点、link信息转成json
    String genJson(ArrayList<Node> nodes, ArrayList<Link> links,ArrayList<Category> categories);


}
