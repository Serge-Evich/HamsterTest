package hamster.netty.http.server.stat;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Dmitry on 05.08.2014.
 */
public class RedirectStat {

    private static Map<String, RedirectEntity> redirectEntityMap = new ConcurrentHashMap<String, RedirectEntity>();

    public static void addRedirectEntity(String url) {
        RedirectEntity redirectEntity = redirectEntityMap.get(url);
        if (redirectEntity != null) {
            redirectEntity.incrementCount();
            redirectEntityMap.put(url, redirectEntity);

        } else {
            redirectEntityMap.put(url, new RedirectEntity(url, new AtomicInteger(1)));
        }
    }

    public static Map<String, RedirectEntity> getRedirectMap() {
        return new TreeMap<>(redirectEntityMap);
    }
}
