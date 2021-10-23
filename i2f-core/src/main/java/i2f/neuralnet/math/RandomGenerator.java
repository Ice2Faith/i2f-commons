package i2f.neuralnet.math;

import java.util.Random;

public class RandomGenerator {
    public static long seed=System.currentTimeMillis();
    public static Random random;
    public static double nextDouble(){
        if(random==null){
            random=new Random(seed);
        }
        return random.nextDouble();
    }
}
