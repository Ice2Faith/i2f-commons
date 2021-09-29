package i2f.commons.core.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ListNode<T> {
    private T data;
    private Object tag;
    public ListNode<T> previous;
    public ListNode<T> next;
    public ListNode<T> asNext(ListNode<T> node){
        ListNode<T> snext=next;
        this.next=node;
        node.previous=this;
        return snext;
    }
    public ListNode<T> asPrevious(ListNode<T> node){
        ListNode<T> sprevious=previous;
        this.previous=node;
        node.next=this;
        return sprevious;
    }
    public ListNode<T> cleanLink(){
        this.previous=null;
        this.next=null;
        return this;
    }
}
