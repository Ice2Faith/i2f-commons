package i2f.commons.component.http.axios;

/**
 * @author ltb
 * @date 2022/1/18 16:59
 * @desc
 */
public class AxiosResp<T> {
    public String respText;
    public T respObj;
    public AxiosHeader headers;
    public String statusLine;
    public int status;
}
