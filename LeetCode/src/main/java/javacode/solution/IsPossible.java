package javacode.solution;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;

public class IsPossible {

    public boolean isPossible(int[] nums) {
        TreeMap<Integer, Integer> f = new TreeMap<>();
        for (int v : nums) {
            if (f.containsKey(v)) {
                f.put(v, f.get(v) + 1);
            } else {
                f.put(v, 1);
            }
        }
        int n = nums.length;
        int[] opens = new int[n + 3];
        int[] closes = new int[n + 3];
        int op = 0, cp = 0;
        for (Map.Entry<Integer, Integer> e : f.entrySet()) {
            {
                int over = e.getValue() - (f.containsKey(e.getKey() - 1) ? f.get(e.getKey() - 1) : 0);
                for (int j = 0; j < over; j++)
                    opens[op++] = e.getKey();
            }
            {
                int over = e.getValue() - (f.containsKey(e.getKey() + 1) ? f.get(e.getKey() + 1) : 0);
                for (int j = 0; j < over; j++)
                    closes[cp++] = e.getKey();
            }
        }
        for (int i = 0; i < op; i++) {
            if (closes[i] - opens[i] < 2)
                return false;
        }
        return true;
    }
    
    @Test
    public void test() {
        int[] A = {1,1,2,2,2,3,3,3,4,4,5};
        isPossible(A);
    }
}

class IsPossible2 {
    public boolean isPossible(int[] nums) {
        if (nums.length == 0) {
            return true;
        }
        SortedMap<Integer, Integer> map = new TreeMap<>();
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        while (map.size() > 0) {
            int smallest = map.firstKey();
            int m0 = map.get(map.firstKey()); 
            // int m0 = 1;
            if (map.containsKey(smallest + 1) && map.containsKey(smallest + 2)) {
                if (map.get(smallest + 1) < m0) {
                    return false;
                }
                m0 = map.get(smallest + 1);
                if (map.get(smallest + 2) < m0) {
                    return false;
                }
                m0 = map.get(smallest + 2);
                int j = smallest + 3;
                while (true) {
                    if (!map.containsKey(j) || map.get(j) < m0) {
                        break;
                    }
                    m0 = map.get(j);
                    j++;
                }
                for (int k = smallest; k < j; k++) {
                    int newM = map.get(k) - 1;
                    if (newM == 0) {
                        map.remove(k);
                    } else {
                        map.put(k, newM);
                    }
                }
            } else {
                return false;
            }
        }
        return true;
    }
}
