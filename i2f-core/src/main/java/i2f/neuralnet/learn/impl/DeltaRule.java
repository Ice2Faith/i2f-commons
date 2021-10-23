package i2f.neuralnet.learn.impl;


import i2f.neuralnet.core.NeuralNet;
import i2f.neuralnet.data.HiddenLayer;
import i2f.neuralnet.data.Neuron;
import i2f.neuralnet.data.OutputLayer;
import i2f.neuralnet.data.work.NeuralDataSet;
import i2f.neuralnet.exception.NeuralException;
import i2f.neuralnet.learn.LearnAlgorithm;

public class DeltaRule extends LearnAlgorithm {

    public enum ErrorMeasurement {SimpleError, SquareError, NDegreeError, MSE};

    public double[][] error = new double[0][0];
    public double[] generalError = new double[0];
    public double[] overallError = new double[0];
    public double overallGeneralError;

    public double[][] testError = new double[0][0];
    public double[] testGeneralError = new double[0];
    public double[] testOverallError = new double[0];
    public double testOverallGeneralError;

    public double degreeGeneralError=2.0;
    public double degreeOverallError=0.0;

    public ErrorMeasurement generalErrorMeasurement = ErrorMeasurement.SquareError;
    public ErrorMeasurement overallErrorMeasurement = ErrorMeasurement.MSE;

    public int curRecord = 0;

    public double[][][] newWeights = new double[0][0][0];

    public DeltaRule(NeuralNet nn) {
        this.nn = nn;
        this.learnParadigm = LearnParadigm.SUPERVISED;

        this.newWeights=new double[nn.hiddenLayersCount+1][][];
        int hiddenLayersCount = this.nn.hiddenLayersCount;
        for (int i = 0; i <= hiddenLayersCount; i++) {
            int neuronCount = 0, inputsCount = 0;
            if (i < hiddenLayersCount) {
                neuronCount = this.nn.hiddenLayers[i].neuronsCount;
                this.newWeights[i]=new double[neuronCount][];
                for (int j = 0; j < neuronCount; j++) {
                    inputsCount = this.nn.hiddenLayers[i].neurons[j].inputsCount;
                    this.newWeights[i][j]=new double[inputsCount+1];
                    for (int k = 0; k <= inputsCount; k++) {
                        this.newWeights[i][j][k]=0.0;
                    }
                }
            } else {
                neuronCount = this.nn.outputLayer.neuronsCount;
                this.newWeights[i]=new double[neuronCount][];
                for (int j = 0; j < neuronCount; j++) {
                    inputsCount = this.nn.outputLayer.neurons[j].inputsCount;
                    this.newWeights[i][j]=new double[inputsCount+1];
                    for (int k = 0; k <= inputsCount; k++) {
                        this.newWeights[i][j][k]=0.0;
                    }
                }
            }
        }
    }

    public DeltaRule(NeuralNet nn, NeuralDataSet trainDataSet) {
        this(nn);
        this.trainDataSet = trainDataSet;
        this.generalError=new double[trainDataSet.recordsCount];
        this.error=new double[trainDataSet.recordsCount][];
        this.overallError=new double[nn.outputsCount];
        for (int i = 0; i < trainDataSet.recordsCount; i++) {
            this.generalError[i]=0.0;
            this.error[i]=new double[nn.outputsCount];
            for (int j = 0; j < nn.outputsCount; j++) {
                if (i == 0) {
                    this.overallError[j]=0.0;
                }
                this.error[i][j]=0.0;
            }
        }
    }

    public DeltaRule(NeuralNet nn, NeuralDataSet trainDataSet
            , LearnMode learnMode) {
        this(nn, trainDataSet);
        this.learnMode = learnMode;
    }

    public void setTestDataSet(NeuralDataSet testDataSet) {
        this.testDataSet = testDataSet;
        this.testGeneralError=new double[testDataSet.recordsCount];
        this.testError=new double[testDataSet.recordsCount][];
        this.testOverallError=new double[nn.outputsCount];
        for (int i = 0; i < testDataSet.recordsCount; i++) {
            this.testGeneralError[i]=0.0;
            this.testError[i]=new double[nn.outputsCount];
            for (int j = 0; j < this.nn.outputsCount; j++) {
                if (i == 0) {
                    this.testOverallError[j]=0.0;
                }
                this.testError[i][j]=0.0;
            }
        }
    }

