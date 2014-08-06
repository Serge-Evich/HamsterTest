package hamster.netty.http.server.stat;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Dmitry on 06.08.2014.
 */
public class UrlEntity {
    public UrlEntity(String url, AtomicLong count, AtomicLong lastTime) {
        this.url = url;
        this.count = count;
        this.lastTime = lastTime;
    }
    private String url;
    private AtomicLong count;
    private AtomicLong lastTime;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AtomicLong getCount() {
        return count;
    }

    public void setCount(AtomicLong count) {
        this.count = count;
    }

    public AtomicLong getLastTime() {
        return lastTime;
    }

    public void setLastTime(AtomicLong lastTime) {
        this.lastTime = lastTime;
    }

    public void incrementCount() {
        count.incrementAndGet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UrlEntity url1 = (UrlEntity) o;

        if (count != null ? !count.equals(url1.count) : url1.count != null) return false;
        if (lastTime != null ? !lastTime.equals(url1.lastTime) : url1.lastTime != null) return false;
        if (url != null ? !url.equals(url1.url) : url1.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (count != null ? count.hashCode() : 0);
        result = 31 * result + (lastTime != null ? lastTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Url{");
        sb.append("url='").append(url).append('\'');
        sb.append(", count=").append(count);
        sb.append(", lastTime=").append(lastTime);
        sb.append('}');
        return sb.toString();
    }
}
