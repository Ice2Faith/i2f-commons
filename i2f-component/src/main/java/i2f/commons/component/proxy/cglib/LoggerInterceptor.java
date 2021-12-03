package i2f.commons.component.proxy.cglib;

import i2f.commons.component.proxy.cglib.core.IInvokeInterceptor;
import i2f.commons.core.utils.str.AppendUtil;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

public class LoggerInterceptor implements IInvokeInterceptor {
    private PrintStream print;

    public LoggerInterceptor() {
        print = System.out;
    }

    public LoggerInterceptor(PrintStream print) {
        this.print = print;
    }


    private StringBuilder getBaseInfos(StringBuilder builder, Object obj, Method method, Object[] args) {
        Class clazz = obj.getClass();
        Parameter[] params = method.getParameters();
        AppendUtil.AppendBuilder<StringBuilder> inBuilder= AppendUtil.<StringBuilder>appender(builder);
        inBuilder.adds( "\tClass:", clazz.getSimpleName(), " : ", clazz.getName(), "\n");

        inBuilder.adds( "\t\tMethod:", Modifier.toString(method.getModifiers()), " ", method.getName(), "(");
        for (int i = 0; i < params.length; i++) {
            if (i != 0) {
                inBuilder.add(" , ");
            }
            Parameter item = params[i];
            inBuilder.adds( item.getName(), ":", args[i], "[", item.getType().getSimpleName(), "]");
        }
        inBuilder.add(")\n");

        return inBuilder.target();
    }

    @Override
    public Object beforeIvk(Object obj, Method method, Object[] args) {
        StringBuilder builder = new StringBuilder();

        builder.append("\nlog-ivk-before-begin:\n");
        getBaseInfos(builder, obj, method, args);
        builder.append("log-ivk-before-end:\n");

        print.println(builder.toString());
        return null;
    }

    @Override
    public Object afterIvk(Object retVal, Object obj, Method method, Object[] args) {
        AppendUtil.AppendBuilder<StringBuilder> builder= AppendUtil.builder();

        builder.add("\nlog-ivk-after-begin:\n");
        getBaseInfos(builder.target(), obj, method, args);

        builder.adds( "\t\tReturn:", retVal, "[", method.getReturnType().getName(), "]\n");
        builder.add("log-ivk-after-end:\n");

        print.println(builder.done());
        return retVal;
    }
}
