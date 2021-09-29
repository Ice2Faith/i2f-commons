package i2f.commons.core.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BinTreeNode<T> {
    private T data;
    private Object tag;
    public BinTreeNode<T> parent;
    public BinTreeNode<T> lchild;
    public BinTreeNode<T> rchild;
    public BinTreeNode<T> asLeftChild(BinTreeNode<T> node){
        BinTreeNode<T> slchild=lchild;
        this.lchild=node;
        node.parent=this;
        return slchild;
    }
    public BinTreeNode<T> asRightChild(BinTreeNode<T> node){
        BinTreeNode<T> srchild=rchild;
        this.rchild=node;
        node.parent=this;
        return srchild;
    }
    public BinTreeNode<T> cleanLink(){
        this.parent=null;
        this.lchild=null;
        this.rchild=null;
        return this;
    }
}
