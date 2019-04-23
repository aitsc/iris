#include <iostream>
#include <string.h>
using namespace std;

struct irisData{
    double instance_vec[4]={0};
    char label[512]={0};
    double label_vec[2]={0};
};

irisData* readDataFile(string fileAddress,int num=150){
    irisData *irisData_150=new irisData[150];
    FILE *read=fopen(fileAddress.c_str(),"r");
    for(int i=0;i<num;i++){
        fscanf(read,"%lf,%lf,%lf,%lf,%s",
               &irisData_150[i].instance_vec[0],&irisData_150[i].instance_vec[1],
               &irisData_150[i].instance_vec[2],&irisData_150[i].instance_vec[3],irisData_150[i].label);
    }
    fclose(read);
    return irisData_150;
}

int main() {
    string irisDataAddress="D:\\data\\code\\vscode\\irisDataExperiment\\raw data\\iris.data.txt";
    string trainSetOutAddress="D:\\data\\code\\vscode\\irisDataExperiment\\vector and norm\\trainSet.txt";
    string testSetOutAddress="D:\\data\\code\\vscode\\irisDataExperiment\\vector and norm\\testSet.txt";
    double trainSetRatio=0.8;
    int num=150;
    char label_enum[3][256]={"Iris-setosa","Iris-versicolor","Iris-virginica"};

    //输入：iris150条数据、trainSetRatio
    irisData *irisData_150=readDataFile(irisDataAddress,num);

    //对前4维的每一维寻找最大值，并将最后1维的label映射为向量
    double perDimInstanceMax[4]={0};
    for(int i=0;i<num;i++){
        if(irisData_150[i].instance_vec[0]>perDimInstanceMax[0])
            perDimInstanceMax[0]=irisData_150[i].instance_vec[0];
        if(irisData_150[i].instance_vec[1]>perDimInstanceMax[1])
            perDimInstanceMax[1]=irisData_150[i].instance_vec[1];
        if(irisData_150[i].instance_vec[2]>perDimInstanceMax[2])
            perDimInstanceMax[2]=irisData_150[i].instance_vec[2];
        if(irisData_150[i].instance_vec[3]>perDimInstanceMax[3])
            perDimInstanceMax[3]=irisData_150[i].instance_vec[3];
        if(strcmp(irisData_150[i].label,label_enum[0])==0) {
            irisData_150[i].label_vec[0] = 0;
            irisData_150[i].label_vec[1] = 0;
        } else if(strcmp(irisData_150[i].label,label_enum[1])==0) {
            irisData_150[i].label_vec[0] = 1;
            irisData_150[i].label_vec[1] = 1;
        } else {
            irisData_150[i].label_vec[0] = 0;
            irisData_150[i].label_vec[1] = 1;
        }
    }

    //用每一维的最大值得到每条数据每一维的归一化表示
    for(int i=0;i<num;i++){
        irisData_150[i].instance_vec[0]=irisData_150[i].instance_vec[0]/perDimInstanceMax[0];
        irisData_150[i].instance_vec[1]=irisData_150[i].instance_vec[1]/perDimInstanceMax[1];
        irisData_150[i].instance_vec[2]=irisData_150[i].instance_vec[2]/perDimInstanceMax[2];
        irisData_150[i].instance_vec[3]=irisData_150[i].instance_vec[3]/perDimInstanceMax[3];
    }

    //按trainSetRatio把每个label类别对应的样例分为2份，并输出到2个文件中
    int trainSetNum=(int)(trainSetRatio*num/3);
    int perClassTestDatasetNum[3]={0};
    FILE *writeTrain=fopen(trainSetOutAddress.c_str(),"w");
    FILE *writeTest=fopen(testSetOutAddress.c_str(),"w");
    for(int i=0;i<num;i++){
        if(strcmp(irisData_150[i].label,label_enum[0])==0){
            if(perClassTestDatasetNum[0]<trainSetNum){
                perClassTestDatasetNum[0]++;
                fprintf(writeTrain,"%lf,%lf,%lf,%lf,%lf,%lf\n",
                        irisData_150[i].instance_vec[0],irisData_150[i].instance_vec[1],
                        irisData_150[i].instance_vec[2],irisData_150[i].instance_vec[3],
                        irisData_150[i].label_vec[0],irisData_150[i].label_vec[1]);
            } else{
                fprintf(writeTest,"%lf,%lf,%lf,%lf,%lf,%lf\n",
                        irisData_150[i].instance_vec[0],irisData_150[i].instance_vec[1],
                        irisData_150[i].instance_vec[2],irisData_150[i].instance_vec[3],
                        irisData_150[i].label_vec[0],irisData_150[i].label_vec[1]);
            }
        }else if(strcmp(irisData_150[i].label,label_enum[1])==0){
            if(perClassTestDatasetNum[1]<trainSetNum){
                perClassTestDatasetNum[1]++;
                fprintf(writeTrain,"%lf,%lf,%lf,%lf,%lf,%lf\n",
                        irisData_150[i].instance_vec[0],irisData_150[i].instance_vec[1],
                        irisData_150[i].instance_vec[2],irisData_150[i].instance_vec[3],
                        irisData_150[i].label_vec[0],irisData_150[i].label_vec[1]);
            } else{
                fprintf(writeTest,"%lf,%lf,%lf,%lf,%lf,%lf\n",
                        irisData_150[i].instance_vec[0],irisData_150[i].instance_vec[1],
                        irisData_150[i].instance_vec[2],irisData_150[i].instance_vec[3],
                        irisData_150[i].label_vec[0],irisData_150[i].label_vec[1]);
            }
        }else{
            if(perClassTestDatasetNum[2]<trainSetNum){
                perClassTestDatasetNum[2]++;
                fprintf(writeTrain,"%lf,%lf,%lf,%lf,%lf,%lf\n",
                        irisData_150[i].instance_vec[0],irisData_150[i].instance_vec[1],
                        irisData_150[i].instance_vec[2],irisData_150[i].instance_vec[3],
                        irisData_150[i].label_vec[0],irisData_150[i].label_vec[1]);
            } else{
                fprintf(writeTest,"%lf,%lf,%lf,%lf,%lf,%lf\n",
                        irisData_150[i].instance_vec[0],irisData_150[i].instance_vec[1],
                        irisData_150[i].instance_vec[2],irisData_150[i].instance_vec[3],
                        irisData_150[i].label_vec[0],irisData_150[i].label_vec[1]);
            }
        }
    }
    fclose(writeTrain);
    fclose(writeTest);
    printf("the number of train set: %d, the number of test set: %d",trainSetNum*3,num-trainSetNum*3);
    return 0;
}