package com.example.backend.Objects;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: ReturnedJson
 * @Description:
 * @Author: luning
 * @Date: 2022/11/2 10:17
 * @Version: v1.0
 */
@Data
public class ReturnedJson {
    @JSONField(name = "nodes")
    private List<Node> nodes;
    @JSONField(name = "links")
    private List<Link> links;

    @JSONField(name = "categories")
    private List<Category> categories;

//    private ArrayList<Link> categories;

    public ReturnedJson(List<Node> nodes, List<Link> links, List<Category> categories) {
        this.nodes=nodes;
        this.links=links;
        this.categories=categories;

    }
}
