package i2f.neuralnet.exception;


public class NeuralException extends Exception {
    public NeuralException() {
    }

    public NeuralException(String message) {
        super(message);
    }

    public NeuralException(String message, Throwable cause) {
        super(message, cause);
    }

    public NeuralException(Throwable cause) {
        super(cause);
    }
}
