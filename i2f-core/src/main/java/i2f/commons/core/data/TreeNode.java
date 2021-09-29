package i2f.commons.core.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
public class TreeNode<T> {
    private T data;
    private Object tag;
    private TreeNode<T> parent;
    private List<TreeNode<T>> children=new LinkedList<TreeNode<T>>();
    public void asChild(TreeNode<T> node){
        this.children.add(node);
        node.parent=this;
    }
    public TreeNode<T> cleanLink(){
        this.children.clear();
        this.parent=null;
        return this;
    }
}
