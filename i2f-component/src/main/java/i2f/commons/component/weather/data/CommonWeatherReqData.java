package i2f.commons.component.weather.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommonWeatherReqData {
    public static final String URL="https://api.caiyunapp.com/v2.5/%s/%s/weather.json";
    private String token;
    private String geoLocation;
    public String genUrl(){
        return String.format(URL,token,geoLocation);
    }
}
