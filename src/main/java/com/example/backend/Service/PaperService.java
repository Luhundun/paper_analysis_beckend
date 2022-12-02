package com.example.backend.Service;

import com.example.backend.Objects.Link;
import com.example.backend.Objects.Node;
import com.example.backend.Objects.Paper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;
import java.util.List;

/**
* @author luning
* @description 针对表【paper】的数据库操作Service
* @createDate 2022-11-10 16:08:13
*/
public interface PaperService extends IService<Paper> {

    List<Paper> getPapers() throws IOException;

    List<Paper> getPapers(String rawPapers);

    List<Node> getNodesByType(List<Paper> papers, String type);
//    List<Node> getNodesForInstitutes(List<Paper> papers);
//
//    ArrayList<Node> getNodesForKeyword(List<Paper> papers);

    void adjustNodeSizeWithABaseValue(List<Node> nodes, int base);

    List<Link> getLinksByType(List<Paper> papers, List<Node> nodes, String type);

    String splitNodesByUnionAndGenJson(List<Node> nodes, List<Link> links, int limit);
}
