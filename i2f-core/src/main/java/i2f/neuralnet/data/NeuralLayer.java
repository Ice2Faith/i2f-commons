package i2f.neuralnet.data;


import i2f.neuralnet.activation.IActivationFunction;

/**
 * 神经元层
 */

public abstract class NeuralLayer {
    public int neuronsCount;
    public Neuron[] neurons=new Neuron[0];
    public IActivationFunction activeFunc;
    public NeuralLayer previousLayer;
    public NeuralLayer nextLayer;
    public double[] inputs=new double[0];
    public double[] outputs=new double[0];
    public int inputsCount;

    public NeuralLayer(int neuronsCount){
        this.neuronsCount=neuronsCount;
        this.neurons=new Neuron[neuronsCount];
        this.outputs=new double[neuronsCount];
    }

    public NeuralLayer(int neuronsCount,IActivationFunction activeFunc){
        this.neuronsCount=neuronsCount;
        this.activeFunc=activeFunc;
        this.neurons=new Neuron[neuronsCount];
        this.outputs=new double[neuronsCount];
    }

    public void init(){
        for(int i=0;i<neuronsCount;i+=1){
            Neuron item=new Neuron(inputsCount,activeFunc);
            neurons[i]=item;
            item.init();
        }
    }

    public void calc(){
        outputs=new double[outputs.length];
        for(int i=0;i<neuronsCount;i+=1){
            neurons[i].inputs=inputs;
            neurons[i].calc();
            outputs[i]=neurons[i].output;
        }
    }
}
