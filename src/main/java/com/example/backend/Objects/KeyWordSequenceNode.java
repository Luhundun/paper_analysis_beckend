package com.example.backend.Objects;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: KeyWordSequenceNode
 * @Description:
 * @Author: luning
 * @Date: 2022/11/1 15:19
 * @Version: v1.0
 */
@Data
public class KeyWordSequenceNode extends Node{
//    static ArrayList<KeyWordSequenceNode> nodeArrayList = new ArrayList<>();
    public static int num=0;


    @JSONField(name = "x")
    protected double x;
    @JSONField(name = "y")
    protected double y;

    @JSONField(serialize = false)
    private int year;



    public KeyWordSequenceNode(String name, int year) {
        this.id = String.valueOf(num++);
        this.name = name;
//        this.symbolSize = 1;
        this.value = 1;
        this.year = year;
    }

//    @Override
//    public int compareTo(KeyWordSequenceNode o) {
//        return this.year-o.year;
//    }


    @Override
    public String toString() {
        return "KeyWordSequenceNode{" +
                "value=" +value  +
                ", year=" + year +
                ", category=" + category +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", symbolSize=" + symbolSize +
                ", x=" + x +
                ", y=" + y +
                ", outDegree=" + outDegree +
                ", inDegree=" + inDegree +
                '}';
    }
}
