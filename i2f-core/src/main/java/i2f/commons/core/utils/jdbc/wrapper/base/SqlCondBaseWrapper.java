package i2f.commons.core.utils.jdbc.wrapper.base;


import i2f.commons.core.data.Pair;
import i2f.commons.core.data.Triple;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class SqlCondBaseWrapper extends SqlBaseWrapper{
    //and|or colOpe value:and,age,12
    public List<Triple<String,String,Object>> whereConditions=new ArrayList<Triple<String,String,Object>>();
    //and|or col vals:and,age,{1,2,3,4}
    public List<Triple<String,String,Object[]>> inWhereCondition=new ArrayList<Triple<String,String,Object[]>>();
    //and|or col vals:and,name,{"a","b"}
    public List<Triple<String,String,Object[]>> likeWhereCondition=new ArrayList<Triple<String,String,Object[]>>();
    //and|or condition:and age<50
    public List<Pair<String,String>> freeWhereCondition=new ArrayList<Pair<String,String>>();
    //and|or colValMap:and,{"name":{"a","b"},"introduce":{"a","b"}}
    public List<Pair<String, Map<String,Object[]>>> multiLikeWhereCondition=new ArrayList<Pair<String, Map<String,Object[]>>>();
}
