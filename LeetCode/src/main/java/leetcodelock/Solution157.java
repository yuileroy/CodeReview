package leetcodelock;

/**
 * 157. Read N Characters Given Read4
 */
public class Solution157 {

}

class Reader4 {
    int read4(char[] buf) {
        // unimplemented
        return 0;
    }
}

class Solution extends Reader4 {
    /**
     * @param buf
     *            Destination buffer
     * @param n
     *            Maximum number of characters to read
     * @return The number of characters read
     */
    public int read(char[] buf, int n) {
        int res = 0;
        boolean end = false;
        char[] tmp = new char[4];
        while (!end && res < n) {
            int val = read4(tmp);
            if (val < 4) {
                end = true;
            }
            int addTo = Math.min(n - res, val);
            for (int i = 0; i < addTo; i++) {
                buf[res + i] = tmp[i];
            }
            res += addTo;
        }
        return res;
    }
}

/*
 * The read4 API is defined in the parent class Reader4. int read4(char[] buf);
 */

// Below is a high level example of how read4 works:

// File file("abcdefghijk"); // File is "abcdefghijk", initially file pointer (fp) points to 'a'
// char[] buf = new char[4]; // Create buffer with enough space to store characters
// read4(buf); // read4 returns 4. Now buf = "abcd", fp points to 'e'
// read4(buf); // read4 returns 4. Now buf = "efgh", fp points to 'i'
// read4(buf); // read4 returns 3. Now buf = "ijk", fp points to end of file

class Solution258 extends Reader4 {
    /**
     * @param buf
     *            Destination buffer
     * @param n
     *            Maximum number of characters to read
     * @return The number of characters read
     */
    private char[] tmp = new char[4];
    private int preIdx = 0;
    private int preVal = 0;

    public int read(char[] buf, int n) {
        int res = 0;
        while (res < n) {
            if (preIdx < preVal) {
                buf[res++] = tmp[preIdx++];
            } else {
                preVal = read4(tmp);
                preIdx = 0;
                if (preVal == 0) {
                    break;
                }
            }
        }
        return res;
    }

    public int read0(char[] buf, int n) {
        int res = 0;
        while (res < n && preIdx < preVal) {
            buf[res++] = tmp[preIdx++];
        }
        boolean end = false;
        while (!end && res < n) {
            preVal = read4(tmp);
            if (preVal < 4) {
                end = true;
            }
            int addTo = Math.min(n - res, preVal);
            for (int i = 0; i < addTo; i++) {
                buf[res + i] = tmp[i];
            }
            preIdx = addTo;
            res += addTo;
        }
        return res;
    }
}