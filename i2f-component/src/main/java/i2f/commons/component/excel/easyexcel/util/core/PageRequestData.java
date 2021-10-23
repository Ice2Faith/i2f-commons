package i2f.commons.component.excel.easyexcel.util.core;

/**
 * @author ltb
 * @date 2021/10/19
 */
public class PageRequestData {
    private Integer index;
    private Integer limit;
    private Integer offsetStart;
    private Integer offsetEnd;
    public PageRequestData(Integer index,Integer limit){
        this.page(index,limit);
    }
    public PageRequestData page(Integer index,Integer limit){
        this.index=index;
        this.limit=limit;
        if(this.index!=null && this.index<0){
            this.index=0;
        }
        if(this.limit!=null && this.limit<0){
            this.limit=30;
        }
        if(legal()){
            this.offsetStart=this.index*this.limit;
            this.offsetEnd=(this.index+1)*this.limit;
        }
        return this;
    }
    public boolean legal(){
        return this.index!=null && this.limit!=null;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffsetStart() {
        return offsetStart;
    }

    public void setOffsetStart(Integer offsetStart) {
        this.offsetStart = offsetStart;
    }

    public Integer getOffsetEnd() {
        return offsetEnd;
    }

    public void setOffsetEnd(Integer offsetEnd) {
        this.offsetEnd = offsetEnd;
    }

    @Override
    public String toString() {
        return "PageRequestData{" +
                "index=" + index +
                ", limit=" + limit +
                ", offsetStart=" + offsetStart +
                ", offsetEnd=" + offsetEnd +
                '}';
    }
}
