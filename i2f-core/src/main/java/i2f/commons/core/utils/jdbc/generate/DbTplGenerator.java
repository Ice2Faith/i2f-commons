package i2f.commons.core.utils.jdbc.generate;


import i2f.commons.core.utils.data.ContainerUtil;
import i2f.commons.core.utils.file.FileUtil;
import i2f.commons.core.utils.generator.regex.RegexGenerator;
import i2f.commons.core.utils.jdbc.generate.data.DbGenType;
import i2f.commons.core.utils.jdbc.generate.data.GenerateContext;
import i2f.commons.core.utils.jdbc.generate.data.TableMeta;
import lombok.Data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2021/8/25
 */
@Data
public class DbTplGenerator {
    public static File getResourceFile(String fileName){
        URL url=Thread.currentThread().getContextClassLoader().getResource(fileName);
        return new File(url.getFile());
    }
    public static String tplGenProxy(GenerateContext ctx,String tplPath) throws IOException {
        File tplFile=getResourceFile(tplPath);
        String tpl= FileUtil.loadTxtFile(tplFile);
        List basePackages= ContainerUtil.arrList(
                "i2f.commons.core.utils.jdbc.generate.data",
                "i2f.commons.core.utils.data");
        Map<String, Object> params=new HashMap<>();
        params.put("ctx",ctx);
        params.put("now",new Date());
        String rs= RegexGenerator.render(tpl,params,basePackages);
        return rs;
    }

    public static GenerateContext genCodeFiles(GenerateContext ctx) throws IOException {
        TableMeta meta= ctx.meta;

        String str="";
        File pfile=null;
        FileOutputStream fos=null;
        if(DbGenType.isType(ctx.genType,DbGenType.BEAN)) {
            str = tplGenProxy(ctx,"tpl/Bean.tpl");
            ctx.resultMap.put(DbGenType.BEAN,str);
            if(ctx.save2File) {
                File file = getSavePathFile(ctx);
                pfile = new File(file, "model/" + ctx.castTableName(meta.getTableName()) + "Bean.java");
                saveAsFile(str, pfile);
            }
        }

        if(DbGenType.isType(ctx.genType,DbGenType.DAO)) {
            str = tplGenProxy(ctx,"tpl/Dao.tpl");
            ctx.resultMap.put(DbGenType.DAO,str);
            if(ctx.save2File) {
                File file = getSavePathFile(ctx);
                pfile = new File(file, "dao/" + ctx.castTableName(meta.getTableName()) + "Dao.java");
                saveAsFile(str, pfile);
            }
        }

        if(DbGenType.isType(ctx.genType,DbGenType.MAPPER)) {
            str = tplGenProxy(ctx,"tpl/Mapper.tpl");
            ctx.resultMap.put(DbGenType.MAPPER,str);
            if(ctx.save2File) {
                File file = new File(ctx.savePath,"resources");
                pfile = new File(file, "mapper/" + ctx.castTableName(meta.getTableName()) + "Mapper.xml");
                saveAsFile(str, pfile);
            }
        }

        if(DbGenType.isType(ctx.genType,DbGenType.SERVICE)) {
            str = tplGenProxy(ctx,"tpl/Service.tpl");
            ctx.resultMap.put(DbGenType.SERVICE,str);
            if(ctx.save2File) {
                File file = getSavePathFile(ctx);
                pfile = new File(file, "service/I" + ctx.castTableName(meta.getTableName()) + "Service.java");
                saveAsFile(str, pfile);
            }
        }

        if(DbGenType.isType(ctx.genType,DbGenType.SERVICE_IMPL)) {
            str = tplGenProxy(ctx,"tpl/ServiceImpl.tpl");
            ctx.resultMap.put(DbGenType.SERVICE_IMPL,str);
            if(ctx.save2File) {
                File file = getSavePathFile(ctx);
                pfile = new File(file, "service/impl/" + ctx.castTableName(meta.getTableName()) + "ServiceImpl.java");
                saveAsFile(str, pfile);
            }
        }

        if(DbGenType.isType(ctx.genType,DbGenType.CONTROLLER)) {
            str = tplGenProxy(ctx,"tpl/Controller.tpl");
            ctx.resultMap.put(DbGenType.CONTROLLER,str);
            if(ctx.save2File) {
                File file = getSavePathFile(ctx);
                pfile = new File(file, "controller/" + ctx.castTableName(meta.getTableName()) + "Controller.java");
                saveAsFile(str, pfile);
            }
        }

        if(DbGenType.isType(ctx.genType,DbGenType.AXIOS_REQUEST)) {
            str = tplGenProxy(ctx,"tpl/AxiosRequest.tpl");
            ctx.resultMap.put(DbGenType.AXIOS_REQUEST,str);
            if(ctx.save2File) {
                File file = new File(ctx.savePath,"web");
                pfile = new File(file, "api/" + ctx.castTableName(meta.getTableName()) + "Api.js");
                saveAsFile(str, pfile);
            }
        }

        return ctx;
    }

    private static void saveAsFile(String str, File pfile) throws IOException {
        FileOutputStream fos=null;
        if (!pfile.getParentFile().exists()) {
            pfile.getParentFile().mkdirs();
        }
        fos = new FileOutputStream(pfile);
        fos.write(str.getBytes("UTF-8"));
        fos.close();
    }

    private static File getSavePathFile(GenerateContext ctx) {
        String path= ctx.basePackage.replace(".","/");
        String sfile= ctx.savePath;
        File file=new File(sfile,path);
        return file;
    }

}
