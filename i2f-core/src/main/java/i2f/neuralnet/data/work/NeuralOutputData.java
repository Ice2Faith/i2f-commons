package i2f.neuralnet.data.work;


import i2f.neuralnet.core.NeuralNet;

public class NeuralOutputData {
    public int outputsCount;
    public int recordsCount;

    public NeuralNet nn;

    public double[][] targetRecords=new double[0][0];
    public double[][] neuralRecords=new double[0][0];

    public NeuralOutputData(double[][] records){
        this.recordsCount=records.length;
        this.outputsCount=records[0].length;
        this.targetRecords=records;

        neuralRecords=new double[recordsCount][outputsCount];
        for(int i=0;i<recordsCount;i+=1){
            for(int j=0;j<outputsCount;j+=1){
                neuralRecords[i][j]=0.0;
            }
        }
    }

    public NeuralOutputData(int recordsCount,int outputsCount){
        this.recordsCount=recordsCount;
        this.outputsCount=outputsCount;
        this.targetRecords=new double[recordsCount][outputsCount];
        this.neuralRecords=new double[recordsCount][outputsCount];
        for(int i=0;i<recordsCount;i+=1){
            for(int j=0;j<outputsCount;j+=1){
                neuralRecords[i][j]=0.0;
            }
        }
    }

    public double[] getTargetColumn(int i){
        double[] ret=new double[recordsCount];
        for(int j=0;j<recordsCount;j++){
            ret[j]=targetRecords[j][i];
        }
        return ret;
    }

    public double[] getNeuralColumn(int i){
        double[] ret=new double[recordsCount];
        for(int j=0;j<recordsCount;j++){
            ret[j]=neuralRecords[j][i];
        }
        return ret;
    }

    public Double[] getMeanNeuralData(){
        Double[] ret=new Double[outputsCount];
        for(int j=0;j<outputsCount;j++){
            double r=0.0;
            for(int k=0;k<recordsCount;k++){
                r+=neuralRecords[k][j];
            }
            ret[j]=r/((double)recordsCount);
        }
        return ret;
    }

    public void printTarget() {
        System.out.println("Targets:");
        for(int k=0;k<recordsCount;k++){
            System.out.print("Target Output["+String.valueOf(k)+"]={ ");
            for(int i=0;i<outputsCount;i++){
                if(i==outputsCount-1){
                    System.out.print(String.valueOf(this.targetRecords[k][i])+"}\n");
                }
                else{
                    System.out.print(String.valueOf(this.targetRecords[k][i])+"\t");
                }
            }
        }
    }

    public void printNeural() {
        System.out.println("Neural:");
        for(int k=0;k<recordsCount;k++){
            System.out.print("Neural Output["+String.valueOf(k)+"]={ ");
            for(int i=0;i<outputsCount;i++){
                if(i==outputsCount-1){
                    System.out.print(String.valueOf(this.neuralRecords[k][i])+"}\n");
                }
                else{
                    System.out.print(String.valueOf(this.neuralRecords[k][i])+"\t");
                }
            }
        }
    }
}
