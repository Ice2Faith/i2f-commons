package i2f.commons.component.template.velocity;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.*;
import java.util.Map;
import java.util.Properties;

public class VelocityGenerator {
    /**
     * 通过模板字符串通过渲染参数进行渲染，并返回结果
     * 实际上是借助临时文件达到目的
     * @param template
     * @param params
     * @return
     * @throws IOException
     */
    public static String render(String template,Map<String,Object> params) throws IOException {
//        URL url=Thread.currentThread().getContextClassLoader().getResource("");
//        String filePath=url.getFile();
        String filePath=System.getProperty("java.io.tmpdir");

        String fileName="_tpl_"+System.currentTimeMillis()+".vm";
        File file=new File(filePath,fileName);
        OutputStream os=new FileOutputStream(file);
        os.write(template.getBytes("UTF-8"));
        os.flush();
        os.close();
        String rs=render(null,false,file.getAbsolutePath(),params);
        file.delete();
        return rs;
    }

    /**
     * 模板引擎使用config配置加载是否在classpath下isInClassPath的模板文件fileName使用params参数绑定进行渲染
     * 并返回渲染结果字符串
     * @param config 模板引擎配置
     * @param isInClassPath 模板是否在classpath下
     * @param fileName 模板文件（在classpath下时，写classpath的相对路径，不在时写完整路径）
     * @param params 渲染参数
     * @return
     */
    public static String render(Properties config,boolean isInClassPath, String fileName, Map<String,Object> params){
        //初始化引擎，默认从classpath加载模板文件
        VelocityEngine engine=new VelocityEngine();

        if(isInClassPath){
            engine.setProperty(RuntimeConstants.RESOURCE_LOADER,"classpath");
            engine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        }else{
            File file=new File(fileName);
            engine.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH,file.getParentFile().getAbsolutePath());
            fileName=file.getName();
        }

        engine.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
        engine.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
        //有指定配置则使用配置覆盖
        if(config==null || config.isEmpty()){
            engine.init();
        }else{
            engine.init(config);
        }

        //创建模板对象
        Template template= engine.getTemplate(fileName);

        //创建绑定对象
        VelocityContext ctx=new VelocityContext(params);

        //创建写出对象
        StringWriter writer=new StringWriter();

        //渲染结果
        template.merge(ctx, writer);

        return writer.toString();
    }

}
