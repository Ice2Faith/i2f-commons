package i2f.commons.core.utils.str;


import java.io.File;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;

public class AppendUtil {

    public static String str(Object... objs) {
        return new AppendBuilder(new StringBuffer()).adds(objs).done();
    }

    public static String strSep(Object separator, Object... objs) {
        return new AppendBuilder(new StringBuffer()).addsSep(separator, objs).done();
    }

    public static String strArr(Object arr) {
        return new AppendBuilder(new StringBuffer()).addArray(arr).done();
    }

    public static String strCollection(Collection collection) {
        return new AppendBuilder(new StringBuffer()).addCollection(collection).done();
    }

    public static String strMap(Map map) {
        return new AppendBuilder(new StringBuffer()).addMap(map).done();
    }

    public static <T extends Appendable> AppendBuilder appender(T appender) {
        return new AppendBuilder(appender);
    }

    public static AppendBuilder builder() {
        return new AppendBuilder(new StringBuilder());
    }

    public static AppendBuilder buffer() {
        return new AppendBuilder(new StringBuffer());
    }

    public static AppendBuilder builder(String separator, Object... objs) {
        return new AppendBuilder(new StringBuilder()).addsSep(separator, objs);
    }

    public static AppendBuilder buffer(String separator, Object... objs) {
        return new AppendBuilder(new StringBuffer()).addsSep(separator, objs);
    }

    public static <T extends Appendable> T headAdd(T appender, boolean condition, Object obj) {
        if (appender == null) {
            return appender;
        }
        String str = appender.toString();
        setLength(appender, 0);
        add(appender, condition, 1, obj);
        add(appender, str);
        return appender;
    }

    public static <T extends Appendable> T setLength(T appender, int length) {
        if (appender == null || length < 0) {
            return appender;
        }
        if (appender instanceof StringBuilder) {
            StringBuilder builder = (StringBuilder) appender;
            builder.setLength(length);
        } else if (appender instanceof StringBuffer) {
            StringBuffer buffer = (StringBuffer) appender;
            buffer.setLength(length);
        }
        return appender;
    }

    public static <T extends Appendable> T add(T appender, Object obj) {
        if (appender == null) {
            return appender;
        }
        if (appender instanceof StringBuilder) {
            StringBuilder builder = (StringBuilder) appender;
            builder.append(obj);
        } else if (appender instanceof StringBuffer) {
            StringBuffer buffer = (StringBuffer) appender;
            buffer.append(obj);
        }
        return appender;
    }

    public static <T extends Appendable> T add(T appender, boolean condition, long count, Object obj) {
        if (appender == null) {
            return appender;
        }
        if (!condition) {
            return appender;
        }
        if (count <= 0) {
            return appender;
        }
        for (long i = 0; i < count; i += 1) {
            add(appender, obj);
        }
        return appender;
    }

    public static <T extends Appendable> T adds(T appender, boolean trim, boolean line, Object separator, Object prefix, Object suffix, Object... objs) {
        if (trim) {
            if (objs == null || objs.length == 0) {
                return appender;
            }
        }
        if (prefix != null) {
            add(appender, prefix);
        }
        boolean isFirst = true;
        for (Object item : objs) {
            if (!isFirst) {
                if (separator != null) {
                    add(appender, separator);
                }
            }
            add(appender, item);
            isFirst = false;
        }
        if (suffix != null) {
            add(appender, suffix);
        }
        if (line) {
            add(appender, System.lineSeparator());
        }
        return appender;
    }

    public static <T extends Appendable> T addArray(T appender, boolean trim, Object separator, Object prefix, Object suffix, Object arr) {
        if (arr != null && arr.getClass().isArray()) {
            List<Object> list = new ArrayList<>();
            int len = Array.getLength(arr);
            for (int i = 0; i < len; i += 1) {
                list.add(Array.get(arr, i));
            }
            addCollection(appender, trim, separator, prefix, suffix, list);
        } else {
            add(appender, arr != null, 1, arr);
        }
        return appender;
    }

    public static <T extends Appendable> T addCollection(T appender, boolean trim, Object separator, Object prefix, Object suffix, Collection collection) {
        if (trim) {
            if (collection == null || collection.size() == 0) {
                return appender;
            }
        }
        if (prefix != null) {
            add(appender, prefix);
        }
        boolean isFirst = true;
        for (Object item : collection) {
            if (!isFirst) {
                if (separator != null) {
                    add(appender, separator);
                }
            }
            add(appender, item);
            isFirst = false;
        }
        if (suffix != null) {
            add(appender, suffix);
        }
        return appender;
    }

