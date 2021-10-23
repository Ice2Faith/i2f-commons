package i2f.neuralnet.activation.func;


import i2f.neuralnet.activation.IActivationFunction;

public class Step implements IActivationFunction {
    @Override
    public double calc(double val) {
        if(val<0){
            return 0.0;
        }else{
            return 1.0;
        }
    }

    @Override
    public double derivative(double val){
        if(val==0)
            return Double.MAX_VALUE;
        else
            return 0.0;

    }
}
