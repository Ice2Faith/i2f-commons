package i2f.commons.core.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
public class MapNode<T> {
    private T data;
    private Object tag;
    private List<MapNode<T>> outs=new LinkedList<MapNode<T>>();
    private List<MapNode<T>> ins=new LinkedList<MapNode<T>>();
    public void asIn(MapNode<T> node){
        ins.add(node);
        node.outs.add(this);
    }
    public void asOut(MapNode<T> node){
        outs.add(node);
        node.ins.add(this);
    }
    public MapNode<T> cleanLink(){
        this.outs.clear();
        this.ins.clear();
        return this;
    }
}
