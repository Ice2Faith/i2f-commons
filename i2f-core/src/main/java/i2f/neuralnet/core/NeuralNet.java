package i2f.neuralnet.core;


import i2f.neuralnet.activation.IActivationFunction;
import i2f.neuralnet.data.HiddenLayer;
import i2f.neuralnet.data.InputLayer;
import i2f.neuralnet.data.Neuron;
import i2f.neuralnet.data.OutputLayer;

/**
 * 神经网络
 */
public class NeuralNet {
    public InputLayer inputLayer;
    public HiddenLayer[] hiddenLayers=new HiddenLayer[0];
    public OutputLayer outputLayer;
    public int inputsCount;
    public int hiddenLayersCount;
    public int outputsCount;
    public double[] inputs=new double[0];
    public double[] outputs=new double[0];

    private boolean activeBias=true;

    public NeuralNet(int inputsCount, int outputsCount, IActivationFunction outputActiveFunc){
        this(inputsCount,outputsCount,new int[0],new IActivationFunction[0],outputActiveFunc);
    }

    public NeuralNet(int inputsCount, int outputsCount,
                     int[] hiddenLayersNeuronsCountArr,
                     IActivationFunction[] hiddenActiveFuncArr,
                     IActivationFunction outputActiveFunc){
        this.inputsCount=inputsCount;
        this.outputsCount=outputsCount;

        this.inputs=new double[inputsCount];
        this.inputLayer=new InputLayer(inputsCount);

        int minLen=hiddenLayersNeuronsCountArr.length;
        if(minLen>hiddenActiveFuncArr.length){
            minLen=hiddenActiveFuncArr.length;
        }
        this.hiddenLayersCount=minLen;
        this.hiddenLayers=new HiddenLayer[this.hiddenLayersCount];
        for(int i=0;i<this.hiddenLayersCount;i+=1){
            if(i==0){
                HiddenLayer layer=new HiddenLayer(hiddenLayersNeuronsCountArr[i],
                        hiddenActiveFuncArr[i],
                        inputLayer.neuronsCount);
                hiddenLayers[i]=layer;
                inputLayer.nextLayer=layer;
                layer.previousLayer=inputLayer;
            }else{
                HiddenLayer previousLayer=hiddenLayers[i-1];
                HiddenLayer layer=new HiddenLayer(hiddenLayersNeuronsCountArr[i],
                        hiddenActiveFuncArr[i],
                        previousLayer.neuronsCount);
                hiddenLayers[i]=layer;
                previousLayer.nextLayer=layer;
                layer.previousLayer=previousLayer;
            }
        }

        if(hiddenLayersCount>0){
            HiddenLayer previousLayer=hiddenLayers[hiddenLayersCount-1];
            OutputLayer layer=new OutputLayer(outputsCount,
                    outputActiveFunc,
                    previousLayer.neuronsCount);
            outputLayer=layer;
            previousLayer.nextLayer=layer;
            layer.previousLayer=previousLayer;
        }else{
            outputLayer=new OutputLayer(outputsCount,
                    outputActiveFunc,
                    inputsCount);
            inputLayer.nextLayer=outputLayer;
            outputLayer.previousLayer=inputLayer;
        }
    }

    public void calc(){
        inputLayer.inputs=inputs;
        inputLayer.calc();

        for(int i=0;i<hiddenLayersCount;i+=1){
            HiddenLayer layer=hiddenLayers[i];
            layer.inputs=layer.previousLayer.outputs;
            layer.calc();
        }

        outputLayer.inputs=outputLayer.previousLayer.outputs;
        outputLayer.calc();

        this.outputs=outputLayer.outputs;
    }


    /**
     * print
     * Method to print the neural network information
     */
    public void print(){
        System.out.println("Neural Network: ");
        System.out.println("\tInputs:"+String.valueOf(this.inputsCount));
        System.out.println("\tOutputs:"+String.valueOf(this.outputsCount));
        System.out.println("\tHidden Layers: "+String.valueOf(hiddenLayersCount));
        for(int i=0;i<hiddenLayersCount;i++){
            System.out.println("\t\tHidden Layer "+
                    String.valueOf(i)+": "+
                    String.valueOf(this.hiddenLayers[i].neuronsCount)+" Neurons");
        }

    }

    public void deactivateBias(){
        if(hiddenLayersCount>0){
            for(HiddenLayer layer:hiddenLayers){
                for(Neuron neuron:layer.neurons){
                    neuron.deactivateBias();
                }
            }
        }
        for(Neuron neuron:outputLayer.neurons){
            neuron.deactivateBias();
        }
    }

    public void activateBias(){
        for(HiddenLayer layer:hiddenLayers){
            for(Neuron neuron:layer.neurons){
                neuron.activateBias();
            }
        }
        for(Neuron neuron:outputLayer.neurons){
            neuron.activateBias();
        }
    }

    public boolean isBiasActive(){
        return activeBias;
    }
}
