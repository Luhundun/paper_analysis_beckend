package com.example.backend.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.Objects.Paper;
import com.example.backend.Objects.TempPaper;
import com.example.backend.Service.PaperService;
import com.example.backend.mapper.PaperMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
* @author luning
* @description 针对表【paper】的数据库操作Service实现
* @createDate 2022-11-10 16:08:13
*/
@Service
public class PaperServiceImpl extends ServiceImpl<PaperMapper, Paper>
    implements PaperService{

    @Autowired
    PaperMapper paperMapper;

    public ArrayList<TempPaper> getPapers(String path) throws IOException {
        ArrayList<TempPaper> list = new ArrayList<>();
        BufferedReader reader = Files.newBufferedReader(Paths.get(path));
        String nextText = reader.readLine();
        while (nextText!=null){
            //去掉无意义的标题
            //读取关键字
            String[] words=reader.readLine().split(";");
            //去掉开头的k1
            words[0]=words[0].substring(3);
            //读取年份
            int year = Integer.parseInt(reader.readLine().substring(3));
            //加入paper列表
            TempPaper temp = new TempPaper(words,year);
            list.add(temp);
            //去空行
            nextText = reader.readLine();
            //读下一行
            nextText = reader.readLine();
        }

        reader.close();
        return list;
    }

    public void putPaperIntoDatabase(){

    }
}




