package i2f.commons.core.data.functions;

import i2f.commons.core.data.interfaces.IExecute;
import i2f.commons.core.data.interfaces.IFilter;
import i2f.commons.core.data.interfaces.IMap;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author ltb
 * @date 2021/10/18
 */
public class Func {
    /**
     * 迭代可迭代对象
     * @param it 可迭代对象
     * @param execute 执行器
     * @param <T>
     */
    public static<T> void each(Iterable<T> it, IExecute<T> execute){
        Iterator<T> itr=it.iterator();
        while(itr.hasNext()){
            T cur=itr.next();
            execute.exec(cur);
        }
    }

    /**
     * 重新映射可迭代对象到一个新的集合中
     * @param col 新的集合对象
     * @param it 可迭代对象
     * @param mapper 映射器
     * @param <R> 一个Collection的子对象
     * @param <T> 原始元素数据类型
     * @param <E> 新数据数据类型
     * @return
     */
    public static<R extends Collection<E>,T,E> R map(R col, Iterable<T> it, IMap<T,E> mapper){
        Iterator<T> itr=it.iterator();
        while(itr.hasNext()){
            T cur=itr.next();
            E val=mapper.map(cur);
            col.add(val);
        }
        return col;
    }

    /**
     * 过滤可迭代对象到新的集合中
     * @param col 新的集合
     * @param it 可迭代对象
     * @param filter 过滤器
     * @param <T> 集合Collection的子对象
     * @param <E> 数据类型
     * @return
     */
    public static<T extends Collection<E>,E> T filter(T col, Iterable<E> it, IFilter<E> filter){
        Iterator<E> itr=it.iterator();
        while(itr.hasNext()){
            E cur=itr.next();
            if(filter.choice(cur)){
                col.add(cur);
            }
        }
        return col;
    }

    /**
     * 对可迭代对象进行系列操作
     * @param col 保存新对象的集合，nullable
     * @param it 可迭代对象
     * @param filter 过滤器，nullable
     * @param mapper 映射器
     * @param executor 执行器，nullable
     * @param <R> 新集合Collection的子对象
     * @param <T> 原始元素类型
     * @param <E> 新元素类型
     * @return col
     */
    public static<R extends Collection<E>,T,E> R process(R col,Iterable<T> it,IFilter<T> filter,IMap<T,E> mapper,IExecute<E> executor){
        Iterator<T> itr=it.iterator();
        while(itr.hasNext()){
            T cur=itr.next();
            if(filter!=null){
                if(!filter.choice(cur)){
                    continue;
                }
            }
            E nval= mapper.map(cur);
            if(col!=null){
                col.add(nval);
            }
            if(executor!=null){
                executor.exec(nval);
            }
        }
        return col;
    }
}
