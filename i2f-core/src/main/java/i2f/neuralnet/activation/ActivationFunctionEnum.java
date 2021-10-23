package i2f.neuralnet.activation;


import i2f.neuralnet.activation.func.HyperTan;
import i2f.neuralnet.activation.func.Linear;
import i2f.neuralnet.activation.func.Sigmoid;
import i2f.neuralnet.activation.func.Step;

public enum ActivationFunctionEnum {
    LINEAR(Linear.class),
    SIGMOID(Sigmoid.class),
    STEP(Step.class),
    HYPERTAN(HyperTan.class);

    private Class<? extends IActivationFunction> clazz;

    private ActivationFunctionEnum(Class<? extends IActivationFunction> clazz){
        this.clazz=clazz;
    }

    public Class<? extends IActivationFunction> getResolveClass(){
        return this.clazz;
    }
}
