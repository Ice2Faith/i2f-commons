package i2f.commons.core.utils.reflect.core.impl;


import i2f.commons.core.utils.reflect.core.Bean2MapFieldConvertor;

public class StringBean2MapFieldConvertor implements Bean2MapFieldConvertor<Object,String> {
    @Override
    public String convert(Object obj) {
        return String.valueOf(obj);
    }
}
