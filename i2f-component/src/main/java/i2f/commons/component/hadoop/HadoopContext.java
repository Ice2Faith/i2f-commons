package i2f.commons.component.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.net.URI;


/**
 * @author ltb
 * @date 2021/11/4
 */
public class HadoopContext {
    private Configuration config;
    public HadoopContext(){
        init();
    }
    public void init(){
        config=new Configuration();
        String defFs=getDefaultFs();
        if(defFs!=null && !"".equals(defFs)){
            config.set("fs.defaultFS",defFs);
        }
        rewriteConfig(config);
    }
    protected String getDefaultFs(){
        return null;
    }
    protected void rewriteConfig(Configuration conf){

    }
    public Configuration getConfig(){
        return config;
    }
    public FileSystem getFileSystem() throws IOException {
        return FileSystem.get(getConfig());
    }
    public FileSystem getFileSystem(URI uri, String userName) throws IOException, InterruptedException {
        return FileSystem.get(uri,getConfig(),userName);
    }
    public JobBuilder createJob() throws IOException {
        return new JobBuilder(getConfig());
    }

    public static class JobBuilder {
        private Configuration config;
        private Job job;
        public JobBuilder(){

        }
        public JobBuilder(Configuration conf) throws IOException {
            this.config=conf;
            job=Job.getInstance(conf);
        }
        public JobBuilder(Configuration conf,Job job){
            this.config=conf;
            this.job=job;
        }
        public JobBuilder name(String name){
            job.setJobName(name);
            return this;
        }
        public JobBuilder appClass(Class clazz){
            job.setJarByClass(clazz);
            return this;
        }
        public JobBuilder app(Class appClass,String jobName){
            name(jobName);
            appClass(appClass);
            return this;
        }
        public JobBuilder inputFormatClass(Class clazz){
            job.setInputFormatClass(clazz);
            return this;
        }
        public JobBuilder addInput(Path path) throws IOException {
            FileInputFormat.addInputPath(job,path);
            return this;
        }
        public JobBuilder input(Class formatClazz,Path ... paths) throws IOException {
            inputFormatClass(formatClazz);
            for(Path item : paths){
                addInput(item);
            }
            return this;
        }
        public JobBuilder input(Class formatClazz,String ... paths) throws IOException {
            inputFormatClass(formatClazz);
            for(String item : paths){
                addInput(new Path(item));
            }
            return this;
        }
        public JobBuilder input(Path ... paths) throws IOException {
            return input(TextInputFormat.class,paths);
        }
        public JobBuilder input(String ... paths) throws IOException {
            return input(TextInputFormat.class,paths);
        }
        public JobBuilder output(Path path,Class outKeyClass,Class outValClass){
            FileOutputFormat.setOutputPath(job,path);
            job.setOutputKeyClass(outKeyClass);
            job.setOutputValueClass(outValClass);
            return this;
        }
        public JobBuilder output(String path,Class outKeyClass,Class outValClass){
            return output(new Path(path),outKeyClass,outValClass);
        }
        public JobBuilder mapper(Class mapperClass,Class outKeyClass,Class outValClass){
            job.setMapperClass(mapperClass);
            job.setMapOutputKeyClass(outKeyClass);
            job.setMapOutputValueClass(outValClass);

            return this;
        }
        public JobBuilder reducer(Class reducerClass,int reduceTaskCount){
            job.setReducerClass(reducerClass);
            job.setNumReduceTasks(reduceTaskCount);

            return this;
        }

        public Job done(){
            return job;
        }

        public void doneAndWaitCompletion(boolean verbose) throws InterruptedException, IOException, ClassNotFoundException {
            job.waitForCompletion(verbose);
        }
    }
}
