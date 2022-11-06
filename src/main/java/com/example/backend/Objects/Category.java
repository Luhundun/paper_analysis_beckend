package com.example.backend.Objects;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @ClassName: Category
 * @Description:
 * @Author: luning
 * @Date: 2022/11/2 14:41
 * @Version: v1.0
 */
@Data
public class Category {

    @JSONField(name = "name")
    private String name;

    public Category(String name){
        this.name=name;
    }
}
