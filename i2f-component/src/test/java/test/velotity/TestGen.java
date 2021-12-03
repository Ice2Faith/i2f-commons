package test.velotity;

import i2f.commons.component.template.velocity.VelocityGenerator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestGen {
    public static void main(String[] args) throws IOException {
        String template="ID card\nname:${name}\nage:${age}";
        Map<String,Object> params=new HashMap<>();
        params.put("name","Mr.Zhang");
        params.put("age",22);
        String rs= VelocityGenerator.render(template,params);
        System.out.println("rs:\n"+rs);

    }
}
