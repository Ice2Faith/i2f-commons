package i2f.log.api.impl;

import java.io.PrintStream;

/**
 * @author ltb
 * @date 2022/3/2 16:16
 * @desc
 */
public class PrintStreamLogWriterImpl extends AbsPrintStreamLogWriterImpl {
    private PrintStream ps;
    public PrintStreamLogWriterImpl(PrintStream ps){
        this.ps=ps;
    }
    @Override
    protected void writeLine(String str) {
        ps.println(str);
    }
}
