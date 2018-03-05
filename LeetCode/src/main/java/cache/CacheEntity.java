package cache;

import java.io.Serializable;

public class CacheEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int DEFUALT_VALID_TIME = 20;

    private String cacheKey;
    private Object cacheContext;
    private int validTime;
    private long timeoutStamp;

    private CacheEntity() {
        this.validTime = DEFUALT_VALID_TIME;
        this.timeoutStamp = System.currentTimeMillis() + DEFUALT_VALID_TIME * 1000;
    }

    public CacheEntity(String cacheKey, Object cacheContext) {
        this();
        this.cacheKey = cacheKey;
        this.cacheContext = cacheContext;
    }

    public CacheEntity(String cacheKey, Object cacheContext, int validTime) {
        this(cacheKey, cacheContext);
        this.validTime = validTime;
        this.timeoutStamp = System.currentTimeMillis() + validTime * 1000;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public Object getCacheContext() {
        return cacheContext;
    }

    public void setCacheContext(Object cacheContext) {
        this.cacheContext = cacheContext;
    }

    public long getTimeoutStamp() {
        return timeoutStamp;
    }

    public void setTimeoutStamp(long timeoutStamp) {
        this.timeoutStamp = timeoutStamp;
    }

    public int getValidTime() {
        return validTime;
    }

    public void setValidTime(int validTime) {
        this.validTime = validTime;
    }
}