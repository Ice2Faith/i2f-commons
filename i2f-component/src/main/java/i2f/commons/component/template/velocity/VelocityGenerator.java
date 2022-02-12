package i2f.commons.component.template.velocity;

import i2f.commons.component.json.gson.JsonUtil;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class VelocityGenerator {
    /**
     * 批量渲染整个目录模板
     * 说明：
     * 模板文件名，以.vm为后缀，输出文件名会自动去除这个后缀
     * 例如：
     * LoginController.java.vm
     * 则输出文件名为：
     * LoginController.java
     * 参数json文件，需要是一个JSON格式的合法对象
     * 输出路径，输出文件树和模板文件树结构一致
     * 模板文件特殊标记识别：
     * 模板文件第一行：
     * #filename 指定本模板渲染之后的文件名，不包含后缀，后缀按照模板文件后缀
     * 例如：
     * #filename ${tableName}Controller
     * 模板文件名为：
     * Controller.java.vm
     * 参数中tableName的值为User
     * 则输出文件名为：
     * UserController.java
     * 没有此标记或不在第一行，按照默认规则进行
     * @param templatePath 模板文件路径
     * @param paramJsonFile 参数JSON文件
     * @param outputPath 输出文件路径
     */
    public static void batchRender(String templatePath,String paramJsonFile,String outputPath,String charset) throws IOException {
        File ipath=new File(templatePath);
        if(!ipath.exists()){
            return;
        }
        Map<String, Object> params=new HashMap<>();
        if(paramJsonFile!=null){
            File pfile=new File(paramJsonFile);
            if(pfile.exists()){
                String json=readFileText(paramJsonFile,charset);
                params= JsonUtil.fromJsonTypeToken(json);
            }
        }
        if(ipath.isFile()){
            ipath=ipath.getParentFile();
        }
        File[] files=ipath.listFiles();
        for(File item : files) {
            if(item.isFile()) {
                String name = item.getName();
                if(name.endsWith(".vm")) {
                    String tpl=readFileText(item.getAbsolutePath(),charset);
                    String rs=render(tpl,params);
                    String sname = name.substring(0,name.length()-".vm".length());
                    int idx = rs.indexOf("\n");
                    if(idx>=0){
                        String fileNameLine = rs.substring(0,idx);
                        if(rs.startsWith("#filename ")){
                            rs = rs.substring(idx+1);
                            String fileName = fileNameLine.substring("#filename ".length());
                            fileName=fileName.trim();
                            if(!"".equals(fileName)){
                                int pidx = sname.lastIndexOf(".");
                                String suffix="";
                                if(pidx>=0){
                                    suffix = sname.substring(pidx);
                                }
                                sname=fileName+suffix;
                            }
                        }
                    }
                    File sfile=new File(outputPath,sname);
                    if(!sfile.getParentFile().exists()){
                        sfile.getParentFile().mkdirs();
                    }
                    writeFileText(sfile.getAbsolutePath(),rs,charset);
                }
            }else{
                String nextTplPath=item.getAbsolutePath();
                String nextOutputPath=new File(outputPath,item.getName()).getAbsolutePath();
                batchRender(nextTplPath,paramJsonFile,nextOutputPath,charset);
            }
        }
    }
    public static void writeFileText(String filePath,String content,String charset) throws IOException {
        try(BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath),charset))){
            writer.write(content);
            writer.flush();
        }
    }
    public static String readFileText(String filePath,String charset) throws IOException {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath),charset))) {
            String line=null;
            while ((line=reader.readLine())!=null){
                builder.append(line);
                builder.append("\n");
            }
        }
        return builder.toString();
    }
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
        ctx.put("_vm",new GeneratorTool());

        //创建写出对象
        StringWriter writer=new StringWriter();

        //渲染结果
        template.merge(ctx, writer);

        return writer.toString();
    }

}
