package i2f.commons.component.hadoop.hdfs.data;

import org.apache.hadoop.conf.Configuration;

/**
 * @author ltb
 * @date 2021/11/1
 */
public interface IHdfsMeta {
    /**
     * <property>
     *     <name>fs.defaultFS</name>
     *     <value>hdfs://192.168.1.120:8020</value>
     *   </property>
     * @return
     */
    String getDefaultFs();
    String  getUri();
    String getUser();
    Configuration getConfig();
}
