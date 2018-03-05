package cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import concurrent.SimpleConcurrentMap;

/**
 * @projName��WZServer
 * @className��CacheHandler
 * @description����������࣬�Ի�����й������ô�����У���ʱѭ������ķ�ʽ
 * @creater��Administrator
 * @creatTime��2013��7��22�� ����9:18:54
 * @alter��Administrator
 * @alterTime��2013��7��22�� ����9:18:54 @remark��
 * @version
 */
public class CacheListHandler {
    private static final long SECOND_TIME = 1000;
    private static final SimpleConcurrentMap<String, CacheEntity> map;
    private static final List<CacheEntity> tempList;

    static {
        tempList = new ArrayList<CacheEntity>();
        map = new SimpleConcurrentMap<String, CacheEntity>(new HashMap<String, CacheEntity>(1 << 18));
        new Thread(new TimeoutTimerThread()).start();
    }

    /**
     * ���ӻ������
     * 
     * @param key
     * @param ce
     */
    public static void addCache(String key, CacheEntity ce) {
        addCache(key, ce, ce.getValidTime());
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
        ce.setTimeoutStamp(System.currentTimeMillis() + validTime * SECOND_TIME);
        map.put(key, ce);
        // ��ӵ����ڴ������
        tempList.add(ce);
    }

    /**
     * ��ȡ�������
     * 
     * @param key
     * @return
     */
    // public static synchronized CacheEntity getCache(String key) {
    public static CacheEntity getCache(String key) {
        return map.get(key);
    }

    /**
     * ����Ƿ����ƶ�key�Ļ���
     * 
     * @param key
     * @return
     */
    // public static synchronized boolean isConcurrent(String key) {
    public static boolean isConcurrent(String key) {
        return map.containsKey(key);
    }

    /**
     * ɾ������
     * 
     * @param key
     */
    // public static synchronized void removeCache(String key) {
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
        tempList.clear();
        map.clear();
        System.out.println("clear cache");
    }

    static class TimeoutTimerThread implements Runnable {
        public void run() {
            while (true) {
                try {
                    checkTime();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * ���ڻ���ľ��崦����
         * 
         * @throws Exception
         */
        private void checkTime() throws Exception {
            // "��ʼ������� ";
            CacheEntity tce = null;
            long timoutTime = 1000L;

            // " ���ڶ��д�С : "+tempList.size());
            if (tempList.isEmpty()) {
                System.out.println("���ڶ��пգ���ʼ��ѯ");
                timoutTime = 1000L;
                Thread.sleep(timoutTime);
                return;
            }

            tce = tempList.get(0);
            timoutTime = tce.getTimeoutStamp() - System.currentTimeMillis();
            // " ����ʱ�� : "+timoutTime);
            if (timoutTime > 0) {
                // �趨����ʱ��
                Thread.sleep(timoutTime);
                return;
            }
            System.out.print(" ������ڻ��� �� " + tce.getCacheKey());
            // ������ڻ����ɾ����Ӧ�Ļ������
            tempList.remove(tce);
            removeCache(tce.getCacheKey());
        }
    }
}
