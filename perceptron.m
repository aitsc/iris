global x_input
global y_output
global w_matrix

iterations=100; % ��������
learningRate=0.1; % ѧϰ��
x_input=4; % ������Ԫ����
y_output=2; % �����Ԫ����
trainDataAddress='D:\\data\\code\\vscode\\irisDataExperiment\\vector and norm\\trainSet.txt';
testDataAddress='D:\\data\\code\\vscode\\irisDataExperiment\\vector and norm\\testSet.txt';
w_matrix=rand(x_input+1,y_output); % Ȩֵ�����ѳ�ʼ��

[trainData_x,trainData_y]=readFile(trainDataAddress);
[testData_x,testData_y]=readFile(testDataAddress);
computerWeight(trainData_x,trainData_y,iterations,learningRate);
accuracy=experimentResult(testData_x,testData_y)


function [x_vectorGroup,y_vectorGroup]=readFile(address) % ��ȡ���ݵ���������ֵ����ʾ����
openFile=fopen(address,'r');
vectorGroup=fscanf(openFile,'%f,%f,%f,%f,%f,%f',[6,inf]);
fclose(openFile);
vectorGroup=vectorGroup';
row=size(vectorGroup,1);
x_vectorGroup=[vectorGroup(:,1:4),ones(row,1)];
y_vectorGroup=vectorGroup(:,5:6);
end

function y=activationFunction(x) % ���������Ծ����
if x>=0.5
    y=1;
else
    y=0;
end
end

function y=computerOutputVecotr(x) % �����������
global x_input
global y_output
global w_matrix
y=zeros(1,y_output);
for j=1:y_output
    for i=1:x_input+1
        y(j)=y(j)+w_matrix(i,j)*x(i);
    end
    y(j)=activationFunction(y(j));
end
end

function accuracy=computerAccuracy(vectorGroup1,vectorGroup2,binary) % ������ȷ��
accuracy=0;
for row=1:size(vectorGroup1,1)
    accuracy_temporary=1;
    for column=1:size(vectorGroup1,2)
        if binary>0 % �Ƿ��ֵ��
            if activationFunction(vectorGroup1(row,column))~=activationFunction(vectorGroup2(row,column))
                accuracy_temporary=0;
            end
        else
            if vectorGroup1(row,column)~=vectorGroup2(row,column)
                accuracy_temporary=0;
            end
        end
    end
    accuracy=accuracy+accuracy_temporary;
end
accuracy=accuracy/size(vectorGroup1,1);
end

function computerWeight(x_inputSet,y_outputSet,iterations,learningRate) % ��ʼѵ��
global w_matrix
for n=1:iterations
    for num=1:size(x_inputSet,1)
        y_o=computerOutputVecotr(x_inputSet(num,:)); % �õ��������
        for y=1:length(y_o)
            for x=1:size(x_inputSet,2)
                w_matrix(x,y)=w_matrix(x,y)+learningRate*(y_outputSet(num,y)-y_o(y))*x_inputSet(num,x);
            end
        end
    end
end
end

function accuracy=experimentResult(testVectorGroup_in,testVectorGroup_out) % ����Ԥ����Լ���׼ȷ��
testVectorGroup_Pout=[];
for num=1:size(testVectorGroup_in,1)
    testVectorGroup_Pout=[testVectorGroup_Pout;computerOutputVecotr(testVectorGroup_in(num,:))];
end
accuracy=computerAccuracy(testVectorGroup_Pout,testVectorGroup_out,1);
end