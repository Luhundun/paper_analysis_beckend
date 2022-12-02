package com.example.backend.Objects;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: SearchCondition
 * @Description:
 * @Author: luning
 * @Date: 2022/11/22 16:28
 * @Version: v1.0
 */
@Data
public class SearchCondition {
    String state;
    String message;
    List<Title> titles;
    List<Author> authors;
    List<KeyWord> keyWords;
    List<Institute> institutes;
    List<Abstract> abstracts;
    List<Field> fields;
    List<Publish> publishes;
    String language;
//    boolean isCore;
    //题目
    @Data
    public static class Title{
        String name;
        String condition;
        public Title(String name, String condition) {
            this.name = name;
            this.condition = condition;
        }
        public Title(String name) {
            this.name = name;
            this.condition = "AND";
        }
    }

    //作者
    @Data
    public static class Author{
        String name;
        String condition;
        public Author(String name, String condition) {
            this.name = name;
            this.condition = condition;
        }
        public Author(String name) {
            this.name = name;
            this.condition = "AND";
        }
    }

    //关键字
    @Data
    public static class KeyWord {
        String name;
        String condition;
        public KeyWord(String name, String condition) {
            this.name = name;
            this.condition = condition;
        }
        public KeyWord(String name) {
            this.name = name;
            this.condition = "AND";
        }
    }

    //摘要
    @Data
    public static class Abstract {
        String name;
        String condition;
        public Abstract(String name, String condition) {
            this.name = name;
            this.condition = condition;
        }
        public Abstract(String name) {
            this.name = name;
            this.condition = "AND";
        }
    }

    //摘要
    @Data
    public static class Institute {
        String name;
        String condition;
        public Institute(String name, String condition) {
            this.name = name;
            this.condition = condition;
        }
        public Institute(String name) {
            this.name = name;
            this.condition = "AND";
        }
    }

    //领域
    @Data
    public static class Field {
        String name;
        String condition;
        public Field(String name, String condition) {
            this.name = name;
            this.condition = condition;
        }
        public Field(String name) {
            this.name = name;
            this.condition = "AND";
        }
    }

    //出版
    @Data
    public static class Publish {
        String name;
        String condition;
        public Publish(String name, String condition) {
            this.name = name;
            this.condition = condition;
        }
        public Publish(String name) {
            this.name = name;
            this.condition = "AND";
        }
    }


    public static void main(String[] args) {
        System.out.println(new Date());

        Date date = new Date();
        System.out.println();
    }
}
