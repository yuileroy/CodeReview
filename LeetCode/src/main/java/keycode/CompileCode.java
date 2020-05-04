package keycode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class CompileCode {

    public int canCompleteCircuit(int[] gas, int[] cost) {

        Stack<Integer> s = new Stack<>();
        s.pop();

        int gasTotal = 0;
        int costTotal = 0;
        for (int i = 0; i < gas.length; i++) {
            gasTotal += gas[i];
            costTotal += cost[i];
        }
        if (gasTotal < costTotal) {
            return -1;
        }
        int res = -1;
        gasTotal = 0;
        costTotal = 0;
        for (int i = 0; i < gas.length; i++) {
            gasTotal += gas[i];
            costTotal += cost[i];
            if (gasTotal >= costTotal) {
                if (res == -1) {
                    res = i;
                }
            } else {
                res = -1;
                gasTotal = 0;
                costTotal = 0;
            }
        }
        return res;
    }

    // "abc abs, abd! ddd"

    public String reverse(String s) {
        StringBuilder sb = new StringBuilder();
        String[] word = s.split("[\\W]+");
        String[] punc = s.split("[\\w]+");
        for (int i = 0; i < punc.length; i++) {
            sb.append(punc[i]).append(word[punc.length - i - 1]);
        }
        System.out.println(sb.toString());
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

    int minTrips(int[] bags, int limit) {
        int res = 0;
        Arrays.sort(bags);

        return res;
    }

    int perfectSub(String s, int k) {
        char[] ch = s.toCharArray();
        int n = s.length();
        int res = 0;
        for (int m = k; m <= n; m += k) {
            int i = 0;
            int miss = 0, extra = 0;
            int[] cnt = new int[10];
            for (int j = 0; j < n; j++) {
                int cur = ch[j] - '0';
                cnt[cur]++;
                if (cnt[cur] == 1) {
                    miss += k - 1;
                } else if (cnt[cur] <= k) {
                    miss--;
                } else {
                    extra++;
                }
                if (j - i + 1 > m) {
                    int start = ch[i++] - '0';
                    cnt[start]--;
                    if (cnt[start] == k) {
                        extra--;
                    } else if (cnt[start] == 0) {
                        miss -= k - 1;
                    } else {
                        miss++;
                    }
                }
                if (j - i + 1 == m && miss == 0 && extra == 0) {
                    System.out.println(s.substring(i, j + 1));
                    res++;
                }
            }
        }
        return res;
    }

    class PStack {
        private LinkedList<Integer> list = new LinkedList<>();
        private int size = 0;

        PStack() {

        }

        PStack(LinkedList<Integer> list, int size) {
            this.list = list;
            this.size = size;
        }

        public int size() {
            return size;
        }

        public PStack push(int x) {
            list.add(x);
            return new PStack(list, size + 1);
        }

        public PStack pop() {
            peek();
            return new PStack(list, size - 1);
        }

        public PStack peek() {
            if (size == 0) {
                throw new RuntimeException("empty stack");
            }
            return this;
        }

        public String toString() {
            if (size == 0) {
                return "[]";
            }
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < size; i++) {
                sb.append(list.get(i)).append(", ");
            }
            sb.setLength(sb.length() - 2);
            sb.append("]");
            return sb.toString();
        }
    }

    public void sortOddEven(int[] A) {
        // [1, 3, 5, 2, 4, 6]
        int odd = 0, even = 1;
        while (odd < A.length && even < A.length) {
            while (odd < A.length && A[odd] % 2 != 0) {
                odd += 2;
            }
            while (even < A.length && A[even] % 2 == 0) {
                even += 2;
            }
            System.out.println(odd + "-" + even);
            if (odd < A.length && even < A.length) {
                swap(A, odd, even);
                odd += 2;
                even += 2;
            }
        }
    }

    private void swap(int[] A, int a, int b) {
        int tmp = A[a];
        A[a] = A[b];
        A[b] = tmp;
    }

    @Test
    public void test() {

        int[] A = { 2, 3, 5, 1, 4, 6, 8, 1 };
        sortOddEven(A);
        for (int i = 0; i < A.length; i++) {
            System.out.print(A[i]);
        }

        System.out.println(perfectSub("1102021222", 2));
        // System.out.println(convert("15-million-1-hundred-15-thousand-20-1"));
        reverse("abc abs, abd! ddd");
        String[] ss = "done   do".split("[ ]+");
        for (String s : ss) {
            System.out.println(s);
        }

        String regex = "https:\\\\[\\w\\.]+\\.com";
        String test = "https:\\abc.edf.com<> https:\\abdf.com";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(test);
        while (matcher.find()) {
            System.out.format("I found the text" + " \"%s\" starting at " + "index %d and ending at index %d.%n",
                    matcher.group(), matcher.start(), matcher.end());
        }

        PStack P0 = new PStack();
        PStack P1 = P0.push(1);
        PStack P2 = P1.push(2);
        PStack P3 = P2.pop();
        System.out.println(P1);
        System.out.println(P2);
        System.out.println(P3);

    }
}