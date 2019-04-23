import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class 正则化RBF {
	double w矩阵[][];
	double beta矩阵[][];
	double 中心向量组[][];
	
	void 初始化(double 训练集示例向量组[][],int 输出神经元个数){
		w矩阵=new double[训练集示例向量组.length][输出神经元个数];
		beta矩阵=new double[训练集示例向量组.length][输出神经元个数];
		中心向量组=训练集示例向量组;
		for(int i=0;i<训练集示例向量组.length;i++){
			for(int j=0;j<输出神经元个数;j++){
				w矩阵[i][j]=Math.random();
				beta矩阵[i][j]=Math.random();
			}
		}
	}
	
	double 计算正确率(double 预测结果矩阵[][],double 标准结果矩阵[][],boolean 二值化){
		double 正确率=0;
		for(int i=0;i<预测结果矩阵.length;i++){
			double 一个结果正确率=1;
			for(int j=0;j<预测结果矩阵[0].length;j++){
				if(二值化){
					if(标准结果矩阵[i][j]!=(预测结果矩阵[i][j]>0.5?1:0))
						一个结果正确率=0;
				}else{
					if(标准结果矩阵[i][j]!=预测结果矩阵[i][j])
						一个结果正确率=0;
				}
			}
			正确率+=一个结果正确率;
		}
		return 正确率/预测结果矩阵.length;
	}
	
	double 计算平方欧氏距离(double 向量1[],double 向量2[]){
		double 平方欧氏距离=0;
		for(int i=0;i<向量1.length;i++){
			平方欧氏距离+=Math.pow(向量1[i]-向量2[i],2);
		}
		return 平方欧氏距离;
	}
	
	double 高斯径向基函数(double 向量1[],double 向量2[],double beta){
		double 标量结果=计算平方欧氏距离(向量1,向量2);
		标量结果=Math.pow(Math.E, -beta*标量结果);
		return 标量结果;
	}
	
	double[] 计算输出向量(double 输入向量[]){
		double 输出向量[] = new double[w矩阵[0].length];
		for(int i=0;i<w矩阵[0].length;i++){
			输出向量[i]=0;
			for(int j=0;j<w矩阵.length;j++){
				输出向量[i]+=w矩阵[j][i]*高斯径向基函数(输入向量,中心向量组[j],beta矩阵[j][i]);
			}
			输出向量[i]=输出向量[i]/w矩阵.length; //归一化
		}
		return 输出向量;
	}
	
	void 开始训练(double 训练集示例向量组[][],double 训练集标记向量组[][],int 迭代次数,double 学习率){
		初始化(训练集示例向量组,训练集标记向量组[0].length);
		for(int i=0;i<迭代次数;i++){
			for(int j=0;j<训练集示例向量组.length;j++){ //训练每个输入向量
				double 输出向量[]=计算输出向量(训练集示例向量组[j]);
				for(int k=0;k<输出向量.length;k++){ //训练每个输出神经元
					for(int p=0;p<w矩阵.length;p++){ //针对每个隐层神经元修改参数
						double 德尔塔w=学习率*(训练集标记向量组[j][k]-输出向量[k])*
								高斯径向基函数(训练集示例向量组[j],中心向量组[p],beta矩阵[p][k]);
						double 德尔塔beta=学习率*(输出向量[k]-训练集标记向量组[j][k])*w矩阵[p][k]*计算平方欧氏距离(训练集示例向量组[j],中心向量组[p])
								*高斯径向基函数(训练集示例向量组[j],中心向量组[p],beta矩阵[p][k]);
						w矩阵[p][k]+=德尔塔w;
						beta矩阵[p][k]+=德尔塔beta;
					}
				}
			}
		}
	}
	
	double 验证测试集正确率(double 测试集示例向量组[][],double 测试集标记向量组[][],boolean 二值化){
		double 正确率=0;
		double 测试集预测向量组[][]=new double[测试集标记向量组.length][测试集标记向量组[0].length];
		for(int i=0;i<测试集示例向量组.length;i++){
			测试集预测向量组[i]=计算输出向量(测试集示例向量组[i]);
		}
		正确率=计算正确率(测试集预测向量组,测试集标记向量组,二值化);
		return 正确率;
	}
	
	ArrayList<double[][]> 读取数据集(String 数据集地址,int 输入神经元个数) throws IOException{
		ArrayList<double[][]> 数据集=new ArrayList<double[][]>();
		double 示例向量组[][]=null;
		double 标记向量组[][]=null;
		ArrayList<double[]> 示例向量组x=new ArrayList<double[]>();
		ArrayList<double[]> 标记向量组x=new ArrayList<double[]>();
		BufferedReader 读取=new BufferedReader(new FileReader(数据集地址));
		String 一行=null;
		while((一行=读取.readLine())!=null){
			String 向量[]=一行.split(",");
			double 示例向量[]=new double[输入神经元个数];
			double 标记向量[]=new double[向量.length-输入神经元个数];
			for(int i=0;i<向量.length;i++){
				if(i<输入神经元个数)
					示例向量[i]=Double.valueOf(向量[i]);
				else
					标记向量[i-输入神经元个数]=Double.valueOf(向量[i]);
			}
			示例向量组x.add(示例向量);
			标记向量组x.add(标记向量);
		}
		示例向量组=new double[示例向量组x.size()][输入神经元个数];
		标记向量组=new double[标记向量组x.size()][标记向量组x.get(0).length];
		for(int i=0;i<示例向量组x.size();i++){
			示例向量组[i]=示例向量组x.get(i);
			标记向量组[i]=标记向量组x.get(i);
		}
		数据集.add(示例向量组);
		数据集.add(标记向量组);
		读取.close();
		return 数据集;
	}
	
	public static void main(String arg[]) throws IOException{
		String 训练集地址="D:\\data\\code\\vscode\\irisDataExperiment\\vector and norm\\trainSet.txt";
		String 测试集地址="D:\\data\\code\\vscode\\irisDataExperiment\\vector and norm\\testSet.txt";
		int 迭代次数=40;
		double 学习率=0.1;
		
		正则化RBF 正则化RBF_示例=new 正则化RBF();
		ArrayList<double[][]> 训练集=正则化RBF_示例.读取数据集(训练集地址,4);
		ArrayList<double[][]> 测试集=正则化RBF_示例.读取数据集(测试集地址,4);
		正则化RBF_示例.开始训练(训练集.get(0), 训练集.get(1), 迭代次数, 学习率);
		double 正确率=正则化RBF_示例.验证测试集正确率(测试集.get(0), 测试集.get(1),true);
		System.out.println(正确率);
	}
}