package i2f.commons.component.http.axios;


/**
 * @author ltb
 * @date 2022/1/18 16:56
 * @desc
 */
public class Axios {
    private String baseUrl;
    private long timeout;
    public<T> AxiosResp<T> request(AxiosConfig config,Class<T> parseTypeClass)
    {
        throw new RuntimeException("not implement method has been invoked.");
    }
}
