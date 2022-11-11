package com.example.backend.Service.impl;

import com.alibaba.fastjson.JSON;
import com.example.backend.Objects.*;
import com.example.backend.Service.KeyWordSequenceService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.example.backend.utils.GraphUtil.isCollide;

/**
 * @ClassName: KeyWordSequenceServiceImpl
 * @Description:
 * @Author: luning
 * @Date: 2022/11/1 15:00
 * @Version: v1.0
 */
@Service
public class KeyWordSequenceServiceImpl implements KeyWordSequenceService {

    /**
     * @Description: 匹配文献列表
     * @param: [path]
     * @return: java.util.ArrayList<com.example.backend.Objects.TempPaper>
     * @auther: Lu Ning
     * @date: 2022/11/1 15:41
     */
    @Override
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

    //通过限制发表年份来筛选文章
    @Override
    public ArrayList<TempPaper> selectPaperByYear(ArrayList<TempPaper> papers, int begin, int end) {
        for (int i=0;i< papers.size();i++){
            TempPaper paper = papers.get(i);
            //去除不符合年份的文章，i--防止报错
            if(paper.getYear()<begin||paper.getYear()>end){
                papers.remove(i);
                i--;
            }
        }
        return papers;
    }

    //通过限制出现次数来筛选关键词节点,并且去掉link中的source节点
    @Override
    public List<Node> selectNodeByMinValue(List<Node> nodes, int minvalue) {
        for (int i=0;i< nodes.size();i++){
            Node node = nodes.get(i);
            if(node.getSymbolSize()<minvalue){
                nodes.remove(i);
//                //二分查找可以优化加速
//                for(int j=0;j<links.size();j++){
//                    Link link = links.get(j);
//                    if(link.getSource()==node.getId()||link.getTarget()==node.getId()){
//                        links.remove(j);
//                        j--;
//                    }
//                }
                i--;
            }
        }
        return nodes;
    }

    /**
     * @Description: 从papaer列表生成关键字列表
     * @param: [papers]
     * @return: java.util.ArrayList<com.example.backend.Objects.KeyWordSequenceNode>
     * @auther: Lu Ning
     * @date: 2022/11/1 16:14
     */
    @Override
    public ArrayList<Node> getNodes(ArrayList<TempPaper> papers){

        //按照文章发表年份预排序
        papers.sort((a,b)->
            a.getYear()-b.getYear()
        );
        ArrayList<Node> nodes = new ArrayList<>();
        for (TempPaper paper:papers){
            for (String keyword:paper.getKeywords()){
                //可优化时间复杂度
                //若关键字存在则增加大小
                boolean isExist = false;
                for(int i=0;i<nodes.size();i++){
                    KeyWordSequenceNode node=(KeyWordSequenceNode) (nodes.get(i));
                    if(node.getName().equals(keyword)){
                        isExist = true;
//                        if(node.getYear()>paper.getYear()){
//                            node.setYear(paper.getYear());
//                        }
                        node.setValue(node.getValue()+1);
                        break;
                    }
                }
                //若关键字不存在则创建
                if (!isExist){
                    KeyWordSequenceNode temp = new KeyWordSequenceNode(keyword,paper.getYear());
                    nodes.add(temp);
                }
            }
        }
        return nodes;
    }

    /**
     * @Description: 将出现在同一篇文章的两个关键词相连
     * @param: [papers, nodes]
     * @return: java.util.ArrayList<com.example.backend.Objects.Link>
     * @auther: Lu Ning
     * @date: 2022/11/1 16:30
     */
    @Override
    public ArrayList<Link> getLinks(ArrayList<TempPaper> papers, ArrayList<Node> nodes){
        ArrayList<Link> links = new ArrayList<>();
        for (TempPaper paper:papers){
            int linkLength = paper.getKeywords().length;
            //根据文章关键字获取关键字节点
            for(int i=0;i<linkLength-1;i++){
                KeyWordSequenceNode ni = (KeyWordSequenceNode)(Node.getInList(paper.getKeywords()[i],nodes));
                //节点被筛选掉则略过
                if(ni==null){
                    continue;
                }
                for (int j=i+1;j<linkLength;j++){
                    KeyWordSequenceNode nj = (KeyWordSequenceNode)(Node.getInList(paper.getKeywords()[j],nodes));
                    //节点被筛选掉则略过
                    if (nj==null){
                        continue;
                    }
                    //比较，年份小的为起点，大的为终点
                    Node sourceNode = ni.getYear()<nj.getYear()?ni:nj;
                    Node targetNode = ni.getYear()<nj.getYear()?nj:ni;
                    //检查是否已经有连线，有则加粗，无则创建
                    if(!Link.isExistAndAdjuestIt(sourceNode.getId(),targetNode.getId(),links)){
                        Link temp = new Link(sourceNode,targetNode);
                        links.add(temp);
                    }
                }
            }
        }
        return links;
    }


