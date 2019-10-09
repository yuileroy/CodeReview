package keycode;

import java.util.HashMap;
import java.util.Map;

public class TmpClass {

    static String calculate(String s) {
        // if it has no ( and ),
        // e + 8 - 5 + eb
        int res = 0;
        StringBuilder sb = new StringBuilder();
        boolean pos = true;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '+') {
                pos = true;
            } else if (c == '-') {
                pos = false;
            } else if (c == '(') {
                // substring
                int left = 1;
                int idx = i + 1;
                while (idx < s.length()) {
                    if (s.charAt(idx) == '(') {
                        left++;
                    } else if (s.charAt(idx) == ')') {
                        left--;
                        if (left == 0) {
                            String val = calculate(s.substring(i + 1, idx));
                            if (pos) {
                                // res += val;
                            } else {
                                // res -= val;
                            }
                            i = idx;
                            break; // end while
                        }
                    }
                    idx++;
                }
            } else if (Character.isDigit(s.charAt(i))) {
                int num = 0;
                while (i < s.length() && Character.isDigit(s.charAt(i))) {
                    num = num * 10 + s.charAt(i) - '0';
                    i++;
                }
                // System.out.println(num);
                if (pos) {
                    res += num;
                } else {
                    res -= num;
                }
                i--;
            } else {
                if (res != 0) {
                    sb.append(res);
                }
                res = 0;
                sb.append(pos ? '+' : '-');
                while (i < s.length() && 'a' <= s.charAt(i) && s.charAt(i) <= 'z') {
                    sb.append(s.charAt(i));
                    i++;
                }
                i--;
            }
        }
        for (int k = 0; k < sb.length(); k++) {
            if (!Character.isDigit(sb.charAt(k))) {
                sb.insert(0, '(');
                sb.append(')');
                return sb.toString();
            }
        }
        return sb.toString();
    }

    int convert(String input) {
        Map<String, Integer> map = new HashMap<>();
        map.put("20", 20);
        map.put("1", 1);
        map.put("15", 15);
        String[] T = input.split("\\-");
        int res = 0, cur = 0;
        for (String s : T) {
            // 1-99
            if (map.containsKey(s)) {
                cur += map.get(s);
            } else if (s.equals("hundred")) {
                cur *= 100;
            } else if (s.equals("thousand")) {
                cur *= 1000;
                res += cur;
                cur = 0;
            } else if (s.equals("million")) {
                cur *= 1000000;
                res += cur;
                cur = 0;
            }
        }
        res += cur;
        return res;
    }
}
