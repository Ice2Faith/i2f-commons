package i2f.commons.core.utils.generator.regex.impl;

import i2f.commons.core.data.interfaces.IMap;
import i2f.commons.core.utils.file.FileUtil;

import java.io.File;

/**
 * @author ltb
 * @date 2021/10/26
 * 文件模板加载器
 * 支持classpath写法
 */
public class FileTemplateLoader implements IMap<String,String> {
    @Override
    public String map(String val) {
        if(val==null){
            return null;
        }
        val=val.trim();
        try{
            File file=FileUtil.getFileWithClasspath(val);
            if(file!=null){
                return FileUtil.loadTxtFile(file);
            }
        }catch(Exception e){

        }
        return null;
    }
}
