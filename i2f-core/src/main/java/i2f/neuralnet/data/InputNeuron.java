package i2f.neuralnet.data;


import i2f.neuralnet.activation.func.Linear;

/**
 * 输入神经元
 */
public class InputNeuron extends Neuron{
    public InputNeuron(){
        //输入，输入唯一，线性不改变输入，无偏置
        super(1,new Linear(1.0));
        bias=0.0;
    }

    @Override
    protected void init() {
        this.weights=new double[2];
        this.weights[0]=1.0;//不改变输入权重
        this.weights[1]=0.0;//对于输入来说，无偏置权重
    }
}
