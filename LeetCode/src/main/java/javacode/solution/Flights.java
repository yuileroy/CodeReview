package javacode.solution;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class Flights {

    List<String> res = null;
    int minP = Integer.MAX_VALUE;

    void setResult(String[][] flights, String start, String end, int maxCnt) {
        List<String> preList = new ArrayList<>();
        preList.add(start);
        fn1(flights, start, end, preList, 0, 0, maxCnt);
        System.out.println(res);
        System.out.println(minP);
    }

    void fn(String[][] flights, String start, String end, List<String> preList, int prePrice, int cnt, int maxCnt) {
        if (cnt > maxCnt) {
            return;
        }
        if (start.equals(end)) {
            if (prePrice <= minP) {
                minP = prePrice;
                res = preList;
            }
            return;
        }

        for (String[] F : flights) {
            if (start.equals(F[0]) && !preList.contains(F[1])) {
                List<String> tmp = new ArrayList<>(preList);
                tmp.add(F[1]);
                fn(flights, F[1], end, tmp, prePrice + Integer.parseInt(F[2]), cnt + 1, maxCnt);
            }
        }
    }
    
    void fn1(String[][] flights, String start, String end, List<String> preList, int prePrice, int cnt, int maxCnt) {
        if (cnt > maxCnt) {
            return;
        }
        if (start.equals(end)) {
            if (prePrice <= minP) {
                minP = prePrice;
                res = new ArrayList<>(preList);
            }
            return;
        }

        for (String[] F : flights) {
            if (start.equals(F[0]) && !preList.contains(F[1])) {            
                preList.add(F[1]);
                fn1(flights, F[1], end, preList, prePrice + Integer.parseInt(F[2]), cnt + 1, maxCnt);
                preList.remove(preList.size() - 1);
            }
        }
    }

    @Test
    public void test() {
        String[][] flights = { { "A", "B", "100" }, { "A", "C", "500" }, { "B", "C", "200" }, { "B", "A", "150" } };
        setResult(flights, "A", "C", 1);
        setResult(flights, "A", "C", 2);
        
    }
}