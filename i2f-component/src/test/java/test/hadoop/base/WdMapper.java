package test.hadoop.base;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @author ltb
 * @date 2021/11/4
 */
public class WdMapper extends Mapper<LongWritable, Text,Text, IntWritable> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] arr=value.toString().split("\\s+");
        for(String item : arr){
            context.write(new Text(item),new IntWritable(1));
        }
    }
}
