package i2f.commons.core.utils.data.id;

import i2f.commons.core.utils.safe.CheckUtil;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RegionMap {
    public static final String REGION_MAP_LOCATION="docs/regionMap.txt";
    public static Map<String,String> regionMap=new HashMap<>();
    static {
        try {
            URL url=RegionMap.class.getClassLoader().getResource(REGION_MAP_LOCATION);
            regionMap=generateMap(url.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String decode(String regionCode){
        return regionMap.get(regionCode);
    }
    public static Map<String,String> generateMap(String file) throws IOException {
        Map<String,String> map=new HashMap<>();

        BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line="";
        while((line= reader.readLine())!=null){
            line=line.trim();
            if(CheckUtil.isEmptyStr(line,false)){
                continue;
            }
            String[] arr=line.split("\\s+",2);
            if(arr.length>=2){
                map.put(arr[0],arr[1]);
            }
        }
        reader.close();
        return map;
    }
}
