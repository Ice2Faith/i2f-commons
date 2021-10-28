package i2f.commons.core.utils.generator.regex.core.impl;

import i2f.commons.core.data.interfaces.IMap;
import i2f.commons.core.utils.generator.regex.core.IGenerate;
import i2f.commons.core.utils.reflect.core.resolver.base.ClassResolver;
import lombok.Data;

import java.util.List;

/**
 * @author ltb
 * @date 2021/10/28
 */
@Data
public class ValGenerate implements IGenerate {
    public IMap<Object,String> mapper;
    public Object root;
    public Object data;
    public List<String> basePackages;
    public String userMapper;

    @Override
    public String gen() {
        IMap<Object,String> currentMapper=getCurrentMapper();
        return currentMapper.map(data);
    }

    private IMap<Object,String> getCurrentMapper(){
        IMap<Object,String> ret=this.mapper;
        if(userMapper!=null && !"".equals(userMapper)){
            try{
                Class clazz= ClassResolver.getClazz(this.userMapper);
                if(clazz!=null){
                    ret=(IMap<Object, String>)ClassResolver.instance(clazz);
                }
            }catch(Exception e){

            }
        }
        return ret;
    }
}
