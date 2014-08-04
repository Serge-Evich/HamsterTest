package hamster.netty.http.server.stat;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Димон on 03.08.2014.
 */
public class RequestEntity {
    private String ip;
    private AtomicLong count;
    private AtomicLong lastTime;

    public RequestEntity(String ip, AtomicLong count, AtomicLong lastTime) {
        this.ip = ip;
        this.count = count;
        this.lastTime = lastTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getCount() {
        return count.get();
    }

    public void setCount(AtomicLong count) {
        this.count = count;
    }

    public long getLastTime() {
        return lastTime.get();
    }

    public void setLastTime(AtomicLong lastTime) {
        this.lastTime = lastTime;
    }
    public long incrementCount() {
        return this.count.incrementAndGet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestEntity that = (RequestEntity) o;

        if (count != null ? !count.equals(that.count) : that.count != null) return false;
        if (ip != null ? !ip.equals(that.ip) : that.ip != null) return false;
        if (lastTime != null ? !lastTime.equals(that.lastTime) : that.lastTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ip != null ? ip.hashCode() : 0;
        result = 31 * result + (count != null ? count.hashCode() : 0);
        result = 31 * result + (lastTime != null ? lastTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RequestEntity{");
        sb.append("ip='").append(ip).append('\'');
        sb.append(", count=").append(count);
        sb.append(", lastTime=").append(lastTime);
        sb.append('}');
        return sb.toString();
    }
}
