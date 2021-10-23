package test;

import i2f.tools.FormulaCalculator;

import java.lang.reflect.Method;

/**
 * @author ltb
 * @date 2021/10/18
 */
public class TestCache {
    public static void main(String[] args) throws NoSuchMethodException {
        String formula="3+2*5-4/2 ";
        FormulaCalculator calculator=new FormulaCalculator();
        double val=calculator.calculate(formula,10);
        System.out.println(formula+" = "+val);

        String className=TestCache.class.getName();
        String methodNameRegex="main.*";
        className=className.replaceAll("\\.","\\\\.");
        String patten="class\\("+className+"\\)method\\(.*\\."+methodNameRegex+"\\(.*\\).*\\).*";
        Method method=null;
        Class clazz=TestCache.class;
        method=clazz.getDeclaredMethod("main",String[].class);
        String key="class("+clazz.getName()+")method("+method.toGenericString()+")";

        System.out.println("key:\n\t"+key);
        System.out.println("patten:\n\t"+patten);

        System.out.println("match:"+(key.matches(patten)));
    }

}