    public static <T extends Appendable> T addMap(T appender, boolean trim, Object itemSeparator, Object prefix, Object suffix, Object kvSeparator, Map map) {
        if (trim) {
            if (map == null || map.size() == 0) {
                return appender;
            }
        }
        if (prefix != null) {
            add(appender, prefix);
        }
        boolean isFirst = true;
        for (Object item : map.keySet()) {
            if (!isFirst) {
                if (itemSeparator != null) {
                    add(appender, itemSeparator);
                }
            }
            add(appender, item);
            if (kvSeparator != null) {
                add(appender, kvSeparator);
            }
            add(appender, map.get(item));
            isFirst = false;
        }
        if (suffix != null) {
            add(appender, suffix);
        }
        return appender;
    }

    public static <T extends Appendable> T addCollection(T appender, IAppendMap mapper,Object[] args, boolean trim, Object separator, Object prefix, Object suffix, Collection collection) {
        if (trim) {
            if (collection == null || collection.size() == 0) {
                return appender;
            }
        }
        if (prefix != null) {
            add(appender, prefix);
        }
        boolean isFirst = true;
        for (Object item : collection) {
            if (!isFirst) {
                if (separator != null) {
                    add(appender, separator);
                }
            }
            if(mapper!=null){
                mapper.append(item,appender,args);
            }else{
                add(appender, item);
            }
            isFirst = false;
        }
        if (suffix != null) {
            add(appender, suffix);
        }
        return appender;
    }

    public static class AppendBuilder<T extends Appendable> {
        private T appender;

        public AppendBuilder(T appender) {
            this.appender = appender;
        }

        public AppendBuilder<T> sp() {
            AppendUtil.add(appender, true, 1, ' ');
            return this;
        }

        public AppendBuilder<T> tab() {
            AppendUtil.add(appender, true, 1, '\t');
            return this;
        }

        public AppendBuilder<T> line() {
            AppendUtil.add(appender, System.lineSeparator());
            return this;
        }

        public AppendBuilder<T> sp(int count) {
            AppendUtil.add(appender, true, count, ' ');
            return this;
        }

        public AppendBuilder<T> tab(int count) {
            AppendUtil.add(appender, true, count, '\t');
            return this;
        }

        public AppendBuilder<T> line(int count) {
            AppendUtil.add(appender, true, count, System.lineSeparator());
            return this;
        }

        public AppendBuilder<T> pathSep() {
            AppendUtil.add(appender, File.pathSeparator);
            return this;
        }

        public AppendBuilder<T> urlSep() {
            AppendUtil.add(appender, '/');
            return this;
        }

        public AppendBuilder<T> addDate(Date date, String format) {
            AppendUtil.add(appender, new SimpleDateFormat(format).format(date));
            return this;
        }

        public AppendBuilder<T> addFormatStr(String format, Object... vals) {
            AppendUtil.add(appender, String.format(format, vals));
            return this;
        }

        public AppendBuilder<T> add(Object obj) {
            AppendUtil.add(appender, obj);
            return this;
        }

        public AppendBuilder<T> headAdd(Object obj) {
            AppendUtil.headAdd(appender, true, obj);
            return this;
        }

        public AppendBuilder<T> headAdd(boolean condition, Object obj) {
            AppendUtil.headAdd(appender, condition, obj);
            return this;
        }

        public AppendBuilder<T> ensureEndWith(String str) {
            if (!appender.toString().endsWith(str)) {
                AppendUtil.add(appender, str);
            }
            return this;
        }

        public AppendBuilder<T> ensureStartWith(String str) {
            if (!appender.toString().startsWith(str)) {
                AppendUtil.headAdd(appender, true, str);
            }
            return this;
        }

        public AppendBuilder<T> setLength(int length) {
            AppendUtil.setLength(appender, length);
            return this;
        }

        public AppendBuilder<T> repeat(long count, Object obj) {
            AppendUtil.add(appender, true, count, obj);
            return this;
        }

        public AppendBuilder<T> addWhen(boolean condition, Object obj) {
            AppendUtil.add(appender, condition, 1, obj);
            return this;
        }


        public AppendBuilder<T> adds(Object... objs) {
            AppendUtil.adds(appender, false, false, null, null, null, objs);
            return this;
        }

        public AppendBuilder<T> addsWhen(boolean condition, Object... objs) {
            if (condition) {
                return adds(objs);
            }
            return this;
        }

        public AppendBuilder<T> addsLine(Object... objs) {
            AppendUtil.adds(appender, false, true, null, null, null, objs);
            return this;
        }

