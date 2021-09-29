package i2f.commons.component.api.xinhua.dict.data.detail;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class XinHuaDictDetailItem {
    private String id;//ID
    private String zi;//汉字
    private String py;//拼音
    private String wubi;//五笔
    private String pinyin;//带音节拼音
    private String bushou;//部首
    private String bihua;//笔画
    private List<String> jijie;//
    private List<String> xiangjie;//详解
}
