package i2f.commons.component.mybatis.page;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import i2f.commons.core.data.web.data.PageData;

import java.util.List;

/**
 * @author ltb
 * @date 2021/8/10
 */
public class PageUtil {
    public static void beginPage(int pageNum,int pageSize){
        PageHelper.startPage(pageNum, pageSize);
    }
    public static PageInfo getPageData(List<?> list){
        return new PageInfo(list);
    }
    public static<T> PageData<T> getData(List<T> list){
        PageInfo<T> pageInfo=new PageInfo<T>(list);
        PageData<T> data=new PageData<T>(pageInfo.getList(),
                pageInfo.getPageNum()-1,
                pageInfo.getPageSize(),
                (int)pageInfo.getTotal());
        return data;
    }
}
