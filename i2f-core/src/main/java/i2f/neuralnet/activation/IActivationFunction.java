package i2f.neuralnet.activation;

public interface IActivationFunction {
    //激活函数
    double calc(double val);
    //对激活函数求导
    double derivative(double val);
}
