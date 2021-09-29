package i2f.commons.core.data.web.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PageData<T> {
    public List<T> data;
    public Integer index;
    public Integer limit;
    public Integer count;
    public Object tag;


    public PageData(List<T> data, Integer index, Integer limit, Integer count) {
        this.data = data;
        this.index = index;
        this.limit = limit;
        this.count = count;
    }

    public PageData(List<T> data, Integer index, Integer limit, Integer count, Object tag) {
        this.data = data;
        this.index = index;
        this.limit = limit;
        this.count = count;
        this.tag = tag;
    }
}
