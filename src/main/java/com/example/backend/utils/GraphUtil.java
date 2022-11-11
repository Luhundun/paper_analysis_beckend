package com.example.backend.utils;

import com.example.backend.Objects.Link;
import com.example.backend.Objects.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: GraphUtil
 * @Description:
 * @Author: luning
 * @Date: 2022/11/6 16:45
 * @Version: v1.0
 */
public class GraphUtil {

    //计算两点之间的欧氏距离来判断是否会碰撞
    public static boolean isCollide(Double[] oldPoint,double x,double y,double size){
        return !((oldPoint[0] - x) * (oldPoint[0] - x) + (oldPoint[1] - y) * (oldPoint[1] - y) > (oldPoint[2] + size) * (oldPoint[2] + size));
    }

    //微调线条长度
    public static void adjustLineWidth(ArrayList<Link> links){
        for (Link link:links){
            int value = link.getValue();
            value = (int) Math.ceil(Math.sqrt(value*3-2));
            Link.LineStyle style = new Link.LineStyle(value);
            link.setLineStyle(style);
        }
    }

    //让link中sourceNode与ArrayList中的sourceNode匹配,从而减少后续的算法复杂度
    public static void matchNode(List<Node> nodes, List<Link> links){
        for (Link link:links){
            for(Node node:nodes){
                if(link.getSource().equals(node.getId())){
                    link.setSourceNode(node);
                }
                if(link.getTarget().equals(node.getId())){
                    link.setTargetNode(node);
                }
            }
        }
    }


    //计算点的入度和出度
    public static void computeDegree(List<Node> nodes,List<Link> links){

        for(int i=0;i<links.size();i++){
            Link link = links.get(i);
            //去掉空的情况
            if(link.getSourceNode()==null || link.getTargetNode()==null){
                links.remove(i);
                i--;
                continue;
            }
            //分别增加两节点的入度和出度
            link.getSourceNode().setOutDegree(link.getSourceNode().getOutDegree()+1);
            link.getTargetNode().setInDegree(link.getTargetNode().getInDegree()+1);
        }
    }

    //消除孤立点
    public static void deleteSingleNode(List<Node> nodes,List<Link> links){
        //首先要计算点的入度和出度
//        computeDegree(nodes,links);
//
//        for(int i=0;i<nodes.size();i++){
//            Node node = nodes.get(i);
//            //无意义的就从列表移除
//            if(node.getInDegree()+node.getOutDegree()==0){
//                nodes.remove(i);
//                i--;
//            }
//        }
        computeDegree(nodes,links);

        for(int i=0;i<nodes.size();i++){
            Node node = nodes.get(i);
            //无意义的就从列表移除
            if(node.getInDegree()+node.getOutDegree()==0){
                nodes.remove(i);
                i--;
//                System.out.println("移除了"+node);
            }else{

//                System.out.println("没有移除"+node);
            }
        }
    }

    public static List<Node> selectNodeByMinValue(List<Node> nodes, int minvalue) {
        for (int i=0;i< nodes.size();i++){
            Node node = nodes.get(i);
            if(node.getValue()<minvalue){
                nodes.remove(i);
//                //二分查找可以优化加速
//                for(int j=0;j<links.size();j++){
//                    Link link = links.get(j);
//                    if(link.getSource()==node.getId()||link.getTarget()==node.getId()){
//                        links.remove(j);
//                        j--;
//                    }
//                }
                i--;
            }
        }
        return nodes;
    }
}
