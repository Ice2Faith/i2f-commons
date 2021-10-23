package i2f.neuralnet.activation.func;


import i2f.neuralnet.activation.IActivationFunction;

public class Sigmoid implements IActivationFunction {
    private double a=1.0d;
    public Sigmoid(){

    }
    public Sigmoid(double a){
        this.a=a;
    }
    @Override
    public double calc(double val) {
        return 1.0/(1.0+Math.exp(-a*val));
    }

    @Override
    public double derivative(double val){
        double cval=calc(val);
        return cval*(1-cval);
    }
}
