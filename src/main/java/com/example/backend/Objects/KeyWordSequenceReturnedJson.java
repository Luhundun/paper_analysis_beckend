package com.example.backend.Objects;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.ArrayList;

/**
 * @ClassName: KeyWordSequenceReturnedJson
 * @Description:
 * @Author: luning
 * @Date: 2022/11/2 10:17
 * @Version: v1.0
 */
@Data
public class KeyWordSequenceReturnedJson {
    @JSONField(name = "nodes")
    private ArrayList<Node> nodes;
    @JSONField(name = "links")
    private ArrayList<Link> links;

    @JSONField(name = "categories")
    private ArrayList<Category> categories;

//    private ArrayList<Link> categories;

    public KeyWordSequenceReturnedJson(ArrayList<Node> nodes, ArrayList<Link> links, ArrayList<Category> categories) {
        this.nodes=nodes;
        this.links=links;
        this.categories=categories;

    }
}
