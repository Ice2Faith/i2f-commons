package i2f.neuralnet.activation.func;


import i2f.neuralnet.activation.IActivationFunction;

public class HyperTan implements IActivationFunction {
    private double a=1.0d;
    public HyperTan(){

    }
    public HyperTan(double a){
        this.a=a;
    }
    @Override
    public double calc(double val) {
        return (1.0-Math.exp(-a*val))/(1.0+Math.exp(-a*val));
    }

    @Override
    public double derivative(double val){
        return (1.0)-Math.pow(calc(val),2.0);
    }
}
