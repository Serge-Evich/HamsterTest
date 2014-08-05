package hamster.netty.http.server.stat;

import java.util.concurrent.atomic.AtomicInteger;


public class RedirectEntity {
    private String url;
    private AtomicInteger redirectCount;

    public RedirectEntity(String url, AtomicInteger redirectCount) {
        this.url = url;
        this.redirectCount = redirectCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AtomicInteger getRedirectCount() {
        return redirectCount;
    }

    public void setRedirectCount(AtomicInteger redirectCount) {
        this.redirectCount = redirectCount;
    }

    public void incrementCount() {
        this.redirectCount.incrementAndGet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RedirectEntity that = (RedirectEntity) o;

        if (redirectCount != null ? !redirectCount.equals(that.redirectCount) : that.redirectCount != null)
            return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (redirectCount != null ? redirectCount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RedirectEntity{");
        sb.append("url='").append(url).append('\'');
        sb.append(", redirectCount=").append(redirectCount);
        sb.append('}');
        return sb.toString();
    }
}
