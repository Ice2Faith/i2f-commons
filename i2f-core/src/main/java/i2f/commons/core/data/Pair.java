package i2f.commons.core.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Pair<K, V> {
    public K key;
    public V val;
    public Object tag;

    public Pair(K key, V val) {
        this.key = key;
        this.val = val;
    }

    public Pair(K key, V val, Object tag) {
        this.key = key;
        this.val = val;
        this.tag = tag;
    }
}
