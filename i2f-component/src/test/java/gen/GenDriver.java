package gen;

import i2f.commons.component.template.velocity.VelocityGenerator;

import java.io.File;
import java.io.IOException;

/**
 * @author ltb
 * @date 2022/2/11 14:43
 * @desc
 */
public class GenDriver {
    public static void main(String[] args) throws IOException {
        String path="D:\\IDEA_ROOT\\i2f-commons\\i2f-component\\src\\test\\java\\gen";
        String tplPath=new File(path,"tpl").getAbsolutePath();
        String argPath=new File(path,"arg\\data.json").getAbsolutePath();
        String outPath=new File(path,"out").getAbsolutePath();
        VelocityGenerator.batchRender(tplPath,argPath,outPath,"UTF-8");
        System.out.println("done...");
    }
}
