package i2f.commons.core.utils.pkg.data;


import i2f.commons.core.utils.pkg.core.PackageBaseScanner;
import i2f.commons.core.utils.pkg.filter.IClassFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2021/9/24
 */
public class ClassMetaTree {
    public static volatile boolean USE_PREPARE_DATA=true;
    private List<ClassMetaData> sumList;
    private ClassMetaTreeNode rootTree;
    public ClassMetaTree(List<ClassMetaData> list){
        initTree(list);
    }
    public ClassMetaTree reload(List<ClassMetaData> list){
        initTree(list);
        return this;
    }
    private void initTree(List<ClassMetaData> list){
        this.sumList=list;
        ClassMetaTreeNode tree=new ClassMetaTreeNode("#");
        for(ClassMetaData item : list){
            appendToTree(tree,item);
        }
        if(USE_PREPARE_DATA) {
            buildTreeData(tree);
        }
        this.rootTree=tree;
    }

    private void appendToTree(ClassMetaTreeNode tree, ClassMetaData data){
        String[] parts=data.getClassName().split("\\.");
        ClassMetaTreeNode cur=tree;
        int i=0;
        while(i< parts.length){
            String pkg=parts[i];
            if(i!= parts.length-1){
                if(cur.children==null){
                    cur.children=new HashMap<>(32);
                }
                if(cur.children.containsKey(pkg)){
                    cur=cur.children.get(pkg);
                }else{
                    ClassMetaTreeNode node=new ClassMetaTreeNode(pkg);
                    cur.asChild(node);
                    cur=node;
                }
            }else{
                if(cur.data==null){
                    cur.data=new HashMap<>(32);
                }
                cur.data.put(pkg,data);
            }
            i++;
        }
    }

    public List<ClassMetaData> fetchBasePackages(IClassFilter filter, String ... basePackages){
        if(basePackages==null || basePackages.length==0){
            return sumList;
        }
        ClassMetaTreeNode tree=this.rootTree;
        List<ClassMetaData> list=new ArrayList<>(sumList.size());
        List<String> basePacks= PackageBaseScanner.getShortlyPrefixes(basePackages);
        for(String item : basePacks){
            fetchBasePackage(filter,tree,item,list);
        }
        return list;
    }


    private void fetchBasePackage(IClassFilter filter, ClassMetaTreeNode tree, String pack, List<ClassMetaData> list){
        ClassMetaTreeNode cur=tree;
        String[] parts=pack.split("\\.");
        int i=0;
        while(i< parts.length){
            String pkg=parts[i];
            if(cur.children!=null){
                if(cur.children.containsKey(pkg)){
                    cur=cur.children.get(pkg);
                }else{
                    cur=null;
                    break;
                }
            }else{
                cur=null;
                break;
            }
            i++;
        }
        if(cur!=null){
            getAllInTree(filter,cur,list);
        }
    }

    private void getAllInTree(IClassFilter filter, ClassMetaTreeNode tree, List<ClassMetaData> list){
        if(USE_PREPARE_DATA) {
            List<ClassMetaData> datas = tree.treeData;
            long startTime = System.currentTimeMillis();
            if(filter==null){
                list.addAll(datas);
                return;
            }
            for (ClassMetaData item : datas) {
                if (filter.save(item)) {
                    list.add(item);
                }
            }

            return;
        }
        if(tree.children!=null){
            for(String item : tree.children.keySet()){
                getAllInTree(filter,tree.children.get(item),list);
            }
        }
        if(tree.data!=null){
            long startTime=System.currentTimeMillis();
            for(Map.Entry<String, ClassMetaData> item : tree.data.entrySet()){
                ClassMetaData data=item.getValue();
                if(filter==null || filter.save(data)){
                    list.add(data);
                }
            }
        }
    }

    private void getAllInTreeDataList(IClassFilter filter, ClassMetaTreeNode tree, List<ClassMetaData> list){
        if(tree.children!=null){
            for(String item : tree.children.keySet()){
                getAllInTreeDataList(filter,tree.children.get(item),list);
            }
        }
        if(tree.data!=null){
            for(Map.Entry<String, ClassMetaData> item : tree.data.entrySet()){
                ClassMetaData data=item.getValue();
                if(filter==null || filter.save(data)){
                    list.add(data);
                }
            }
        }
    }

    private void buildTreeData(ClassMetaTreeNode tree){
        if(tree.treeData==null){
            tree.treeData=new ArrayList<>(256);
        }
        if(tree.children!=null){
            for(Map.Entry<String, ClassMetaTreeNode> item : tree.children.entrySet()){
                buildTreeData(item.getValue());
            }
        }
        getAllInTreeDataList(null,tree,tree.treeData);

    }
}
