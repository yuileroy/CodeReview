package leetcode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.junit.Test;

import javacode.code.Interval;

public class LeetCode {

    public int[] exclusiveTime0(int n, List<String> logs) {
        int[] res = new int[n];
        int sum = 0;
        Deque<Integer> iq = new ArrayDeque<>();
        Deque<Integer> tq = new ArrayDeque<>();
        String[] t = null;
        for (String lg : logs) {
            t = lg.split(":");
            int id = Integer.parseInt(t[0]);
            boolean isStart = t[1].equals("start");
            int ts = Integer.parseInt(t[2]);
            if (isStart) {
                iq.addLast(id);
                tq.addLast(ts);
            } else {
                int startT = tq.removeLast();
                res[id] = 1 + ts - startT - sum;
                sum += 1 + ts - startT;
                if (tq.isEmpty()) {
                    sum = 0;
                }
            }
        }
        return res;
    }

    public int[] exclusiveTime2(int n, List<String> logs) {
        int[] res = new int[n];

        List<int[]> iq = new ArrayList<>();
        List<Integer> tq = new ArrayList<>();
        String[] t = null;
        for (String lg : logs) {
            t = lg.split(":");
            int id = Integer.parseInt(t[0]);
            boolean isStart = t[1].equals("start");
            int ts = Integer.parseInt(t[2]);
            if (isStart) {
                int[] x = { id, 0 };
                iq.add(x);
                tq.add(ts);
            } else {
                if (res[id] == 0) {
                    if (iq.get(iq.size() - 1)[0] == id && iq.get(iq.size() - 1)[1] == 0) {
                        int startT = tq.get(tq.size() - 1);
                        res[id] = 1 + ts - startT;
                    } else {
                        int sum = 0;
                        for (int k = iq.size() - 1; k >= 0; k--) {
                            if (iq.get(k)[0] == id && iq.get(k)[1] == 0) {
                                res[id] = 1 + ts - tq.get(k) - sum / 2;
                                break;
                            } else {
                                sum += res[iq.get(k)[0]];
                            }
                        }
                    }
                }
                int[] x = { id, 1 };
                iq.add(x);
                tq.add(ts);
            }
        }
        return res;
    }

    public int shoppingOffers(List<Integer> price, List<List<Integer>> special, List<Integer> needs) {
        int basePrice = 0;
        for (int i = 0; i < price.size(); i++) {
            basePrice += price.get(i) * needs.get(i);
        }
        for (int i = 0; i < special.size(); i++) {
            List<Integer> cp = special.get(i);
            boolean feasible = true;
            int currPrice = 0;
            for (int j = 0; j < needs.size() && feasible; j++) {
                if (cp.get(j) > needs.get(j)) {
                    feasible = false;
                } else {
                    currPrice += cp.get(j) * price.get(j);
                }
            }
            // sum cost of item > use special price
            if (feasible && currPrice > cp.get(needs.size())) {
                List<Integer> newNeeds = new ArrayList<>();
                for (int j = 0; j < needs.size(); j++) {
                    newNeeds.add(needs.get(j) - cp.get(j));
                }
                int newPrice = cp.get(needs.size()) + shoppingOffers(price, special, newNeeds);
                if (newPrice < basePrice) {
                    basePrice = newPrice;
                }
            }
        }
        return basePrice;
    }

    static String findNumber(int[] arr, int k) {
        Set<Integer> set = new HashSet<>();
        for (int e : arr) {
            set.add(e);
        }
        return set.contains(k) ? "YES" : "NO";
    }

    // static String largestMagical(String binString) {
    // 1100_10_111000_110100
    // int start = 0,
    // }

    // 761. Special Binary String
    public String makeLargestSpecial(String S) {
        List<String> token = new ArrayList<>();
        int start = 0;
        int count = 0;
        for (int i = 0; i < S.length(); i++) {
            if (S.charAt(i) == '1') {
                count++;
            } else {
                count--;
            }
            // [start, i] inclusively is special with start = 1, i = 0. optimize [start + 1, i - 1]
            if (count == 0) {
                token.add('1' + makeLargestSpecial(S.substring(start + 1, i)) + '0');
                start = i + 1;
            }
        }
        Collections.sort(token, Collections.reverseOrder());
        return String.join("", token);
    }

