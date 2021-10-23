package i2f.neuralnet.data;


import i2f.neuralnet.activation.IActivationFunction;

/**
 * 隐藏层
 */
public class HiddenLayer extends NeuralLayer {
    public HiddenLayer(int neuronsCount, IActivationFunction activeFunc, int inputsCount){
        super(neuronsCount, activeFunc);
        this.inputsCount=inputsCount;
        init();
    }
}
