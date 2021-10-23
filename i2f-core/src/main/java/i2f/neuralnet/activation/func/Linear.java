package i2f.neuralnet.activation.func;


import i2f.neuralnet.activation.IActivationFunction;

public class Linear implements IActivationFunction {
    private double a=1.0d;
    public Linear(){

    }
    public Linear(double a){
        this.a=a;
    }
    @Override
    public double calc(double val) {
        return val*a;
    }

    @Override
    public double derivative(double val){
        return a;
    }
}
