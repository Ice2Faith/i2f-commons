package i2f.commons.core.utils.jdbc.generate;


import i2f.commons.core.utils.jdbc.generate.data.DbGenType;
import i2f.commons.core.utils.jdbc.generate.data.GenerateContext;
import i2f.commons.core.utils.jdbc.generate.data.TableMeta;
import i2f.commons.core.utils.jdbc.generate.impl.*;
import lombok.Data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author ltb
 * @date 2021/8/25
 */
@Data
public class DbGenerator {

    public static GenerateContext genCodeFiles(GenerateContext ctx) throws IOException {
        TableMeta meta= ctx.meta;

        String str="";
        File pfile=null;
        FileOutputStream fos=null;
        if(DbGenType.isType(ctx.genType,DbGenType.BEAN)) {
            str = new BeanGenerator().generate(ctx);
            ctx.resultMap.put(DbGenType.BEAN,str);
            if(ctx.save2File) {
                File file = getSavePathFile(ctx);
                pfile = new File(file, "model/" + ctx.castTableName(meta.getTableName()) + "Bean.java");
                saveAsFile(str, pfile);
            }
        }

        if(DbGenType.isType(ctx.genType,DbGenType.DAO)) {
            str = new DaoGenerator().generate(ctx);
            ctx.resultMap.put(DbGenType.DAO,str);
            if(ctx.save2File) {
                File file = getSavePathFile(ctx);
                pfile = new File(file, "dao/" + ctx.castTableName(meta.getTableName()) + "Dao.java");
                saveAsFile(str, pfile);
            }
        }

        if(DbGenType.isType(ctx.genType,DbGenType.MAPPER)) {
            str = new MapperGenerator().generate(ctx);
            ctx.resultMap.put(DbGenType.MAPPER,str);
            if(ctx.save2File) {
                File file = new File(ctx.savePath,"resources");
                pfile = new File(file, "mapper/" + ctx.castTableName(meta.getTableName()) + "Mapper.xml");
                saveAsFile(str, pfile);
            }
        }

        if(DbGenType.isType(ctx.genType,DbGenType.SERVICE)) {
            str = new ServiceGenerator().generate(ctx);
            ctx.resultMap.put(DbGenType.SERVICE,str);
            if(ctx.save2File) {
                File file = getSavePathFile(ctx);
                pfile = new File(file, "service/I" + ctx.castTableName(meta.getTableName()) + "Service.java");
                saveAsFile(str, pfile);
            }
        }

        if(DbGenType.isType(ctx.genType,DbGenType.SERVICE_IMPL)) {
            str = new ServiceImplGenerator().generate(ctx);
            ctx.resultMap.put(DbGenType.SERVICE_IMPL,str);
            if(ctx.save2File) {
                File file = getSavePathFile(ctx);
                pfile = new File(file, "service/impl/" + ctx.castTableName(meta.getTableName()) + "ServiceImpl.java");
                saveAsFile(str, pfile);
            }
        }

        if(DbGenType.isType(ctx.genType,DbGenType.CONTROLLER)) {
            str = new ControllerGenerator().generate(ctx);
            ctx.resultMap.put(DbGenType.CONTROLLER,str);
            if(ctx.save2File) {
                File file = getSavePathFile(ctx);
                pfile = new File(file, "controller/" + ctx.castTableName(meta.getTableName()) + "Controller.java");
                saveAsFile(str, pfile);
            }
        }

        if(DbGenType.isType(ctx.genType,DbGenType.AXIOS_REQUEST)) {
            str = new AxiosInterfaceGenerator().generate(ctx);
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
