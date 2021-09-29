package i2f.commons.component.restapi.data.tips;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TipsReqData {
    public static final String URL="https://restapi.amap.com/v3/assistant/inputtips";
    public static final String METHOD="GET";
    private String key;//请求服务权限标识
    private String keywords;//查询关键词
    private String type;//POI分类,服务可支持传入多个分类，多个类型剑用“|”分隔,可选值：POI分类名称、分类代码
    private String location;//坐标,格式：“X,Y”（经度,纬度），不可以包含空格,建议使用location参数，可在此location附近优先返回搜索关键词
    private String city;//搜索城市,可选值：citycode、adcode，不支持县级市。
    private boolean citylimit;//仅返回指定城市数据
    private String datatype;//返回的数据类型,多种数据类型用“|”分隔，可选值：all-返回所有数据类型、poi-返回POI数据类型、bus-返回公交站点数据类型、busline-返回公交线路数据类型
    private String sig;//数字签名
    private String output;//返回数据格式类型,可选值：JSON,XML
    private String callback;//callback值是用户定义的函数名称，此参数只在output=JSON时有效
}