    public void setGeneralErrorMeasurement(ErrorMeasurement errorMeasurement) {
        switch (errorMeasurement) {
            case SimpleError:
                this.degreeGeneralError = 1;
                break;
            case SquareError:
            case MSE:
                this.degreeGeneralError = 2;
        }
        this.generalErrorMeasurement = errorMeasurement;
    }

    public void setOverallErrorMeasurement(ErrorMeasurement errorMeasurement) {
        switch (errorMeasurement) {
            case SimpleError:
                this.degreeOverallError = 1;
                break;
            case SquareError:
            case MSE:
                this.degreeOverallError = 2;
        }
        this.overallErrorMeasurement = errorMeasurement;
    }

    @Override
    public double calcNewWeight(int layer, int inputIndex, int neuronIndex) throws NeuralException {
        if (layer > 0) {
            throw new NeuralException("Delta rule can be used only with single layer neural network");
        }
        double deltaWeight = learnRate;
        Neuron currNeuron = nn.outputLayer.neurons[neuronIndex];
        switch (learnMode) {
            case BATCH:
                double[] derivativeResult = currNeuron.derivativeBatch(trainDataSet.inputData.records);
                double[] idxInput;
                if (inputIndex < currNeuron.inputsCount) {
                    idxInput = trainDataSet.inputData.getColumnData(inputIndex);
                } else {
                    idxInput = new double[trainDataSet.recordsCount];
                    for (int i = 0; i < trainDataSet.recordsCount; i++) {
                        idxInput[i]=1.0;
                    }
                }
                double multDerivResultIdxInput = 0.0;
                for (int i = 0; i < trainDataSet.recordsCount; i++) {
                    multDerivResultIdxInput += error[i][neuronIndex] * derivativeResult[i] * idxInput[i];
                }
                deltaWeight *= multDerivResultIdxInput;
                break;
            case ONLINE:
                deltaWeight *= error[curRecord][neuronIndex];
                deltaWeight *= currNeuron.derivative(nn.inputs);
                if (inputIndex < currNeuron.inputsCount) {
                    deltaWeight *= nn.inputs[inputIndex];
                }
                break;
        }

        return currNeuron.weights[inputIndex] + deltaWeight;
    }

    @Override
    public double calcNewWeight(int layer, int inputIndex, int neuronIndex, double error) throws NeuralException {
        if (layer > 0) {
            throw new NeuralException("Delta rule can be used only with single layer neural network");
        }
        double deltaWeight = learnRate * error;
        Neuron currNeuron = nn.outputLayer.neurons[neuronIndex];
        switch (learnMode) {
            case BATCH:
                double[] derivativeResult = currNeuron.derivativeBatch(trainDataSet.inputData.records);
                double[] idxInput;
                if (inputIndex < currNeuron.inputsCount) {
                    idxInput = trainDataSet.inputData.getColumnData(inputIndex);
                } else {
                    idxInput = new double[trainDataSet.recordsCount];
                    for (int i = 0; i < trainDataSet.recordsCount; i++) {
                        idxInput[i]=1.0;
                    }
                }
                double multDerivResultIdxInput = 0.0;
                for (int i = 0; i < trainDataSet.recordsCount; i++) {
                    multDerivResultIdxInput += derivativeResult[i] * idxInput[i];
                }
                deltaWeight *= multDerivResultIdxInput;
                break;
            case ONLINE:
                deltaWeight *= currNeuron.derivative(nn.inputs);
                if (inputIndex < currNeuron.inputsCount) {
                    deltaWeight *= nn.inputs[inputIndex];
                }
                break;
        }

        return currNeuron.weights[inputIndex] + deltaWeight;
    }

