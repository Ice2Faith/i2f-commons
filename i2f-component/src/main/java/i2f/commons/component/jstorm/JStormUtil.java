package i2f.commons.component.jstorm;

import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;

import java.util.Map;

public class JStormUtil {
    public static volatile LocalCluster cluster;
    public static LocalCluster getLocalCluster(){
        if(cluster==null){
            synchronized (JStormUtil.class){
                if(cluster==null){
                    cluster=new LocalCluster();
                }
            }
        }
        return cluster;
    }
    public static void submitLocal(String id, Map config, TopologyBuilder builder){
        getLocalCluster().submitTopology(id,config, builder.createTopology());
    }
    public static void submitLocal(String id, Map config, StormTopology topology){
        getLocalCluster().submitTopology(id,config,topology);
    }
    public static void killTopology(String id){
        getLocalCluster().killTopology(id);
    }
    public static void shutdown(){
        getLocalCluster().shutdown();
    }

}