    static String largestMagical(String binString) {
        List<String> token = new ArrayList<>();
        int start = 0;
        int count = 0;
        for (int i = 0; i < binString.length(); i++) {
            if (binString.charAt(i) == '1') {
                count++;
            } else {
                count--;
            }
            if (count == 0) {
                token.add(binString.substring(start, ++i));
                start = i;
            }
        }
        Collections.sort(token, Collections.reverseOrder());
        return String.join("", token);
    }

    public int minSwapsCouples(int[] row) {
        int[] pos = new int[row.length];
        for (int i = 0; i < row.length; i++) {
            pos[row[i]] = i;
        }
        int count = 0;
        // greedy
        for (int i = 0; i < row.length; i += 2) {
            int cp = row[i] % 2 == 0 ? row[i] + 1 : row[i] - 1;
            if (row[i + 1] != cp) {
                swap(row, pos, i + 1, pos[cp]);
                count++;
            }
        }
        return count;
    }

    private void swap(int[] row, int[] pos, int x, int y) {
        pos[row[x]] = y;
        pos[row[y]] = x;
        int tmp = row[x];
        row[x] = row[y];
        row[y] = tmp;
    }

    // 759. Employee Free Time
    public List<Interval> employeeFreeTime(List<List<Interval>> avails) {

        List<Interval> timeLine = new ArrayList<>();
        avails.forEach(e -> timeLine.addAll(e));
        Collections.sort(timeLine, ((a, b) -> a.start - b.start));

        List<Interval> res = new ArrayList<>();
        Interval tmp = timeLine.get(0);
        for (Interval item : timeLine) {
            if (tmp.end < item.start) {
                res.add(new Interval(tmp.end, item.start));
                tmp = item;
            } else {
                tmp = tmp.end < item.end ? item : tmp;
            }
        }
        return res;
    }

    void superStack(String[] operations) {
        Stack<Long> stack = new Stack<>();
        Map<Long, Long> map = new HashMap<>();
        // key is last index of stack, value is k to be added to last element

        int lineNum = -1;
        while (++lineNum < operations.length) {
            String[] token = operations[lineNum].split(" ");

            if ("push".equals(token[0])) { // !-> ("push" == token[0])
                stack.push(Long.parseLong(token[1]));
            } else if ("pop".equals(token[0])) {
                long size = stack.size();
                stack.pop();
                if (map.containsKey(size)) {
                    long addK = map.remove(size);
                    map.put(size - 1, map.getOrDefault(size - 1, 0L) + addK);
                }
            } else {
                long e = Long.parseLong(token[1]), k = Long.parseLong(token[2]);
                map.put(e, map.getOrDefault(e, 0L) + k);
            }
            if (stack.isEmpty()) {
                System.out.println("EMPTY");
            } else {
                long size = stack.size();
                // !-> .get(Object key), cast both to long
                long res = stack.peek() + map.getOrDefault(size, 0L);
                System.out.println(res);
            }
        }
    }

    static void superStack0(String[] operations) {
        Stack<Integer> stack = new Stack<>();
        Map<Integer, Integer> map = new HashMap<>();
        // key is last index of stack, value is k to be added to last element

        int lineNum = -1;
        while (++lineNum < operations.length) {
            String[] token = operations[lineNum].split(" ");

            if ("push".equals(token[0])) {
                stack.push(Integer.parseInt(token[1]));
            } else if ("pop".equals(token[0])) {
                int size = stack.size();
                stack.pop();
                if (map.containsKey(size)) {
                    int addK = map.remove(size);
                    map.put(size - 1, map.getOrDefault(size - 1, 0) + addK);
                }
            } else {
                int e = Integer.parseInt(token[1]), k = Integer.parseInt(token[2]);
                map.put(e, map.getOrDefault(e, 0) + k);
            }
            if (stack.isEmpty()) {
                System.out.println("EMPTY");
            } else {
                int res = stack.peek() + map.getOrDefault(stack.size(), 0);
                System.out.println(res);
            }
        }
    }