    @Override
    public void train() throws NeuralException {
        if (nn.hiddenLayersCount > 0) {
            throw new NeuralException("Delta rule can be used only with single layer neural network");
        }
        switch (learnMode) {
            case BATCH:
                curEpoch = 0;
                forward();
                if (openPrintTrain) {
                    print();
                }
                while (curEpoch < maxEpochs && overallGeneralError > minOverallError) {
                    curEpoch++;
                    for (int j = 0; j < nn.outputsCount; j++) {
                        for (int i = 0; i <= nn.inputsCount; i++) {
                            //weightUpdate(0, i, j,overallError.get(j));
                            newWeights[0][j][i]=calcNewWeight(0, i, j);
                        }
                    }
                    applyNewWeights();
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
                forward(k);
                if (openPrintTrain) {
                    print();
                }
                while (curEpoch < maxEpochs && overallGeneralError > minOverallError) {
                    for (int j = 0; j < nn.outputsCount; j++) {
                        for (int i = 0; i <= nn.inputsCount; i++) {
                            //weightUpdate(0, i, j,error.get(currentRecord).get(j));
                            newWeights[0][j][i]= calcNewWeight(0, i, j);
                        }
                    }
                    applyNewWeights();
                    curRecord = ++k;
                    if (k >= trainDataSet.recordsCount) {
                        k = 0;
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

    public double generalError(double[] yt, double[] y) {
        int ny = yt.length;
        double ret = 0.0;
        for (int i = 0; i < ny; i++) {
            ret += Math.pow(yt[i] - y[i], degreeGeneralError);
        }
        if (generalErrorMeasurement == ErrorMeasurement.MSE)
            ret *= (1.0 / ny);
        else
            ret *= (1.0 / degreeGeneralError);
        return ret;
    }

    public double overallError(double[] yt, double[] y) {
        int n = yt.length;
        double ret = 0.0;
        for (int i = 0; i < n; i++) {
            ret += Math.pow(yt[i] - y[i], degreeOverallError);
        }
        if (overallErrorMeasurement == ErrorMeasurement.MSE)
            ret *= (1.0 / n);
        else
            ret *= (1.0 / degreeOverallError);
        return ret;
    }

    public double simpleError(double yt, double y) {
        return yt - y;
    }

    public double overallGeneralErrorArrayList(double[][] yt, double[][] y) {
        int n = yt.length;
        int ny = yt[0].length;
        double ret = 0.0;
        for (int i = 0; i < n; i++) {
            double resultY = 0.0;
            for (int j = 0; j < ny; j++) {
                resultY += Math.pow(yt[i][j] - y[i][j], degreeGeneralError);
            }
            if (generalErrorMeasurement == ErrorMeasurement.MSE)
                ret += Math.pow((1.0 / ny) * resultY, degreeOverallError);
            else
                ret += Math.pow((1.0 / degreeGeneralError) * resultY, degreeOverallError);
        }
        if (overallErrorMeasurement == ErrorMeasurement.MSE)
            ret *= (1.0 / n);
        else
            ret *= (1.0 / degreeOverallError);
        return ret;
    }

    @Override
    public void forward(int i) throws NeuralException {
        if (nn.hiddenLayersCount > 0) {
            throw new NeuralException("Delta rule can be used only with single layer neural network");
        }
        nn.inputs=trainDataSet.inputData.records[i];
        nn.calc();
        trainDataSet.outputData.neuralRecords[i]=nn.outputs;
        generalError[i]=generalError(
                trainDataSet.outputData.targetRecords[i]
                , trainDataSet.outputData.neuralRecords[i]);
        for (int j = 0; j < nn.outputsCount; j++) {
            overallError[j]=overallError(trainDataSet.outputData.getTargetColumn(j)
                            , trainDataSet.outputData.getNeuralColumn(j));
            error[i][j]=simpleError(trainDataSet.outputData.getTargetColumn(j)[i]
                            , trainDataSet.outputData.getNeuralColumn(j)[i]);
        }
        overallGeneralError = overallGeneralErrorArrayList(
                trainDataSet.outputData.targetRecords
                , trainDataSet.outputData.neuralRecords);
        //simpleError=simpleErrorEach.get(i);
    }

    @Override
    public void forward() throws NeuralException {
        if (nn.hiddenLayersCount > 0) {
            throw new NeuralException("Delta rule can be used only with single layer neural network");
        }
        for (int i = 0; i < trainDataSet.recordsCount; i++) {
            nn.inputs=trainDataSet.inputData.records[i];
            nn.calc();
            trainDataSet.outputData.neuralRecords[i]=nn.outputs;
            generalError[i]=generalError(
                            trainDataSet.outputData.targetRecords[i]
                            , trainDataSet.outputData.neuralRecords[i]);
            for (int j = 0; j < nn.outputsCount; j++) {
                error[i][j]=simpleError(trainDataSet.outputData.targetRecords[i][j]
                                , trainDataSet.outputData.neuralRecords[i][j]);
            }
        }
        for (int j = 0; j < nn.outputsCount; j++) {
            overallError[j]=overallError(trainDataSet.outputData.getTargetColumn(j)
                            , trainDataSet.outputData.getNeuralColumn(j));
        }
        overallGeneralError = overallGeneralErrorArrayList(
                trainDataSet.outputData.targetRecords
                , trainDataSet.outputData.neuralRecords);
        //simpleError=simpleErrorEach.get(trainDataSet.recordCount-1);
    }

    @Override
    public void test(int i) throws NeuralException {
        if (nn.hiddenLayersCount > 0) {
            throw new NeuralException("Delta rule can be used only with single layer neural network");
        }
        nn.inputs=testDataSet.inputData.records[i];
        nn.calc();
        testDataSet.outputData.neuralRecords[i]=nn.outputs;
        testGeneralError[i]=generalError(
                        testDataSet.outputData.targetRecords[i]
                        , testDataSet.outputData.neuralRecords[i]);
        for (int j = 0; j < nn.outputsCount; j++) {
            testOverallError[j]=overallError(testDataSet.outputData.getTargetColumn(j)
                            , testDataSet.outputData.getNeuralColumn(j));
            testError[i][j]=simpleError(testDataSet.outputData.getTargetColumn(j)[i]
                            , testDataSet.outputData.getNeuralColumn(j)[i]);
        }
        testOverallGeneralError = overallGeneralErrorArrayList(
                testDataSet.outputData.targetRecords
                , testDataSet.outputData.neuralRecords);
        //simpleError=simpleErrorEach.get(i);

    }

    @Override
    public void test() throws NeuralException {
        if (nn.hiddenLayersCount > 0) {
            throw new NeuralException("Delta rule can be used only with single layer neural network");
        }
        for (int i = 0; i < testDataSet.recordsCount; i++) {
            nn.inputs=testDataSet.inputData.records[i];
            nn.calc();
            testDataSet.outputData.neuralRecords[i]=nn.outputs;
            testGeneralError[i]=generalError(
                            testDataSet.outputData.targetRecords[i]
                            , testDataSet.outputData.neuralRecords[i]);
            for (int j = 0; j < nn.outputsCount; j++) {
                testError[i][j]=simpleError(testDataSet.outputData.targetRecords[i][j]
                                , testDataSet.outputData.neuralRecords[i][j]);
            }
        }
        for (int j = 0; j < nn.outputsCount; j++) {
            testOverallError[j]=overallError(testDataSet.outputData.getTargetColumn(j)
                            , testDataSet.outputData.getNeuralColumn(j));
        }
        testOverallGeneralError = overallGeneralErrorArrayList(
                testDataSet.outputData.targetRecords
                , testDataSet.outputData.neuralRecords);
        //simpleError=simpleErrorEach.get(trainDataSet.recordCount-1);

    }

    @Override
    public void print(){
        if(learnMode==LearnMode.ONLINE){
            System.out.println("Epoch="+String.valueOf(curEpoch)+"; Record="
                    +String.valueOf(curRecord)+"; Overall Error="
                    +String.valueOf(overallGeneralError));
        }
        else{
            System.out.println("Epoch= "+String.valueOf(curEpoch)
                    +"; Overall Error ="+String.valueOf(overallGeneralError));
        }
    }
}
