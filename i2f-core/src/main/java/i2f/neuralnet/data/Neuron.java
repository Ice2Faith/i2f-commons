package i2f.neuralnet.data;


import i2f.neuralnet.activation.IActivationFunction;
import i2f.neuralnet.math.RandomGenerator;

/**
 * 神经元类
 */
public class Neuron {
    public double[] weights=new double[0];//权重
    public double[] inputs=new double[0];//输入
    public double output;//激活函数处理之后的输出值
    public double beforeActiveOutput;//激活函数处理之前的计算输出值
    public int inputsCount;//输入个数
    public double bias=1.0;//偏置
    public IActivationFunction activeFunc;//激活函数
    public Neuron(int inputsCount,IActivationFunction activeFunc){
        this.inputsCount=inputsCount;
        this.activeFunc=activeFunc;
    }

    protected void init(){
        this.weights=new double[inputsCount+1];//偏置也需要一个权重
        this.inputs=new double[inputsCount];

        for(int i=0;i<inputsCount+1;i+=1){
            double pwei= RandomGenerator.nextDouble();
            this.weights[i]=pwei;
        }
    }
    public void calc(){
        beforeActiveOutput=0.0;
        if(inputsCount>0){
            for(int i=0;i<=inputsCount;i+=1){
                beforeActiveOutput+=(i==inputsCount?bias:inputs[i])*weights[i];
            }
        }
        output=activeFunc.calc(beforeActiveOutput);

    }

    public void updateWeight(int i, double value){
        if(i>=0 && i<=inputsCount){
            weights[i]=value;
        }
    }

    public void deactivateBias(){
        this.bias=0.0;
    }

    public void activateBias(){
        this.bias=1.0;
    }

    public double calc(double[] inputs){
        double pBeforeActiveOutput=0.0;
        if(inputsCount>0){
            if(weights!=null){
                for(int i=0;i<=inputsCount;i++){
                    pBeforeActiveOutput+=(i==inputsCount?bias:inputs[i])*weights[i];
                }
            }
        }
        return activeFunc.calc(pBeforeActiveOutput);
    }

    public double[] calcBatch(double[][] batchInputs){
        double[] ret=new double[batchInputs.length];
        for(int i=0;i<batchInputs.length;i++){
            double pBeforeActiveOutput=0.0;
            for(int j=0;j<=inputsCount;j++){
                pBeforeActiveOutput+=(j==inputsCount?bias:batchInputs[i][j])*weights[j];
            }
            ret[i]=activeFunc.calc(pBeforeActiveOutput);
        }
        return ret;
    }

    public double derivative(double[] inputs){
        double pBeforeActiveOutput=0.0;
        if(inputsCount>0){
            if(weights!=null){
                for(int i=0;i<=inputsCount;i++){
                    pBeforeActiveOutput+=(i==inputsCount?bias:inputs[i])*weights[i];
                }
            }
        }
        return activeFunc.derivative(pBeforeActiveOutput);
    }

    public double[] derivativeBatch(double[][] batchInputs){
        double[] ret=new double[batchInputs.length];
        for(int i=0;i<batchInputs.length;i++){
            double pBeforeActiveOutput=0.0;
            for(int j=0;j<=inputsCount;j++){
                pBeforeActiveOutput+=(j==inputsCount?bias:batchInputs[i][j])*weights[j];
            }
            ret[i]=activeFunc.derivative(pBeforeActiveOutput);
        }
        return ret;
    }
}
