package com.example.backend.utils;

import com.example.backend.Objects.Category;
import com.example.backend.Objects.Link;
import com.example.backend.Objects.Node;
import lombok.Data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class MarkovClustering{

	//连接数
	private int numberOfLink;
	//结点数
	private int numberOfNodes;
	//邻接矩阵
	private int adjMatrix[][];
	//转移矩阵
	private double transMatrix[][];
	//id映射
	private Map<String,Integer> mapping;
	//id反向映射
	private String[] reverseMapping;
	private int power = 2;
	private int inflate = 2;

	public MarkovClustering(List<Node> nodes, List<Link> links, int power, int inflate) {
		this.power = power;
		this.inflate = inflate;
		numberOfLink = links.size();
		numberOfNodes = nodes.size();
		transMatrix = new double[numberOfNodes][numberOfNodes];
		adjMatrix = new int[numberOfNodes][numberOfNodes];
		mapping = new HashMap<>();
		reverseMapping = new String[numberOfNodes];
		//建立映射矩阵(因为Node中的id并不是从0开始的)
		for (int i=0;i<numberOfNodes;i++){
			Node node = nodes.get(i);
			mapping.put(node.getId(),i);
			reverseMapping[i]=node.getId();
		}
		for(Link link:links){
			adjMatrix[mapping.get(link.getSource())][mapping.get(link.getTarget())]=link.getValue();
			adjMatrix[mapping.get(link.getTarget())][mapping.get(link.getSource())]=link.getValue();
		}

	}

	
	//自身+1
	void addSelfLoop()
	{
		for(int i=0;i<numberOfNodes;i++)
		{
			for(int j=0;j<numberOfNodes;j++)
			{
				adjMatrix[i][i] = 1;
			}
		}
	}

	/**
	 * @Description: 构造转移矩阵
	 * @param: []
	 * @return: void
	 * @auther: Lu Ning
	 * @date: 2022/12/6 15:37
	 */
	void constructTransitionMatrix()
	{
		double[] columnSum = new double[numberOfNodes];
		//计算每个点到其他点所有边的值的和
		for(int row=0;row<numberOfNodes;row++)
		{
			for(int col=0;col<numberOfNodes;col++)
			{
				columnSum[col] += adjMatrix[row][col];
			}
		}

		//计算转移概率
		for(int row=0; row<numberOfNodes; row++)
		{
			for(int col=0;col<numberOfNodes; col++)
			{
				transMatrix[row][col] = (double)adjMatrix[row][col]/columnSum[col];
			} 
		}
	}
	
	//聚类过程
	void mcl() throws IOException
	{
		//Markov Cluster Algorithm
		int iteration = 1;
		System.out.println("Iteration "+iteration);
		transMatrix = expand();
		inflate();
		iteration++;
		//若不收敛 继续
		while(!checkConvergence()){
			System.out.println("Iteration "+iteration);
			transMatrix = expand();
			inflate();
			iteration++;
		}
		//System.out.println("Convergence Reached. The Matrix is: ");
		//printMatrix(transMatrix);
//		System.out.println("The number of clusters are: "+findClusters());
	}
	
	/**
	 * @Description: 判断是否收敛 
	 * @param: []
	 * @return: boolean
	 * @auther: Lu Ning
	 * @date: 2022/12/6 16:55
	 */
	boolean checkConvergence(){
		double prev = -1;
		for(int j = 0;j<numberOfNodes;j++){
			for(int i = 0;i<numberOfNodes;i++){
				if(transMatrix[i][j]!=0){
					prev = transMatrix[i][j];
					break;
				}
			}
			for(int i = 0;i<numberOfNodes;i++){
				if(transMatrix[i][j]!=0){
					if(transMatrix[i][j]!=prev)
						return false;
				}
			}
		}
		return true;
	}
	/**
	 * @Description: 求矩阵平方 
	 * @param: []
	 * @return: double[][]
	 * @auther: Lu Ning
	 * @date: 2022/12/6 16:56
	 */
	double[][] expand(){
		double[][] matrix = new double[numberOfNodes][numberOfNodes];
		int  p = power;
		while(p>1){
			for(int i = 0;i<numberOfNodes;i++){
				for(int j = 0;j<numberOfNodes;j++){
					//int sum = 0;
					for(int k = 0;k<numberOfNodes;k++){
						matrix[i][j] += (transMatrix[i][k]*transMatrix[k][j]); 
					}
					//transMatrix[i][j] = sum;
				}
			}
			p--;
		}
		return matrix;
	}
	/**
	 * @Description:  矩阵元素平方后标准化
	 * @param: 
	 * @return: 
	 * @auther: Lu Ning
	 * @date: 2022/12/6 16:56
	 */
	void inflate(){
		double[] sum = new double[numberOfNodes]; 
		for(int j = 0;j<numberOfNodes;j++){
			for(int i = 0;i<numberOfNodes;i++){
				sum[j]+=Math.pow(transMatrix[i][j], inflate);
			}
		}
		for(int j = 0;j<numberOfNodes;j++){
			for(int i = 0;i<numberOfNodes;i++){
				transMatrix[i][j] = Math.pow(transMatrix[i][j],inflate)/sum[j];
			}
		}
	}
	
	/**
	 * @Description: 将结果写回,并返回目录
	 * @param: [nodes]
	 * @return: List<Category>
	 * @auther: Lu Ning
	 * @date: 2022/12/6 20:40
	 */
	List<Category> findClusters(List<Node> nodes) throws IOException{
		File f = new File("src/main/resources/static/attweb_out.txt");
		if(!f.exists())
			f.createNewFile();
		FileOutputStream fs = new FileOutputStream(f);
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fs));
		int count = -1;

		//为了加速,决定点聚类的同时创建目录
		Node tempMaxNode = new Node();
		List<Category> categories= new ArrayList<>();
		for(int i = 0;i<numberOfNodes;i++){
			for(int j = 0;j<numberOfNodes;j++){
				if(transMatrix[i][j]!=0){
					count++;
					tempMaxNode = nodes.get(j);
					Category category = new Category(nodes.get(j).getName());
					categories.add(category);
					break;
				}
			}
			//取这一聚类中值最大的作为目录名称
			for(int j = 0;j<numberOfNodes;j++){
				if(transMatrix[i][j]!=0){
					out.write(reverseMapping[j]+"\t"+Integer.toString(count)+"\n");
					nodes.get(j).setCategory(count);
					if(nodes.get(j).getValue()>tempMaxNode.getValue()){
						tempMaxNode = nodes.get(j);
						//修改目录名
						categories.get(count).setName(tempMaxNode.getName());
					}
				}
			}
		}
		out.close();
		//生成目录
		return categories;
	}

	/**
	 * @Description: 整个马尔科夫聚类过程,并把结果写回node
	 * @param: [nodes, links, power, inflate]
	 * @return: List<Category>
	 * @auther: Lu Ning
	 * @date: 2022/12/6 17:01
	 */
	public static List<Category> getResult(List<Node> nodes, List<Link> links, int power, int inflate) throws IOException{
		MarkovClustering mcl = new MarkovClustering(nodes,links,power,inflate);
		mcl.addSelfLoop();
		mcl.constructTransitionMatrix();
		mcl.mcl();
		return mcl.findClusters(nodes);
	}

	void printMatrix(int[][] matrix)
	{
		for(int i=0;i<numberOfNodes;i++)
		{
			System.out.println();
			for(int j=0;j<numberOfNodes;j++)
			{
				System.out.print(matrix[i][j]+" ");
			}
		}
		System.out.println();
	}

	void printMatrix(double[][] matrix)
	{
		for(int i=0;i<numberOfNodes;i++)
		{
			System.out.println();
			for(int j=0;j<numberOfNodes;j++)
			{
				System.out.print(matrix[i][j]+" ");
			}
		}
		System.out.println();
	}

}