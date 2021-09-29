package i2f.commons.core.permissions.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class PermissionSet {
    public Set<Integer> ids=new HashSet<>();
    public Set<String> names=new HashSet<>();

    public PermissionSet(int[] hasIds,String[] hasNames){
        if(hasIds!=null && hasIds.length>0){
            for(int item : hasIds){
                ids.add(item);
            }
        }
        if(hasNames!=null && hasNames.length>0){
            for(String item : hasNames){
                names.add(item);
            }
        }
    }
}
