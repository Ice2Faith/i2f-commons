package test;

import i2f.commons.core.utils.data.interfaces.ITreeNode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2021/9/29
 */
@Data
@NoArgsConstructor
public class TreeItem implements ITreeNode<TreeItem> {
    public int id;
    public int parentId;
    public TreeItem(int id,int parentId){
        this.id=id;
        this.parentId=parentId;
    }

    public List<TreeItem> children;
    @Override
    public void asMyChild(TreeItem val) {
        if(children==null){
            children=new ArrayList<>();
        }
        children.add(val);
    }

    @Override
    public boolean isMyChild(TreeItem val) {
        return val.parentId==this.id;
    }

    @Override
    public boolean isMyParent(TreeItem val) {
        return this.parentId==val.id;
    }
}
