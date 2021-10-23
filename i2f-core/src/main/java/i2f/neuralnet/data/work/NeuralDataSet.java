package i2f.neuralnet.data.work;


import i2f.neuralnet.core.NeuralNet;

public class NeuralDataSet {

    public NeuralInputData inputData;
    public NeuralOutputData outputData;

    public NeuralNet nn;

    public int inputsCount;
    public int outputsCount;

    public int recordsCount;

    public NeuralDataSet(double[][] records, int[] inputColumns, int[] outputColumns){
        inputsCount=inputColumns.length;
        outputsCount=outputColumns.length;
        recordsCount=records.length;
        double[][] inputRecords=new double[recordsCount][inputsCount];
        double[][] outputRecords=new double[recordsCount][outputsCount];
        for(int i=0;i<inputsCount;i++){
            for(int j=0;j<recordsCount;j+=1){
                inputRecords[j][i]=records[j][inputColumns[i]];
            }
        }
        for(int i=0;i<outputsCount;i++){
            for(int j=0;j<recordsCount;j+=1){
                outputRecords[j][i]=records[j][outputColumns[i]];
            }
        }
        inputData=new NeuralInputData(inputRecords);
        outputData=new NeuralOutputData(outputRecords);
    }

    public NeuralDataSet(double[][] inputRecords,int numberOfOutputColumns){
        inputsCount=inputRecords[0].length;
        outputsCount=numberOfOutputColumns;
        recordsCount=inputRecords.length;
        inputData=new NeuralInputData(inputRecords);
        outputData=new NeuralOutputData(recordsCount,outputsCount);
    }

    public void printInput(){
        this.inputData.print();
    }

    public void printTargetOutput(){
        this.outputData.printTarget();
    }

    public void printNeuralOutput(){
        this.outputData.printNeural();
    }
}