    @Override
    public void genarateXY(ArrayList<Node> nodes, int beginYear, int endYear){
        //根据年份信息判断出X轴被分割的块数
        int cols = endYear-beginYear+1;
        //年数间隔
        int gap = 1;
        //一个年份对应一个区间，默认情况下占画布50%的高，此外越后面的列越高，营造一种层次感
        //x、y轴范围-800——800。从左上角到右下角增长。
        double baseX=-800;
        double baseY=800;
        //列宽
        double widthX=1600/cols;
        //列中能出现圆的高度范围
        double hightY=400;
        //列宽和高度范围增长速度
        double growX=1600/cols;
        double growY=1200/cols;

        //调整每个node在图中的大小,原大小用value记录
        for(int i=0;i<nodes.size();i++){
            KeyWordSequenceNode node=(KeyWordSequenceNode) (nodes.get(i));
            double size = node.getSymbolSize();
            node.setValue(size);
            //用这个公式改变圆的大小
            size = Math.sqrt(size*10);
            //设置上限
            if(size>widthX/4){
                size=widthX/4;
            }
            node.setSymbolSize(size);
        }

        //遍历nodes的指针
        int pn=0;
        int length=nodes.size();
        //默认在这里nodes都是按年份排好序的,并且让size大的先生成位置
        nodes.sort((aa,bb)->{
            KeyWordSequenceNode a=(KeyWordSequenceNode) aa;
            KeyWordSequenceNode b=(KeyWordSequenceNode) bb;
            if(a.getYear()==b.getYear()){
                return (int) (b.getValue()-a.getValue());
            }else {
                return a.getYear()-b.getYear();
            }
        });
        for (int i=0;i<cols;i++){
            //这一列中生成的点要记录，保证圆不碰撞
            ArrayList<Double[]> points = new ArrayList<>();

            //目前还只是一年的gap，两年的需要改while内条件
            while (pn<length&&((KeyWordSequenceNode)(nodes.get(pn))).getYear()==beginYear+i*gap){
                KeyWordSequenceNode node = (KeyWordSequenceNode)nodes.get(pn);
                //size为圆的半径，与node大小有关
                double size=node.getSymbolSize();
//                double scope=widthX-2*size;
                //产生的随机位置，首先加上半径后不能越列的界

                double x,y;
                while (true){
                    boolean isCol=false;
                    //随机数生成正确的x，y坐标
                    x =random.nextDouble()*(widthX-2*size)+size+baseX;
                    y =-(random.nextDouble()*(hightY-2*size)+size)+baseY;
                    //与其他点进行对比，如果碰撞则重新生成位置
                    for (Double[] point:points){
                        if(isCollide(point,x,y,size)){
                            isCol=true;
                            break;
                        }
                    }
                    //正确生成无碰撞的位置
                    if(!isCol){
                        //加入到这一列的点列表
                        Double[] newPoint = {x,y,size};
                        points.add(newPoint);
                        //写回node
                        node.setX(x);
                        node.setY(y);
                        break;
                    }
                }

                //设置类目（用于echarts图）
                node.setCategory(i);
                pn++;
            }
            //这一列结束后，生成下一列的基础坐标
            baseX+=growX;
            baseY-=growY;
        }
    }

    //节点、link信息转成json
    @Override
    public String genJson(ArrayList<Node> nodes, ArrayList<Link> links, int beginYear, int endYear){
//        String nodeJson = JSON.toJSONString(nodes);
//        String linkJson = JSON.toJSONString(links);
//        JSON.
        ArrayList<Category> categories = new ArrayList<>();
        for(int i=beginYear;i<=endYear;i++){
            categories.add(new Category(String.valueOf(i)));
        }
        return JSON.toJSONString(new ReturnedJson(nodes,links,categories));
    }




    //测试部分
//    public static void main(String[] args) throws IOException {
//        ArrayList<TempPaper> papers = getPapers("src/main/resources/static/cnki_可解释.txt");
//
//        int beginYear=2017,endYear=2022;
//        int nodeMinValue=2;
//        selectPaperByYear(papers,beginYear,endYear);
//        ArrayList<KeyWordSequenceNode> nodes = getNodes(papers);
//        selectNodeByMinValue(nodes,nodeMinValue);
//        ArrayList<Link> links = getLinks(papers,nodes);
//        genarateXY(nodes,beginYear,endYear);
//        adjustLineWidth(links);
//        String json = genJson(nodes,links,beginYear,endYear);
//        FileWriter writer = new FileWriter("src/main/resources/static/out.json",false);
//        writer.write(json);
//        writer.close();
//        System.out.println(papers);
//        System.out.println(json);
//        System.out.println();
//    }



}
