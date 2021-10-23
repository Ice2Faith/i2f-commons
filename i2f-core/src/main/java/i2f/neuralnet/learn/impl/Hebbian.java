package i2f.neuralnet.learn.impl;


import i2f.neuralnet.core.NeuralNet;
import i2f.neuralnet.data.HiddenLayer;
import i2f.neuralnet.data.Neuron;
import i2f.neuralnet.data.OutputLayer;
import i2f.neuralnet.data.work.NeuralDataSet;
import i2f.neuralnet.exception.NeuralException;
import i2f.neuralnet.learn.LearnAlgorithm;

public class Hebbian extends LearnAlgorithm {
    public int curRecord = 0;

    public double[][][] newWeights = new double[0][0][0];

    public Double[] curOutputMean = new Double[0];

    public double[] lastOutputMean = new double[0];

    public Hebbian(NeuralNet nn) {
        this.learnParadigm = LearnParadigm.UNSUPERVISED;
        this.nn = nn;
        this.newWeights = new double[nn.hiddenLayersCount + 1][][];
        int hiddenLayersCount = this.nn.hiddenLayersCount;
        for (int i = 0; i <= hiddenLayersCount; i++) {
            int neuronCount, inputsCount;
            if (i < hiddenLayersCount) {
                neuronCount = this.nn.hiddenLayers[i].neuronsCount;
                this.newWeights[i] = new double[neuronCount][];
                for (int j = 0; j < neuronCount; j++) {
                    inputsCount = this.nn.hiddenLayers[i].neurons[j].inputsCount;
                    this.newWeights[i][j] = new double[inputsCount + 1];
                    for (int k = 0; k <= inputsCount; k++) {
                        this.newWeights[i][j][k] = 0.0;
                    }
                }
            } else {
                neuronCount = this.nn.outputLayer.neuronsCount;
                this.newWeights[i] = new double[neuronCount][];
                for (int j = 0; j < neuronCount; j++) {
                    inputsCount = this.nn.outputLayer.neurons[j].inputsCount;
                    this.newWeights[i][j] = new double[inputsCount + 1];
                    for (int k = 0; k <= inputsCount; k++) {
                        this.newWeights[i][j][k] = 0.0;
                    }
                }
            }
        }
    }

    public Hebbian(NeuralNet nn, NeuralDataSet trainDataSet) {
        this(nn);
        this.trainDataSet = trainDataSet;
    }

    public Hebbian(NeuralNet nn, NeuralDataSet trainDataSet
            , LearnMode learnMode) {
        this(nn, trainDataSet);
        this.learnMode = learnMode;
    }

    @Override
    public double calcNewWeight(int layer, int inputIndex, int neuronIndex) throws NeuralException {
        if (layer > 0) {
            throw new NeuralException("Hebbian can be used only with single layer neural network yet");
        }
        double deltaWeight = learnRate;
        Neuron currNeuron = nn.outputLayer.neurons[neuronIndex];
        switch (learnMode) {
            case BATCH:
                double[] idxInput;
                if (inputIndex < currNeuron.inputsCount) {
                    idxInput = trainDataSet.inputData.getColumnData(inputIndex);
                } else {
                    idxInput = new double[testDataSet.recordsCount];
                    for (int i = 0; i < trainDataSet.recordsCount; i++) {
                        idxInput[i] = 0.0;
                    }
                }
                double multResultIdxInput = 0.0;
                for (int i = 0; i < trainDataSet.recordsCount; i++) {
                    multResultIdxInput += trainDataSet.outputData.neuralRecords[i][neuronIndex] * idxInput[i];
                }
                deltaWeight *= multResultIdxInput;
                break;
            case ONLINE:
                deltaWeight *= currNeuron.output;
                if (inputIndex < currNeuron.inputsCount) {
                    deltaWeight *= nn.inputs[inputIndex];
                }
                break;
        }

        return currNeuron.weights[inputIndex] + deltaWeight;
    }

    @Override
    public double calcNewWeight(int layer, int input, int neuron, double error) throws NeuralException {
        throw new NeuralException("Hebbian learning can be used only with the neuron's inputs and outputs, no error is used");
    }

