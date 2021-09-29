package i2f.commons.component.mybatis;

public interface IMyBatisDao<T,E>{
    E toDo(T dao,Object ... params);
}
