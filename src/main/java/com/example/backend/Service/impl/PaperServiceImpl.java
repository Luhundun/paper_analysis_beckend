package com.example.backend.Service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.Objects.*;
import com.example.backend.Service.PaperService;
import com.example.backend.mapper.PaperMapper;
import com.example.backend.utils.GraphUtil;
import com.example.backend.utils.UnionFind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
* @author luning
* @description 针对表【paper】的数据库操作Service实现
* @createDate 2022-11-10 16:08:13
*/
@Service
public class PaperServiceImpl extends ServiceImpl<PaperMapper, Paper>
    implements PaperService{

    @Autowired
    PaperMapper paperMapper;

    @Override
    public List<Paper> getPapers() throws IOException {
        List<Paper> papers = paperMapper.selectList(new QueryWrapper<>());
        return papers;
    }

    @Override
    public List<Paper> getPapers(String rawPapers){
        List<Paper> papers = new ArrayList<>();
        papers = JSONArray.parseArray(rawPapers, Paper.class);
        return papers;
    }

    /**
     * @Description: 根据需求生成关系节点图（机构、关键字、作者）
     * @param: [papers]
     * @return: java.util.ArrayList<com.example.backend.Objects.Node>
     * @auther: Lu Ning
     * @date: 2022/11/11 10:57
     */
    @Override
    public List<Node> getNodesByType(List<Paper> papers, String type){
        ArrayList<Node> nodes = new ArrayList<>();
        String[] elements;
        for (Paper paper:papers) {
            switch (type){
                case "institue": elements = paper.getInstitues().split(";"); break;
                case "author": elements = paper.getAuthors().split(";"); break;
                case "keyword": elements = paper.getKeywords().split(";"); break;
                default: elements = new String[1];
            }
            for (String element : elements) {
                //可优化时间复杂度
                //若关键字存在则增加大小
                boolean isExist = false;
                for (int i = 0; i < nodes.size(); i++) {
                    Node node = nodes.get(i);
                    if (node.getName().equals(element)) {
                        isExist = true;
                        node.setValue(node.getValue() + 1);
                        break;
                    }
                }
                //若关键字不存在则创建
                if (!isExist) {
                    Node temp = new Node(element);
                    nodes.add(temp);
                }
            }
        }
        //微调点的大小
        adjustNodeSizeWithABaseValue(nodes,9);
        return nodes;
    }

    /**
     * @Description: 生成关键字的关系图
     * @param: [papers]
     * @return: java.util.ArrayList<com.example.backend.Objects.Node>
     * @auther: Lu Ning
     * @date: 2022/11/11 10:59
     */
    @Deprecated
    public ArrayList<Node> getNodesForKeyword(List<Paper> papers){
        ArrayList<Node> nodes = new ArrayList<>();
        String[] keywords;
        for (Paper paper:papers) {
            keywords = paper.getKeywords().split(";");
            for (String keyword : keywords) {
                //可优化时间复杂度
                //若关键字存在则增加大小
                boolean isExist = false;
                for (int i = 0; i < nodes.size(); i++) {
                    Node node = nodes.get(i);
                    if (node.getName().equals(keyword)) {
                        isExist = true;
                        node.setValue(node.getValue() + 1);
                        break;
                    }
                }
                //若关键字不存在则创建
                if (!isExist) {
                    Node temp = new Node(keyword);
                    nodes.add(temp);
                }
            }
        }
        //微调点的大小
        adjustNodeSizeWithABaseValue(nodes,10);
        return nodes;
    }

    /**
     * @Description: 用一个基准值调整点的大小
     * @param: [node, base]
     * @return: void
     * @auther: Lu Ning
     * @date: 2022/11/11 10:58
     */
    @Override
    public void adjustNodeSizeWithABaseValue(List<Node> nodes, int base){
        for(int i=0;i<nodes.size();i++){
            Node node=nodes.get(i);
            double size = node.getValue();
            //用这个公式改变圆的大小
            size = Math.log(size*base)/Math.log(1.5);
            node.setSymbolSize(size);
            node.setLabel(new Node.Label(1+(int)(size)));
        }
    }

    /**
     * @Description: 根据类型生成Link
     * @param: [papers, nodes, type]
     * @return: java.util.List<com.example.backend.Objects.Link>
     * @auther: Lu Ning
     * @date: 2022/11/11 11:22
     */
    @Override
    public List<Link> getLinksByType(List<Paper> papers, List<Node> nodes, String type) {
        ArrayList<Link> links = new ArrayList<>();
        String[] elements;
        for (Paper paper:papers){
            switch (type){
                case "institue": elements = paper.getInstitues().split(";"); break;
                case "author": elements = paper.getAuthors().split(";"); break;
                case "keyword": elements = paper.getKeywords().split(";"); break;
                default: elements = new String[1];
            }
            int linkLength = elements.length;
            //根据文章关键字获取关键字节点
            for(int i=0;i<linkLength-1;i++){
                Node ni = Node.getInList(elements[i],nodes);
                //节点被筛选掉则略过
                if(ni==null){
                    continue;
                }
                for (int j=i+1;j<linkLength;j++){
                    Node nj = Node.getInList(elements[j],nodes);
                    //节点被筛选掉则略过
                    if (nj==null){
                        continue;
                    }
                    //比较，年份小的为起点，大的为终点
                    Node source = ni.getSymbolSize()>nj.getSymbolSize()?ni:nj;
                    Node target = ni.getSymbolSize()>nj.getSymbolSize()?nj:ni;

                    //检查是否已经有连线，有则加粗，无则创建
                    if(!Link.isExistAndAdjuestIt(source.getId(),target.getId(),links)){
                        Link temp = new Link(source,target);

                        links.add(temp);
                    }
                }
            }
        }
        return links;
    }

    /**
     * @Description: 用并查集生成json
     * @param: [nodes, links, limit]
     * @return: void
     * @auther: Lu Ning
     * @date: 2022/11/10 19:43
     */
    @Override
    public String splitNodesByUnionAndGenJson(List<Node> nodes, List<Link> links, int limit){
        //删除权重小的线
        for(int i=0;i<links.size();i++) {
            Link link = links.get(i);
            if (link.getValue() < limit) {

                links.remove(i);
                i--;
            }
        }
        //删除孤立点
        GraphUtil.deleteSingleNode(nodes,links);

        //建立并查集
        UnionFind uf= new UnionFind(nodes);
        //用边的连线完善并查集
        for(int i=0;i<links.size();i++){
            Link link = links.get(i);
            uf.union(link.getTargetNode(), link.getSourceNode());
        }

        for (Node node:nodes) {
            uf.findFather(node);
        }

//        获取所有节点的根
        HashMap<Node,Integer> reflect = new HashMap<>();
        int seq = 0;
        for (Map.Entry<Node,Node> entry:uf.getMap().entrySet()) {
            Node node = entry.getKey();
            Node father = entry.getValue();
            //如果父亲存在，那么将这个节点的目录设为父亲编号
            if (reflect.containsKey(father)) {
                node.setCategory(reflect.get(father));
            } else {
                //反之先创造编号
                node.setCategory(seq);
                reflect.put(father, seq++);
            }
        }


        //建立目录
        ArrayList<Category> categories = new ArrayList<>();
        for(int i=0;i<seq;i++){
            categories.add(new Category(String.valueOf(i)));
        }
        return JSON.toJSONString(new ReturnedJson(nodes,links,categories));

//        HashMap<String, List<Node>> rootMap = new HashMap<>();
//        //
//        for (Map.Entry<Node,Node> entry:uf.getMap().entrySet()){
//            //如果根存在，那么将这个节点添加到根的list中
//            if (rootMap.containsKey(entry.getValue().getId())){
//                rootMap.get(entry.getValue().getId()).add(entry.getKey());
//            }
//            //不存在就先建立根并添加节点
//            else {
//                ArrayList<Node> temp = new ArrayList<>();
//                temp.add(entry.getKey());
//                rootMap.put(entry.getValue().getId(),temp);
//            }
//
//        }

    }


//    public void putPaperIntoDatabase(){
//
//    }
}




