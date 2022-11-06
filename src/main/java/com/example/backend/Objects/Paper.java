package com.example.backend.Objects;

import lombok.Data;

import java.util.ArrayList;

/**
 * @ClassName: Paper
 * @Description:
 * @Author: luning
 * @Date: 2022/11/1 15:23
 * @Version: v1.0
 */
@Data
public class Paper {
    public static int num=0;
    private int id;
    private String name;
    private String[] keywords;
    private int year;

    public Paper(String[] keywords, int year) {
        this.id=num++;
        this.keywords = keywords;
        this.year = year;
    }


}