    public double[] sampleStats(int[] count) {
        double[] res = new double[5];
        res[0] = 256;
        long sum = 0, cnt = 0, maxCnt = 0;
        for (int i = 0; i < count.length; i++) {
            if (count[i] == 0) {
                continue;
            }
            sum += i * count[i];
            cnt += count[i];
            res[0] = Math.min(res[0], i);
            res[1] = Math.max(res[1], i);
            if (count[i] > maxCnt) {
                res[4] = i;
                maxCnt = count[i];
            }
        }
        res[2] = (0.0 + sum) / cnt;
        long m1 = 0, m2 = 0;
        long mid1 = (cnt + 1) / 2;
        long mid2 = cnt % 2 == 0 ? mid1 + 1 : mid1;
        cnt = 0;
        for (int i = 0; i < count.length; i++) {
            cnt += count[i];
            if (cnt >= mid1) {
                m1 = i;
                break;
            }
        }
        cnt = 0;
        for (int i = 0; i < count.length; i++) {
            cnt += count[i];
            if (cnt >= mid2) {
                m2 = i;
                break;
            }
        }
        res[3] = (m1 + m2) / 2.0;
        return res;
    }

    class Solution {
        Map<Integer, Integer> map = new HashMap<>();

        public int findInMountainArray(int target, MountainArray mountainArr) {
            int len = mountainArr.length();
            int l = 0, r = len - 1, top = 0;

            while (l < r) {
                int mid = l + (r - l) / 2;
                int val = getVal(mid, mountainArr);
                //if (val == target) {
                ///    System.out.println("mid: " + mid);
                //    return mid;
                //}
                if (mid - 1 >= 0 && getVal(mid - 1, mountainArr) < val && mid + 1 < len
                        && getVal(mid + 1, mountainArr) < val) {
                    top = mid;
                    break;
                } else if (mid - 1 >= 0 && getVal(mid - 1, mountainArr) < val) {
                    l = mid + 1;
                } else {
                    r = mid;
                }
            }
            System.out.println("top: " + top);
            l = 0;
            r = top;
            while (l < r) {
                int mid = l + (r - l) / 2;
                int val = getVal(mid, mountainArr);
                if (val == target) {
                    return mid;
                } else if (val > target) {
                    r = mid;
                } else {
                    l = mid + 1;
                }
            }
            if (getVal(l, mountainArr) == target) {
                return l;
            }
            l = top;
            r = len - 1;
            while (l < r) {
                int mid = l + (r - l) / 2;
                int val = getVal(mid, mountainArr);
                if (val == target) {
                    return mid;
                } else if (val < target) {
                    r = mid;
                } else {
                    l = mid + 1;
                }
            }
            if (getVal(l, mountainArr) == target) {
                return l;
            }
            return -1;
        }

        int getVal(int i, MountainArray mountainArr) {
            if (map.containsKey(i)) {
                return map.get(i);
            }
            int v = mountainArr.get(i);
            map.put(i, v);
            return v;
        }
    }

    class MountainArray {
        int[] A = {1,5,2};
        public int get(int index) {
            return A[index];
        }

        public int length() {
            return A.length;
        }
    }

    @Test
    public void test() {
        Solution s = new Solution();
        System.out.println(s.findInMountainArray(2, new MountainArray()));
//        String[] cmd = new String[7];
//        cmd[0] = "push 4";
//        cmd[1] = "pop";
//        cmd[2] = "push 3";
//        cmd[3] = "push 5";
//        cmd[4] = "push 2";
//        cmd[5] = "inc 3 1";
//        cmd[6] = "pop";
//        superStack(cmd);

    }
}
