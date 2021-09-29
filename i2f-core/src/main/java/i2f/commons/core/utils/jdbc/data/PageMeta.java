package i2f.commons.core.utils.jdbc.data;

/**
 * @author ltb
 * @date 2021/9/27
 */
public class PageMeta {
    public Long index;
    public Long size;
    public Long offset;
    public Long offsetEnd;

    public PageMeta(Long index,Long size){
        page(index, size);
    }
    public PageMeta page(Long index,Long size){
        this.index=index;
        this.size=size;
        if(isValid()){
            if(this.index<0L){
                this.index=0L;
            }
            if(this.size<0L){
                this.size=10L;
            }
            this.offset=this.index*this.size;
            this.offsetEnd=(this.index+1)*this.size;
        }
        return this;
    }
    public boolean isValid(){
        return this.index!=null && this.size!=null;
    }
}