    @Override
    public void train() throws NeuralException {
        if (nn.hiddenLayersCount > 0) {
            throw new NeuralException("Hebbian learning can be used only with single layer neural network");
        }
        switch (learnMode) {
            case BATCH:
                curEpoch = 0;
                forward();
                if (openPrintTrain) {
                    print();
                }
                setLastOutputMean();
                while (!stopCriteria()) {
                    curEpoch++;
                    for (int j = 0; j < nn.outputsCount; j++) {
                        for (int i = 0; i <= nn.inputsCount; i++) {
                            //weightUpdate(0, i, j,overallError.get(j));
                            newWeights[0][j][i] = calcNewWeight(0, i, j);
                        }
                    }
                    applyNewWeights();
                    setLastOutputMean();
                    forward();
                    if (openPrintTrain) {
                        print();
                    }
                }
                break;
            case ONLINE:
                curEpoch = 0;
                int k = 0;
                curRecord = 0;
                if (curOutputMean[0] == null) {
                    forward();
                }
                forward(k);
                if (openPrintTrain) {
                    print();
                }
                setLastOutputMean();
                while (!stopCriteria()) {
                    for (int j = 0; j < nn.outputsCount; j++) {
                        for (int i = 0; i <= nn.inputsCount; i++) {
                            //weightUpdate(0, i, j,error.get(curRecord)
                            //        .get(j));
                            newWeights[0][j][i] = calcNewWeight(0, i, j);
                        }
                    }
                    applyNewWeights();
                    curRecord = ++k;
                    if (k >= trainDataSet.recordsCount) {
                        k = 0;
                        setLastOutputMean();
                        curOutputMean = trainDataSet.outputData.getMeanNeuralData();
                        curRecord = 0;
                        curEpoch++;
                    }
                    forward(k);
                    if (openPrintTrain) {
                        print();
                    }
                }

                break;

        }

    }

    public void applyNewWeights() {
        int hiddenLayersCount = this.nn.hiddenLayersCount;
        for (int i = 0; i <= hiddenLayersCount; i++) {
            int neuronCount, inputCount;
            if (i < hiddenLayersCount) {
                HiddenLayer layer = this.nn.hiddenLayers[i];
                neuronCount = layer.neuronsCount;
                for (int j = 0; j < neuronCount; j++) {
                    inputCount = layer.neurons[j].inputsCount;
                    for (int k = 0; k <= inputCount; k++) {
                        double newWeight = this.newWeights[i][j][k];
                        layer.neurons[j].updateWeight(k, newWeight);
                    }
                }
            } else {
                OutputLayer layer = this.nn.outputLayer;
                neuronCount = layer.neuronsCount;
                for (int j = 0; j < neuronCount; j++) {
                    inputCount = layer.neurons[j].inputsCount;

                    for (int k = 0; k <= inputCount; k++) {
                        double newWeight = this.newWeights[i][j][k];
                        layer.neurons[j].updateWeight(k, newWeight);
                    }
                }
            }
        }
    }

    @Override
    public void forward(int i) throws NeuralException {
        if (nn.hiddenLayersCount > 0) {
            throw new NeuralException("Hebbian learning can be used only with single layer neural network");
        }
        nn.inputs = trainDataSet.inputData.records[i];
        nn.calc();
        trainDataSet.outputData.neuralRecords[i] = nn.outputs;

        //simpleError=simpleErrorEach.get(i);

    }

    @Override
    public void forward() throws NeuralException {
        if (nn.hiddenLayersCount > 0) {
            throw new NeuralException("Hebbian learning can be used only with single layer neural network");
        }
        for (int i = 0; i < trainDataSet.recordsCount; i++) {
            nn.inputs=trainDataSet.inputData.records[i];
            nn.calc();
            trainDataSet.outputData.neuralRecords[i] = nn.outputs;
        }
        curOutputMean = trainDataSet.outputData.getMeanNeuralData();
        //simpleError=simpleErrorEach.get(trainDataSet.recordsCount-1);
    }

    @Override
    public void test(int i) throws NeuralException {
        if (nn.hiddenLayersCount > 0) {
            throw new NeuralException("Hebbian learning can be used only with single layer neural network");
        }
        nn.inputs = testDataSet.inputData.records[i];
        nn.calc();
        testDataSet.outputData.neuralRecords[i] = nn.outputs;

        //simpleError=simpleErrorEach.get(i);

    }

    @Override
    public void test() throws NeuralException {
        if (nn.hiddenLayersCount > 0) {
            throw new NeuralException("Hebbian learning can be used only with single layer neural network");
        }
        for (int i = 0; i < testDataSet.recordsCount; i++) {
            nn.inputs=testDataSet.inputData.records[i];
            nn.calc();
            testDataSet.outputData.neuralRecords[i] = nn.outputs;
        }
        //curOutputMean=trainDataSet.getMeanNeuralOutput();
        //simpleError=simpleErrorEach.get(trainDataSet.recordsCount-1);

    }


    @Override
    public void print() {
        if (learnMode == LearnMode.ONLINE) {
            System.out.println("Epoch=" + String.valueOf(curEpoch) + "; Record="
                    + String.valueOf(curRecord));
        } else {
            System.out.println("Epoch= " + String.valueOf(curEpoch));
        }
    }

    public boolean stopCriteria() {
        boolean stop = true;
        for (int i = 0; i < curOutputMean.length; i++) {
            if (curOutputMean[i] <= lastOutputMean[i]) {
                stop = false;
            }
        }
        return stop || curEpoch >= maxEpochs;
    }

    private void setLastOutputMean() {
        lastOutputMean = new double[curOutputMean.length];
        for (int i = 0; i < curOutputMean.length; i++) {
            lastOutputMean[i] = curOutputMean[i];
        }
    }
}
