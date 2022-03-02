package i2f.log.api.impl;

/**
 * @author ltb
 * @date 2022/3/2 16:16
 * @desc
 */
public class StdoutLogWriterImpl extends PrintStreamLogWriterImpl {
    public StdoutLogWriterImpl() {
        super(System.out);
    }
}
