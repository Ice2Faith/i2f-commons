package i2f.neuralnet.data;


import i2f.neuralnet.activation.func.Linear;

/**
 * 输入层
 */
public class InputLayer extends NeuralLayer {
    public InputLayer(int inputsCount){
        super(inputsCount,new Linear(1.0));
        this.inputsCount=inputsCount;
        init();
    }

    @Override
    public void init() {
        for(int i=0;i<neuronsCount;i+=1){
            neurons[i]=new InputNeuron();
            neurons[i].init();
        }
    }

    @Override
    public void calc() {
        outputs=new double[outputs.length];
        for(int i=0;i<neuronsCount;i+=1){
            neurons[i].inputs=new double[]{inputs[i]};
            neurons[i].calc();
            outputs[i]=neurons[i].output;
        }
    }
}
