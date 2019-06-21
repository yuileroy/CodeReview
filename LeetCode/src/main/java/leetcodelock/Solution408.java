package leetcodelock;

public class Solution408 {

    public boolean validWordAbbreviation(String word, String abbr) {
        int i = 0, j = 0;

        while (i < word.length() && j < abbr.length()) {
            if (!Character.isDigit(abbr.charAt(j))) {
                if (word.charAt(i) != abbr.charAt(j)) {
                    return false;
                }
                i++;
                j++;
            } else {
                if (abbr.charAt(j) == '0') {
                    return false;
                }
                int n = 0;
                while (j < abbr.length() && Character.isDigit(abbr.charAt(j))) {
                    n = n * 10 + abbr.charAt(j) - '0';
                    j++;
                }
                i += n;
            }
        }

        return i == word.length() && j == abbr.length();
    }

    // V2
    public boolean validWordAbbreviation2(String word, String abbr) {
        int idx = 0;
        for (int i = 0; i < abbr.length(); i++) {
            // word reached end but abbr doesn't
            if (idx >= word.length()) {
                return false;
            }
            char c = abbr.charAt(i);
            if (c == '0') {
                return false;
            }
            if (Character.isDigit(c)) {
                int n = 0;
                while (i < abbr.length() && Character.isDigit(abbr.charAt(i))) {
                    // ! n = n * 10 + c - '0';
                    n = n * 10 + abbr.charAt(i) - '0';
                    i++;
                }
                i--;
                idx += n;
            } else {
                if (word.charAt(idx) != abbr.charAt(i)) {
                    return false;
                }
                idx += 1;
            }
        }
        return idx == word.length();
    }
}
