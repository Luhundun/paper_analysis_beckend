package com.example.backend.Service;

import com.example.backend.Objects.Link;
import com.example.backend.Objects.Node;
import com.example.backend.Objects.Paper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
* @author luning
* @description 针对表【paper】的数据库操作Service
* @createDate 2022-11-10 16:08:13
*/
public interface PaperService extends IService<Paper> {

    List<Paper> getPapers() throws IOException;

    List<Node> getNodesForInstitutes(List<Paper> papers);

    List<Link> getLinks(List<Paper> papers, List<Node> nodes);

    String splitNodesByUnionAndGenJson(List<Node> nodes, List<Link> links, int limit);
}
