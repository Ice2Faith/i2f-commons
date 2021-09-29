package i2f.commons.core.utils.data;

import i2f.commons.core.utils.data.interfaces.ITreeNode;

import java.util.ArrayList;
import java.util.List;

public class ConvertUtil {
    public static Integer s2i(String num){
        return Integer.parseInt(num);
    }
    public static Integer s2ix(String num,int base){
        return Integer.parseInt(num,base);
    }
    public static Double s2d(String num){
        return Double.parseDouble(num);
    }
    public static Long s2l(String num){
        return Long.parseLong(num);
    }
    public static Float s2f(String num){
        return  Float.parseFloat(num);
    }


    public static final long SIZE_K =1024l;
    public static final long SIZE_M =1024l*1024l;
    public static final long SIZE_G =1024l*1024l*1024l;
    public static final long SIZE_T =1024l*1024l*1024l*1024l;
    public static String size2Str(long size){
        if(size< SIZE_K){
            return String.format("%dbyte",size);
        }
        if(size< SIZE_M){
            return String.format("%.2fKb",size/(double) SIZE_K);
        }
        if(size< SIZE_G){
            return String.format("%.2fMb",size/(double) SIZE_M);
        }
        if(size< SIZE_T){
            return String.format("%.2fGb",size/(double) SIZE_G);
        }
        return String.format("%.2fTb",size/(double) SIZE_G /(double) SIZE_K);
    }

    public static <T extends ITreeNode> List<T> list2Tree(List<T> list){
        int size= list.size();
        List<T> root=new ArrayList<>(size);
        List<T> children=new ArrayList<>(size);
        for(T item : list){
            boolean isRoot=true;
            for(T mit : list){
                if (item.isMyParent(mit)) {
                 isRoot=false;
                 break;
                }
            }
            if(isRoot){
                root.add(item);
            }else{
                children.add(item);
            }
        }


        if(children.size()>0){
            list2TreeNext(root,children);
        }
        return root;
    }

    private static<T extends ITreeNode> void list2TreeNext(List<T> root,List<T> children){
        for(T item : root){
            List<T> curRoot=new ArrayList<>(root.size());
            List<T> curChildren=new ArrayList<>(children.size());
            for(T mit : children){
                if(item.isMyChild(mit)){
                    item.asMyChild(mit);
                    curRoot.add(mit);
                }else{
                    curChildren.add(mit);
                }
            }
            if(curChildren.size()>0) {
                list2TreeNext(curRoot, curChildren);
            }
        }
    }
}
