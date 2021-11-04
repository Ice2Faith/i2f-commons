package test.hadoop;

import i2f.commons.component.hadoop.HadoopContext;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import test.hadoop.base.WdMapper;
import test.hadoop.base.WdReducer;

import java.io.IOException;

/**
 * @author ltb
 * @date 2021/11/4
 */
public class TestHadoop {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        String infile="file:///D:/01test/hadoop/test";
        String outfile="file:///D:/01test/hadoop/test/out";
        HadoopContext context=new HadoopContext(){
            @Override
            protected String getDefaultFs() {
                return "file:///D:/01test/hadoop";
            }
        };
        context.createJob()
                .app(TestHadoop.class,"TestHadoop")
                .input(infile)
                .mapper(WdMapper.class, Text.class, IntWritable.class)
                .reducer(WdReducer.class,1)
                .output(outfile,Text.class,IntWritable.class)
                .doneAndWaitCompletion(true);
    }
}
