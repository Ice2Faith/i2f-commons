package i2f.neuralnet.data;


import i2f.neuralnet.activation.IActivationFunction;

/**
 * 输出层
 */
public class OutputLayer extends NeuralLayer {
    public OutputLayer(int neuronsCount, IActivationFunction activeFunc, int inputsCount){
        super(neuronsCount, activeFunc);
        this.inputsCount=inputsCount;
        this.nextLayer=null;
        init();
    }
}
