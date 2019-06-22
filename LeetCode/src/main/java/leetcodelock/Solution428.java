package leetcodelock;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

// 428. Serialize and Deserialize N-ary Tree
public class Solution428 {
	class Node {
		public int val;
		public List<Node> children;

		public Node() {
		}

		public Node(int _val, List<Node> _children) {
			val = _val;
			children = _children;
		}
	};

	class Codec {
		String NN = "X";
		String spliter = ",";

		// Encodes a tree to a single string.
		public String serialize(Node root) {
			StringBuilder sb = new StringBuilder();
			buildString(root, sb);
			return sb.toString();
		}

		private void buildString(Node node, StringBuilder sb) {
			if (node == null) {
				sb.append(NN);
				sb.append(spliter);
			} else {
				sb.append(node.val);
				sb.append(spliter);
				sb.append(node.children.size());
				sb.append(spliter);
				for (Node child : node.children) {
					buildString(child, sb);
				}
			}
		}

		// Decodes your encoded data to tree.
		public Node deserialize(String data) {
			Deque<String> deque = new ArrayDeque<>(Arrays.asList(data.split(spliter)));
			return buildTree(deque);
		}

		private Node buildTree(Deque<String> deque) {
			String s1 = deque.removeFirst();
			if (s1.equals(NN))
				return null;

			int rootVal = Integer.valueOf(s1);
			int childrenNumber = Integer.valueOf(deque.removeFirst());

			Node root = new Node(rootVal, new ArrayList<>());
			for (int i = 0; i < childrenNumber; i++) {
				root.children.add(buildTree(deque));
			}
			return root;
		}
	}
}
