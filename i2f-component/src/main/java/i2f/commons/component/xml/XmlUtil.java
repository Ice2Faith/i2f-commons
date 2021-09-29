package i2f.commons.component.xml;

import org.dom4j.*;

public class XmlUtil {
    public static Document parse(String text) throws DocumentException {
        return DocumentHelper.parseText(text);
    }

    /**
     * 获取文档中指定节点下的元素，没有找到返回null
     * @param doc 文档
     * @param path 查找路径，每集路径按照.分隔，例如：
     * @return
     */
    public static Element elem(Document doc,String path){
        Element root= doc.getRootElement();
        String[] arr=path.split("\\.");
        Element ret=root;
        int idx=0;
        if(arr.length>0 && ret.getName().equals(arr[0])){
            idx=1;
        }
        for(int i=idx;i< arr.length;i++){
            if(ret==null){
                break;
            }
            ret=ret.element(arr[i]);
        }
        return ret;
    }

    public static String elemVal(Document doc,String path){
        Element el=elem(doc, path);
        if(el!=null){
            return el.getStringValue();
        }
        return null;
    }
    public static Attribute attr(Document doc, String path, String attrName){
        Element el=elem(doc,path);
        if(el!=null){
            return el.attribute(attrName);
        }
        return null;
    }
    public static String attrVal(Document doc,String path,String attrName){
        Attribute at=attr(doc, path, attrName);
        if(at!=null){
            return at.getValue();
        }
        return null;
    }

}
