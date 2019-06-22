package leetcodelock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//170. Two Sum III - Data structure design
class TwoSum {

    private List<Integer> list = new ArrayList<>();
    private Map<Integer, Integer> map = new HashMap<>();

    /** Initialize your data structure here. */
    public TwoSum() {

    }

    /** Add the number to an internal data structure.. */
    public void add(int number) {
        if (map.containsKey(number)) {
            map.put(number, map.get(number) + 1);
        } else {
            map.put(number, 1);
            list.add(number);
        }
    }

    /** Find if there exists any pair of numbers which sum is equal to the value. */
    public boolean find(int value) {
        for (int i = 0; i < list.size(); i++) {
            int num1 = list.get(i), num2 = value - num1;
            if ((num1 == num2 && map.get(num1) > 1) || (num1 != num2 && map.containsKey(num2)))
                return true;
        }
        return false;
    }
}

// 186. Reverse Words in a String II
class Solution186 {
    public void reverseWords(char[] str) {
        swap(str, 0, str.length - 1);
        int start = 0;
        for (int i = 0; i < str.length; i++) {
            if (str[i] == ' ') {
                swap(str, start, i - 1);
                start = i + 1;
            }
        }
        swap(str, start, str.length - 1);
        return;
    }
    
    private void swap(char[] str, int s, int e) {
        //!! while (s++ < e--) {
        while (s < e) {
            char t = str[s];
            str[s] = str[e];
            str[e] = t;
            s++;
            e--;
        }
    }
}