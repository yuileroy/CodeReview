package cache;

import java.util.Timer;
import java.util.TimerTask;

import concurrent.SimpleConcurrentMap;

/**
 * @projName��WZServer
 * @className��CacheHandler
 * @description����������࣬�Ի�����й���,�����ʽ����Timer��ʱ�ķ�ʽ
 * @creater��Administrator
 * @creatTime��2013��7��22�� ����9:18:54
 * @alter��Administrator
 * @alterTime��2013��7��22�� ����9:18:54 @remark��
 * @version
 */
public class CacheTimerHandler {
    private static final int DEFUALT_VALID_TIME = 20;
    private static final Timer timer;
    private static final SimpleConcurrentMap<String, CacheEntity> map;

    static {
        timer = new Timer();
        map = new SimpleConcurrentMap<String, CacheEntity>();
    }

    /**
     * ���ӻ������
     * 
     * @param key
     * @param ce
     */
    public static void addCache(String key, CacheEntity ce) {
        addCache(key, ce, DEFUALT_VALID_TIME);
    }

    /**
     * ���ӻ������
     * 
     * @param key
     * @param ce
     * @param validTime
     *            ��Чʱ��
     */
    public static synchronized void addCache(String key, CacheEntity ce, int validTime) {
        map.put(key, ce);
        // ��ӹ��ڶ�ʱ
        timer.schedule(new TimeoutTimerTask(key), validTime * 1000);
    }

    /**
     * ��ȡ�������
     * 
     * @param key
     * @return
     */
    public static CacheEntity getCache(String key) {
        return map.get(key);
    }

    /**
     * ����Ƿ����ƶ�key�Ļ���
     * 
     * @param key
     * @return
     */
    public static boolean isConcurrent(String key) {
        return map.containsKey(key);
    }

    /**
     * ɾ������
     * 
     * @param key
     */
    public static void removeCache(String key) {
        map.remove(key);
    }

    /**
     * ��ȡ�����С
     * 
     * @param key
     */
    public static int getCacheSize() {
        return map.size();
    }

    /**
     * ���ȫ������
     */
    public static synchronized void clearCache() {
        if (timer != null) {
            timer.cancel();
        }
        map.clear();
        System.out.println("clear cache");
    }

    /**
     * @projName��WZServer
     * @className��TimeoutTimerTask
     * @description�������ʱ���涨ʱ������
     * @creater��Administrator
     * @creatTime��2013��7��22�� ����9:34:39
     * @alter��Administrator
     * @alterTime��2013��7��22�� ����9:34:39 @remark��
     * @version
     */
    static class TimeoutTimerTask extends TimerTask {
        private String ceKey;

        public TimeoutTimerTask(String key) {
            this.ceKey = key;
        }

        @Override
        public void run() {
            CacheTimerHandler.removeCache(ceKey);
            System.out.println("remove : " + ceKey);
        }
    }
}