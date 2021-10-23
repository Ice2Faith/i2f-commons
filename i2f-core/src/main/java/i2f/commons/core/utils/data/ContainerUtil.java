package i2f.commons.core.utils.data;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author ltb
 * @date 2021/10/16
 */
public class ContainerUtil {
    public static <E> TreeSet<E> treeSet(E ... vals){
        return collection(new TreeSet<E>(),vals);
    }
    public static <E> HashSet<E> hashSet(E ... vals){
        return collection(new HashSet<E>(Math.max((int)(vals.length/0.7), 64)),vals);
    }
    public static <E> LinkedList<E> lnkList(E ... vals){
        return collection(new LinkedList<E>(),vals);
    }
    public static <E> ArrayList<E> arrList(E... vals) {
        return collection(new ArrayList<E>(Math.max(vals.length, 64)), vals);
    }
    public static <E> CopyOnWriteArrayList<E> conArrList(E ... vals){
        return collection(new CopyOnWriteArrayList<E>(),vals);
    }
    public static <E> CopyOnWriteArraySet<E> conArrSet(E ... vals){
        return collection(new CopyOnWriteArraySet<E>(),vals);
    }

    public static <K,V> HashMap<K,V> hashMapKvs(Object ... kvs){
        return mapKvs(new HashMap<K,V>(Math.max((int)(kvs.length/0.7),64)),kvs);
    }

    public static <K,V> ConcurrentHashMap<K,V> conHashMapKvs(Object ... kvs){
        return mapKvs(new ConcurrentHashMap<K,V>(Math.max((int)(kvs.length/0.7),64)),kvs);
    }

    public static <K,V> LinkedHashMap<K,V> lnkHashMapKvs(Object ... kvs){
        return mapKvs(new LinkedHashMap<K,V>(),kvs);
    }

    public static <K,V> HashMap<K,V> hashMapArr(K[] keys,V[] vals){
        int minLen=Math.min(keys!=null?keys.length:64, vals!=null?vals.length:64);
        return mapArr(new HashMap<K,V>(Math.max(minLen,64)),keys,vals);
    }

    public static <K,V> ConcurrentHashMap<K,V> conHashMapArr(K[] keys,V[] vals){
        int minLen=Math.min(keys!=null?keys.length:64, vals!=null?vals.length:64);
        return mapArr(new ConcurrentHashMap<K,V>(Math.max(minLen,64)),keys,vals);
    }

    public static <K,V> LinkedHashMap<K,V> lnkHashMapArr(K[] keys,V[] vals){
        return mapArr(new LinkedHashMap<K,V>(),keys,vals);
    }

    public static <T extends Collection<E>, E> T collection(T col, E... vals) {
        if (col == null) {
            return col;
        }
        if (vals == null || vals.length == 0) {
            return col;
        }
        for (E item : vals) {
            col.add(item);
        }
        return col;
    }

    public static <T extends Map<K, V>, K, V> T mapKvs(T map, Object... kvs) {
        if (map == null) {
            return map;
        }
        if (kvs == null || kvs.length == 0) {
            return map;
        }
        for (int i = 0; (i + 1) < kvs.length; i += 2) {
            K key = (K) kvs[i];
            V val = (V) kvs[i + 1];
            map.put(key, val);
        }
        return map;
    }

    public static <T extends Map<K, V>, K, V> T mapArr(T map, K[] keys, V[] vals) {
        if (map == null) {
            return map;
        }
        if (keys == null || keys.length == 0 || vals == null || vals.length == 0) {
            return map;
        }
        int minLen = Math.min(keys.length, vals.length);
        for (int i = 0; i < minLen; i++) {
            K key = keys[i];
            V val = vals[i];
            map.put(key, val);
        }
        return map;
    }

}
