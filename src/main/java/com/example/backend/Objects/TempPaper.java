package com.example.backend.Objects;

import lombok.Data;

/**
 * @ClassName: TempPaper
 * @Description:
 * @Author: luning
 * @Date: 2022/11/1 15:23
 * @Version: v1.0
 */
@Data
public class TempPaper {
    public static int num=0;
    private int id;
    private String name;
    private String[] keywords;
    private int year;

    public TempPaper(String[] keywords, int year) {
        this.id=num++;
        this.keywords = keywords;
        this.year = year;
    }


}
