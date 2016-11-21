package net.tsinghua.arc.util.cache;

/**
 * Created by ji on 16-2-4.
 */
public interface ExpireCallBack<K, V> {

    public V handler(K key, boolean isEnd) throws Exception;

}
