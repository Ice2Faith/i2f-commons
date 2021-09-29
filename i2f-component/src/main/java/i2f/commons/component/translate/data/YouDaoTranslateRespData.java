package i2f.commons.component.translate.data;


import i2f.commons.component.translate.data.detail.YouDaoBasicItem;
import i2f.commons.component.translate.data.detail.YouDaoDictItem;
import i2f.commons.component.translate.data.detail.YouDaoWebDictItem;
import i2f.commons.component.translate.data.detail.YouDaoWebTranslateResultItem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class YouDaoTranslateRespData {
    private List<String> returnPhrase;
    private String query;
    private String errorCode;
    private String l;
    private String tSpeakUrl;
    private List<YouDaoWebTranslateResultItem> web;
    private String requestId;
    private List<String> translation;
    private YouDaoDictItem dict;
    private YouDaoWebDictItem webdict;
    private YouDaoBasicItem basic;
    private boolean isWord;
    private String speakUrl;

    public List<String> getResults(){
        return translation;
    }

    public String getResult(){
        if(translation!=null && translation.size()>0){
            return translation.get(0);
        }
        return "";
    }
}
