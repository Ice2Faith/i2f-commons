package i2f.neuralnet.data.work;


import i2f.neuralnet.core.NeuralNet;

public class NeuralInputData {
    public int inputsCount;
    public int recordsCount;

    public NeuralNet nn;

    public double[][] records=new double[0][0];

    public NeuralInputData(double[][] records){
        this.recordsCount=records.length;
        this.inputsCount=records[0].length;
        this.records=records;
    }

    public double[] getColumnData(int i){
        double[] ret=new double[recordsCount];
        for(int j=0;j<recordsCount;j++){
            ret[j]=records[j][i];
        }
        return ret;
    }


    public double[] getMeanInputData(){
        double[] ret=new double[inputsCount];
        for(int i=0;i<inputsCount;i++){
            double r=0.0;
            for(int k=0;k<recordsCount;k++){
                r+=records[k][i];
            }
            ret[i]=r/((double)recordsCount);
        }
        return ret;
    }

    public void print() {
        System.out.println("Inputs:");
        for(int k=0;k<recordsCount;k++){
            System.out.print("Input["+String.valueOf(k)+"]={ ");
            for(int i=0;i<inputsCount;i++){
                if(i==inputsCount-1){
                    System.out.print(String.valueOf(this.records[k][i])+"}\n");
                }
                else{
                    System.out.print(String.valueOf(this.records[k][i])+"\t");
                }
            }
        }
    }
}
