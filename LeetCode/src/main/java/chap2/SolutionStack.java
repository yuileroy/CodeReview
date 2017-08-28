package chap2;

import java.util.Scanner;

public class SolutionStack {

    void stacks() {
	Node dummy = new Node(0);
	int depth = 0;
	Node curr = dummy;
	Scanner sc = new Scanner(System.in);
	String token = "";
	while (sc.hasNext()) {
	    token = sc.next();
	    if (token.equals("#")) {
		System.out.println(depth);
	    } else if (token.equals("?")) {
		System.out.println(curr.val);
	    } else if (token.equals("-")) {
		System.out.println(curr.val);
		Node pop = curr;
		curr = curr.prev;
		curr.next = null;
		pop.prev = null;
		depth--;
	    } else {
		Node newN = new Node(Long.parseLong(token));
		curr.next = newN;
		newN.prev = curr;
		curr = newN;
		depth++;
		System.out.println(depth);
	    }
	}
	sc.close();
    }

}

class Node {
    long val;
    Node prev;
    Node next;

    Node(long x) {
	val = x;
    }
}
