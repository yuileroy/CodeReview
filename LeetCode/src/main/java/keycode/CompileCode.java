package keycode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class CompileCode {

    public int canCompleteCircuit(int[] gas, int[] cost) {
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

    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int m = nums1.length;
        int n = nums2.length;
        int mid = (m + n) / 2;
        if ((m + n) % 2 == 0) {
            return 0.5 * (kth(nums1, 0, nums2, 0, mid) + kth(nums1, 0, nums2, 0, mid + 1));
        }
        return (double) kth(nums1, 0, nums2, 0, mid + 1);
    }

    int kth(int arr1[], int start1, int arr2[], int start2, int k) {
        System.out.println(" start1: " + start1 + " start2: " + start2 + " k: " + k);
        int m = arr1.length - start1, n = arr2.length - start2;
        if (m > n)
            return kth(arr2, start2, arr1, start1, k);
        if (m == 0)
            return arr2[k - 1 + start2];
        if (k == 1)
            return Math.min(arr1[start1], arr2[start2]);

        // divide and conquer, n >= m >= 1, k >= 2
        int i = Math.min(m, k / 2);
        int j = Math.min(n, k / 2);

        if (arr1[i - 1 + start1] > arr2[j - 1 + start2]) {
            // remove first j elements from arr2 by update start2
            return kth(arr1, start1, arr2, start2 + j, k - j);
        } else {
            return kth(arr1, start1 + i, arr2, start2, k - i);
        }
    }

    @Test
    public void test() {
        int[] A = { 1 };
        int[] B = { 2, 3, 4, 5, 6 };
        findMedianSortedArrays(A, B);
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
    }
}