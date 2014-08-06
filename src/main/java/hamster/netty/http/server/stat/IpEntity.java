package hamster.netty.http.server.stat;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Димон on 03.08.2014.
 */
public class IpEntity {
    private String ip;
    private ConcurrentHashMap<String, UrlEntity> urlMap;

    public IpEntity(String ip, String url) {
        this.ip = ip;
        addUrl(url);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getRequestCount() {
        AtomicLong count = new AtomicLong(0);
        for (UrlEntity u : urlMap.values()) {
            count.addAndGet(u.getCount().get());
        }
        return count.get();
    }

    public long getLastTime() {
        AtomicLong lastTime = new AtomicLong(0);
        for (UrlEntity u : urlMap.values()) {
            if(u.getLastTime().get() > lastTime.get()) {
                lastTime.addAndGet(u.getLastTime().get());
            }
        }
        return lastTime.get();
    }

    public void addUrl(String url) {
        if(urlMap == null) {
            urlMap = new ConcurrentHashMap<>();
        }
        UrlEntity u = urlMap.get(url);
        if(u != null) {
            u.incrementCount();
            u.setLastTime(new AtomicLong(System.currentTimeMillis()));
            urlMap.replace(url, u);
        } else {
            u = new UrlEntity(url, new AtomicLong(1), new AtomicLong(System.currentTimeMillis()));
            urlMap.put(url, u);
        }
    }
    public String getLastUrl() {
        String url = "";
        AtomicLong lastTime = new AtomicLong(0);
        for (UrlEntity u : urlMap.values()) {
            if(u.getLastTime().get() > lastTime.get()) {
                lastTime.addAndGet(u.getLastTime().get());
                url = u.getUrl();
            }
        }
        return url;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IpEntity ipEntity = (IpEntity) o;

        if (ip != null ? !ip.equals(ipEntity.ip) : ipEntity.ip != null) return false;
        if (urlMap != null ? !urlMap.equals(ipEntity.urlMap) : ipEntity.urlMap != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ip != null ? ip.hashCode() : 0;
        result = 31 * result + (urlMap != null ? urlMap.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("IpEntity{");
        sb.append("ip='").append(ip).append('\'');
        sb.append(", urlMap=").append(urlMap);
        sb.append('}');
        return sb.toString();
    }
}
