package com.example.backend.utils;

import com.example.backend.Objects.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: UnionFind
 * @Description:
 * @Author: luning
 * @Date: 2022/11/9 16:21
 * @Version: v1.0
 */
//并查集工具
public class UnionFind {
    // 用 Map 在存储并查集，表达的含义是 key 的父节点是 value
    private Map<Node,Node> father;

    // 0.构造函数初始化ss
    public UnionFind(ArrayList<Node> nodes) {
        father = new HashMap<Node,Node>();
        for (int i = 0; i < nodes.size(); i++) {
            father.put(nodes.get(i), null);
        }
    }

    public void add(Node node) {	// 根节点的父节点为null
    // 1.添加：初始加入时，每个元素都是一个独立的集合，因此
        if (!father.containsKey(node)) {
            father.put(node, null);
        }
    }

    // 2.查找：反复查找父亲节点。
    public Node findFather(Node node) {
        Node root = node;	// 寻找x祖先节点保存到root中

        while(father.get(root) != null){
            root = father.get(root);
        }


        while(node != root){	// 路径压缩，把x到root上所有节点都挂到root下面
            Node original_father = father.get(node);	// 保存原来的父节点
            father.put(node,root);		// 当前节点挂到根节点下面
            node = original_father;	// x赋值为原来的父节点继续执行刚刚的操作
        }

        return root;
    }

    // 3.合并：把两个集合合并为一个，只需要把其中一个集合的根节点挂到另一个集合的根节点下方
    public void union(Node x, Node y) {	// x的集合和y的集合合并
        Node rootX = findFather(x);
        Node rootY = findFather(y);

        if (rootX != rootY){	// 节点联通只需要一个共同祖先，无所谓谁是根节点
            father.put(rootX,rootY);
        }
    }

    // 4.判断：判断两个元素是否同属一个集合
    public boolean isConnected(Node x, Node y) {
        return findFather(x) == findFather(y);
    }
}

