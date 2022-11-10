package com.example.backend.mapper;

import com.example.backend.Objects.Paper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* @author luning
* @description 针对表【paper】的数据库操作Mapper
* @createDate 2022-11-10 16:08:13
* @Entity com.example.backend.Objects.Paper
*/
@Repository
public interface PaperMapper extends BaseMapper<Paper> {

}




