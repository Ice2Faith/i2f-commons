package i2f.neuralnet.learn;


import i2f.neuralnet.core.NeuralNet;
import i2f.neuralnet.data.work.NeuralDataSet;
import i2f.neuralnet.exception.NeuralException;

public abstract class LearnAlgorithm {
    public enum LearnMode{ONLINE,BATCH};

    public enum LearnParadigm{SUPERVISED,UNSUPERVISED};

    public NeuralNet nn;

    public LearnMode learnMode;

    public LearnParadigm learnParadigm;

    public int maxEpochs=100;

    public int curEpoch=0;

    public double minOverallError=0.001;

    public double learnRate=0.1;

    public NeuralDataSet trainDataSet;

    public NeuralDataSet testDataSet;

    public NeuralDataSet validateDataSet;

    public boolean openPrintTrain=false;

    public abstract void train() throws NeuralException;

    public abstract void forward() throws NeuralException;

    public abstract void forward(int i) throws NeuralException;

    public abstract double calcNewWeight(int layer, int input, int neuron) throws NeuralException;

    public abstract double calcNewWeight(int layer, int input, int neuron, double error) throws NeuralException;

    public abstract void test() throws NeuralException;

    public abstract void test(int i) throws NeuralException;

    public abstract void print();
}
