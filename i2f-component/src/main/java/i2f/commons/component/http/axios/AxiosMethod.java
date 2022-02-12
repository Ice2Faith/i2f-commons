package i2f.commons.component.http.axios;

/**
 * @author ltb
 * @date 2022/1/18 17:00
 * @desc
 */
public enum AxiosMethod {
    GET("get"),POST("post"),PUT("put"),DELETE("delete");
    private String text;
    private AxiosMethod(String text){
        this.text=text;
    }
    public String text(){
        return this.text;
    }
}
