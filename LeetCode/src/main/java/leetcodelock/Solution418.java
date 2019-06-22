package leetcodelock;

public class Solution418 {
	// KEY_CODE
	// 418. Sentence Screen Fitting
	// Given a rows x cols screen and a sentence represented by a list of non-empty words,
	// find how many times the given sentence can be fitted on the screen.
	public int wordsTyping(String[] sentence, int rows, int cols) {
		int n = sentence.length;
		// for each word, when it starts a line, find the start word of next line
		// and the repeat times of current line
		int[] nextStart = new int[n];
		int[] times = new int[n];

		for (int i = 0; i < n; i++) {
			int cur = i, cnt = 0;
			int curLen = 0;
			while (curLen + sentence[cur].length() <= cols) {
				curLen += sentence[cur].length() + 1;
				cur += 1;
				if (cur == n) {
					cnt++;
					cur = 0;
				}
			}
			nextStart[i] = cur;
			times[i] = cnt;
		}

		int res = 0, cur = 0;
		for (int i = 0; i < rows; i++) {
			res += times[cur];
			cur = nextStart[cur];
		}
		return res;
	}

	// V1, strait forward, too slow
	public int wordsTyping1(String[] sentence, int rows, int cols) {
		int cur = 0, idx = 0;
		int row = 0, res = 0;
		while (row < rows) {
			if (cur + sentence[idx].length() > cols) {
				row++;
				cur = 0;
				continue;
			} else {
				cur += sentence[idx].length() + 1;
			}
			if (idx == sentence.length - 1) {
				res++;
				idx = 0;
			} else {
				idx++;
			}
		}
		return res;
	}

	// V2
	public int wordsTyping2(String[] sentence, int rows, int cols) {
		String s = String.join(" ", sentence) + " ";
		int pos = 0, len = s.length();

		for (int i = 0; i < rows; i++) {
			pos += cols;
			// pos-- means wasted extra space
			while (pos >= 0 && s.charAt(pos % len) != ' ') {
				pos--;
			}
			pos++;
		}
		return pos / len;
	}
}