        public AppendBuilder<T> addsLineWhen(boolean condition, Object... objs) {
            if (condition) {
                return addsLine(objs);
            }
            return this;
        }

        public AppendBuilder<T> addsSep(Object separator, Object... objs) {
            AppendUtil.adds(appender, false, false, separator, null, null, objs);
            return this;
        }

        public AppendBuilder<T> addsSepWhen(boolean condition, Object separator, Object objs) {
            if (condition) {
                return addsSep(separator, objs);
            }
            return this;
        }

        public AppendBuilder<T> addsSepLine(Object separator, Object... objs) {
            AppendUtil.adds(appender, false, true, separator, null, null, objs);
            return this;
        }

        public AppendBuilder<T> addsSepLineWhen(boolean condition, Object separator, Object... objs) {
            if (condition) {
                return addsSepLine(separator, objs);
            }
            return this;
        }

        public AppendBuilder<T> add(boolean condition, long count, Object obj) {
            AppendUtil.add(appender, condition, count, obj);
            return this;
        }

        public AppendBuilder<T> appends(boolean trim, boolean line, Object separator, Object prefix, Object suffix, Object... objs) {
            AppendUtil.adds(appender, trim, line, separator, prefix, suffix, objs);
            return this;
        }

        public AppendBuilder<T> appendsWhen(boolean condition, boolean trim, boolean line, Object separator, Object prefix, Object suffix, Object... objs) {
            if (condition) {
                return appends(trim, line, separator, prefix, suffix, objs);
            }
            return this;
        }

        public AppendBuilder<T> addArray(Object arr) {
            return addArray(false, ",", "[", "]", arr);
        }

        public AppendBuilder<T> addArrayWhen(boolean condition, Object arr) {
            if (condition) {
                addArray(arr);
            }
            return this;
        }

        public AppendBuilder<T> addArray(boolean trim, Object separator, Object prefix, Object suffix, Object arr) {
            AppendUtil.addArray(appender, trim, separator, prefix, suffix, arr);
            return this;
        }

        public AppendBuilder<T> addArrayWhen(boolean condition, boolean trim, Object separator, Object prefix, Object suffix, Object arr) {
            if (condition) {
                AppendUtil.addArray(appender, trim, separator, prefix, suffix, arr);
            }
            return this;
        }

        public AppendBuilder<T> addCollection(Collection collection) {
            return addCollection(false, ",", "[", "]", collection);
        }

        public AppendBuilder<T> addCollection(IAppendMap mapper,Object[] args, Collection collection) {
            return addCollection(mapper,args,false, ",", "[", "]", collection);
        }

        public AppendBuilder<T> addCollectionWhen(boolean condition, Collection collection) {
            if (condition) {
                addCollection(collection);
            }
            return this;
        }

        public AppendBuilder<T> addCollection(boolean trim, Object separator, Object prefix, Object suffix, Collection collection) {
            AppendUtil.addCollection(appender, trim, separator, prefix, suffix, collection);
            return this;
        }
        public  AppendBuilder<T> addCollection(IAppendMap mapper,Object[]  args, boolean trim, Object separator, Object prefix, Object suffix, Collection collection) {
            AppendUtil.addCollection(appender,mapper,args,trim,separator,prefix,suffix,collection);
            return this;
        }

            public AppendBuilder<T> addCollectionWhen(boolean condition, boolean trim, Object separator, Object prefix, Object suffix, Collection collection) {
            if (condition) {
                return addCollection(trim, separator, prefix, suffix, collection);
            }
            return this;
        }

        public AppendBuilder<T> addMap(Map map) {
            return addMap(false, ",", "{", "}", ":", map);
        }

        public AppendBuilder<T> addMapWhen(boolean condition, Map map) {
            if (condition) {
                return addMap(map);
            }
            return this;
        }

        public AppendBuilder<T> addMap(boolean trim, Object itemSeparator, Object prefix, Object suffix, Object kvSeparator, Map map) {
            AppendUtil.addMap(appender, trim, itemSeparator, prefix, suffix, kvSeparator, map);
            return this;
        }

        public AppendBuilder<T> addMapWhen(boolean condition, boolean trim, Object itemSeparator, Object prefix, Object suffix, Object kvSeparator, Map map) {
            if (condition) {
                return addMap(trim, itemSeparator, prefix, suffix, kvSeparator, map);
            }
            return this;
        }

        public T target() {
            return appender;
        }

        public String done() {
            return appender.toString();
        }
    }
}
