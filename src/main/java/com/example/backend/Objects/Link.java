package com.example.backend.Objects;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.ArrayList;

/**
 * @ClassName: Link
 * @Description:
 * @Author: luning
 * @Date: 2022/11/1 15:45
 * @Version: v1.0
 */
@Data
public class Link {
//    static ArrayList<Link> allLinks = new ArrayList<>();

    @JSONField(name = "source")
    private String source;     //起点
    @JSONField(serialize = false)
    private Node sourceNode;

    @JSONField(name = "target")
    private String target;     //终点
    @JSONField(serialize = false)
    private Node targetNode;

    @JSONField(serialize = false)
    private int value;     //重复数量

    @JSONField(name = "lineStyle")
    private LineStyle lineStyle;
    public Link(Node sourceNode, Node targetNode) {
        this.sourceNode=sourceNode;
        this.targetNode=targetNode;
        this.source = sourceNode.getId();
        this.target = targetNode.getId();
        this.value =1;
    }

    /**
     * @Description: 如果两个关键字已经相连，将重复数量增加。否则创建节点
     * @param: [source, target]
     * @return: com.example.backend.Objects.Link
     * @auther: Lu Ning
     * @date: 2022/11/1 15:53
     */
    public static boolean isExistAndAdjuestIt(String source,String target,ArrayList<Link>links){
        for (Link link:links){
            if (link.source==source&&link.target==target){
                link.setValue(link.getValue()+1);
                return true;
            }
        }
        return false;
    }

//    public static  isInLinks(){
//
//    }

    @Data
    public static class LineStyle{
        @JSONField(name = "width")
        int width;

        public LineStyle(int value) {
            width = value;
        }
    }

}
