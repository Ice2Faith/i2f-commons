package test;

import i2f.commons.core.utils.data.ConvertUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2021/9/29
 */
public class List2TreeTest {
    public static void main(String[] args){
        List<TreeItem> list=new ArrayList<>();
        list.add(new TreeItem(-1,-999));
        list.add(new TreeItem(1,-1));
        list.add(new TreeItem(2,-1));
        list.add(new TreeItem(3,-1));
        list.add(new TreeItem(4,-1));
        list.add(new TreeItem(5,-1));
        list.add(new TreeItem(6,-1));
        list.add(new TreeItem(7,-1));
        list.add(new TreeItem(8,-1));
        list.add(new TreeItem(9,-1));
        list.add(new TreeItem(10,1));
        list.add(new TreeItem(11,1));
        list.add(new TreeItem(12,1));
        list.add(new TreeItem(13,1));
        list.add(new TreeItem(14,1));
        list.add(new TreeItem(20,2));
        list.add(new TreeItem(21,2));
        list.add(new TreeItem(22,2));
        list.add(new TreeItem(101,10));
        list.add(new TreeItem(102,10));
        list.add(new TreeItem(103,10));
        list.add(new TreeItem(104,10));
        list.add(new TreeItem(105,10));
        list.add(new TreeItem(1021,102));
        list.add(new TreeItem(1022,102));
        list.add(new TreeItem(1023,102));
        list.add(new TreeItem(1024,102));
        list.add(new TreeItem(1025,102));
        list.add(new TreeItem(1026,102));


        List<TreeItem> tree= ConvertUtil.list2Tree(list);


    }


}
