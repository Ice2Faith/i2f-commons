package i2f.commons.core.utils.sys;

import i2f.commons.core.data.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2021/11/9
 */
public class CmdArgs {
    private volatile String[] args;
    public void setArgs(String[] args){
        if(args==null){
            this.args=new String[0];
            return;
        }
        this.args=new String[args.length];
        for(int i=0;i< args.length;i+=1){
            this.args[i]=args[i];
        }
    }
    public String[] getArgs(){
        String[] ret=new String[this.args.length];
        for(int i=0;i<this.args.length;i+=1){
            ret[i]=this.args[i];
        }
        return ret;
    }

    /**
     * 是否存在选项命令
     * arg示例：
     * -r
     * /f
     * -rf
     * aac
     * @param arg
     * @param ignoreCase
     * @return
     */
    public boolean exist(String arg,boolean ignoreCase){
        if(arg==null){
            return false;
        }
        if(ignoreCase){
            arg=arg.toLowerCase();
        }
        for(String item : this.args){

            if(ignoreCase){
                item=item.toLowerCase();
            }
            if(item.equals(arg)){
                return true;
            }
        }
        return false;
    }

    /**
     * 获取以指定前缀的参数列表
     * prefix示例：
     * -
     * /
     * --
     * @param prefix
     * @param ignoreCase
     * @return
     */
    public List<String> options(String prefix,boolean ignoreCase){
        if(prefix==null){
            prefix="-";
        }
        if(ignoreCase){
            prefix=prefix.toLowerCase();
        }
        List<String> ret=new ArrayList<>();
        for(String item : this.args){
            if(ignoreCase){
                item=item.toLowerCase();
            }
            if(item.startsWith(prefix)){
                ret.add(item);
            }
        }

        return ret;
    }

    /**
     * 获取匹配正则表达式的参数列表
     * @param regex
     * @param ignoreCase
     * @return
     */
    public List<String> optionsMatch(String regex,boolean ignoreCase){
        if(regex==null){
            regex="-.*";
        }
        List<String> ret=new ArrayList<>();
        for(String item : this.args){
            if(ignoreCase){
                item=item.toLowerCase();
            }
            if(item.matches(regex)){
                ret.add(item);
            }
        }

        return ret;
    }

    /**
     * 选项中是否存在某个控制字符
     * 选项指定前缀prefix
     * 则会在以prefix为前缀的所有选项中，查找是否包含字符ch
     * 简单来说，例如：
     * rm -rf命令中的
     * -rf命令参数，
     * 因此以-为prefix,存在r和f选项控制
     * 也就是为了满足此类需求
     * @param prefix
     * @param ch
     * @param ignoreCase
     * @return
     */
    public boolean existOption(String prefix,char ch,boolean ignoreCase){
        if(ignoreCase){
            ch=Character.toLowerCase(ch);
        }
        List<String> opts=options(prefix,ignoreCase);
        for(String item : opts){
            if(item.indexOf(ch)>=0){
                return true;
            }
        }
        return false;
    }

    /**
     * 获取与选项紧密绑定的值
     * 例如：
     * -hei480
     * 则可以prefix=-,optionName=hei
     * 则返回hei:480
     * @param prefix
     * @param optionName
     * @return
     */
    public List<Pair<String,String>> optionValue(String prefix, String optionName, boolean ignoreCase){
        String npfx=prefix+optionName;
        List<String> opts=options(npfx,ignoreCase);
        List<Pair<String,String>> ret=new ArrayList<Pair<String,String>>();
        for(String item : opts){
            Pair<String,String> pair=new Pair<>();
            pair.key=optionName;
            pair.val=item.substring(npfx.length());
            ret.add(pair);
        }
        return ret;
    }

}
