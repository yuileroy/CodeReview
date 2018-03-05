package javacode.code;

import java.util.Scanner;

public class ScannerTest {
    
    
    
    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);

        int lines = Integer.parseInt(scanner.nextLine());
        while (lines-- > 0) {
            String input = scanner.nextLine();
            System.out.println("input : " + input);
        }

        scanner.close();
    }
}
