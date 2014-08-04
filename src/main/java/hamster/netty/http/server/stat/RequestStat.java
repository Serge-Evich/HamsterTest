package hamster.netty.http.server.stat;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Димон on 03.08.2014.
 */
public class RequestStat {

    private static ConcurrentHashMap<String, RequestEntity>
            requestEntityMap = new ConcurrentHashMap<String, RequestEntity>();

    public static void addRequestEntity(String ip) {
        RequestEntity requestEntity = requestEntityMap.get(ip);
        if(requestEntity != null) {
            requestEntity.incrementCount();
            requestEntity.setLastTime(new AtomicLong(System.currentTimeMillis()));
            requestEntityMap.replace(ip, requestEntity);
        }  else {
            requestEntityMap.put(ip, new RequestEntity(ip, new AtomicLong(1), new AtomicLong(System.currentTimeMillis())));
        }

    }

    public static long getAllReqCount() {
        AtomicLong count = new AtomicLong(0);
        for(Map.Entry<String, RequestEntity> entityEntry : requestEntityMap.entrySet()) {
            count.addAndGet(entityEntry.getValue().getCount());
        }
        return count.get();
    }

    public static long getUniReqCount() {
        AtomicLong count = new AtomicLong(0);
        for(Map.Entry<String, RequestEntity> entityEntry : requestEntityMap.entrySet()) {
            RequestEntity req = entityEntry.getValue();
            if(req.getCount() == 1l)
                count.incrementAndGet();
        }
        return count.get();
    }

    public static Map<String, RequestEntity> getReqEntityMap() {
        return new TreeMap<String, RequestEntity>(requestEntityMap);
    }
}
