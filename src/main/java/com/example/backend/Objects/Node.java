package com.example.backend.Objects;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * @ClassName: Node
 * @Description:
 * @Author: luning
 * @Date: 2022/11/6 16:49
 * @Version: v1.0
 */

@Data
public class Node {

    public static  int num=1;
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


    //出度
    @JSONField(serialize = false)
    protected int outDegree=0;
    //入度
    @JSONField(serialize = false)
    protected int inDegree=0;

    @JSONField(name = "category")
    protected int category;

    @JSONField(name = "label")
    protected Label label;

    public Node() {
    }
    public Node(String name) {
        this.id=String.valueOf(num++);
        this.name = name;
        this.value = 1;
    }

    public Node(String id, String name, double symbolSize, double value, int outDegree, int inDegree, int category) {
        this.id = id;
        this.name = name;
        this.symbolSize = symbolSize;
        this.value = value;
        this.outDegree = outDegree;
        this.inDegree = inDegree;
        this.category = category;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(id, node.id) && Objects.equals(name, node.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }


    @Data
    public static class Label{
        @JSONField(name = "fontSize")
        int fontSize;

        public Label(int value) {
            fontSize = value;
        }
    }
}
