package com.example.backend.Objects;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: Node
 * @Description:
 * @Author: luning
 * @Date: 2022/11/6 16:49
 * @Version: v1.0
 */

@Data
public class Node {

    @JSONField(name = "id")
    protected String id;
    @JSONField(name = "name")
    protected String name;

    //大小
    @JSONField(name = "symbolSize")
    protected double symbolSize;

    @JSONField(name = "value")
    protected double value;

    //坐标
    @JSONField(name = "x")
    protected double x;
    @JSONField(name = "y")
    protected double y;

    //出度
    @JSONField(serialize = false)
    protected int outDegree=0;
    //入度
    @JSONField(serialize = false)
    protected int inDegree=0;

    public static boolean isInList(String name, List<Node> nodes){
        for (Node node:nodes){
            if (name.equals(node.getName())){
                return true;
            }
        }
        return false;
    }

    public static Node getInList(String name, List<Node> nodes){
        for (Node node:nodes){
            if (name.equals(node.getName())){
                return node;
            }
        }
        return null;
    }
}
