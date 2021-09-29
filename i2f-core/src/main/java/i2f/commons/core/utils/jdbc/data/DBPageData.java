package i2f.commons.core.utils.jdbc.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页数据存放类
 * count：符合分页的数据的总量
 * index:当前数据的页面索引
 * limit:一页数据的最大显示数据量
 * data:数据的存放，根据你的具体类型而定的一个泛型
 * @param <T>
 */
@Data
@NoArgsConstructor
public class DBPageData<T> {
    private int count;
    private int index;
    private int limit;
    private List<T> data;

    public DBPageData(int index, int limit,int count,  List<T> data) {
        this.index = index;
        this.limit = limit;
        this.count = count;
        this.data = data;
    }

}
