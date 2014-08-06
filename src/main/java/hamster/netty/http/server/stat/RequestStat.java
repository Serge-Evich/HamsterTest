package hamster.netty.http.server.stat;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Димон on 03.08.2014.
 */
public class RequestStat {

    private static ConcurrentHashMap<String, IpEntity>
            requestEntityMap = new ConcurrentHashMap<String, IpEntity>();

    public static void addIpEntity(String ip, String url) {
        IpEntity requestEntity = requestEntityMap.get(ip);
        if(requestEntity != null) {
            requestEntity.addUrl(url);

        }  else {
            requestEntityMap.put(ip, new IpEntity(ip, url));
        }

    }

    public static long getAllReqCount() {
        AtomicLong count = new AtomicLong(0);
        for(Map.Entry<String, IpEntity> entityEntry : requestEntityMap.entrySet()) {
            count.addAndGet(entityEntry.getValue().getRequestCount());
        }
        return count.get();
    }

    public static long getUniReqCount() {
        AtomicLong count = new AtomicLong(0);
        for(Map.Entry<String, IpEntity> entityEntry : requestEntityMap.entrySet()) {
            IpEntity req = entityEntry.getValue();
            if(req.getRequestCount() == 1l)
                count.incrementAndGet();
        }
        return count.get();
    }

    public static TreeMap<Long, IpEntity> getLastConnections() {
        TreeMap<Long, IpEntity> treeMap = new TreeMap<>();
        TreeMap<Long, IpEntity> resMap = new TreeMap<>();
        for (IpEntity ip : requestEntityMap.values()) {
            treeMap.put(ip.getLastTime(), ip);
        }
        int size = treeMap.size();

        if (size <= 16) {
            for (int i = 0; i < size; i++) {
                Map.Entry<Long, IpEntity> entry = treeMap.pollLastEntry();
                resMap.put(entry.getKey(), entry.getValue());
            }
        } else {
            for (int i = 0; i < 16; i++) {
                Map.Entry<Long, IpEntity> entry = treeMap.pollLastEntry();
                resMap.put(entry.getKey(), entry.getValue());
            }
        }
        return resMap;
    }

    public static Map<String, IpEntity> getReqEntityMap() {
        return new TreeMap<String, IpEntity>(requestEntityMap);
    }
}
