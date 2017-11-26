package oca;

//import java.util.Collections;
//import static java.util.Collections.sort;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;
//The public type Chap0 must be defined in its own file
//public class Chap0 {}

//!-- only public, abstract & final are permitted
public class Chap1 {
    String first = "Theodore";
    String last = "Moose";
    // !--
    String full = first + last;

    // !--
    // import java.util.*;
    // import java.sql.Date;
    // this is sql.Date
    // If you explicitly import a class name, it takes precedence over any wildcards present.
    Date dsql = new Date(0);
    Random date = new Random();

    // !--
    // System.out.println("Snowy"); // compile error
    // Code blocks appear outside a method are called instance initializers.
    {
        System.out.println("Snowy");
        System.out.println("abcabc".replace('a', 'A')); // AbcAbc
        System.out.println("abcabc".replace("a", "A")); // AbcAbc
        StringBuilder sb = new StringBuilder("abc");
        sb.insert(3, "-"); // abc-
        int[][] differentSize = { { 1, 4 }, { 3 }, { 9, 8, 7 } };
        int[][] args = new int[4][];
        args[0] = new int[5];
        args[1] = new int[3];

        LocalDate date = LocalDate.of(2015, Month.JANUARY, 20);
        System.out.println(date.getYear() + " " + date.getMonth() + " " + date.getDayOfMonth());
        date.equals("S"); // equlas() can be different object type
        // !--
        // String concat
        String s = "S";
        s = s + false + date;
        // Date dsql2 = new Date(0);
        // System.out.println(dsql == date1);

        List<Integer> ages = new ArrayList<>();
        // ages.add(null);
        for (int age : ages) {
            System.out.print(age); // run time NullPointerException
        }
    }

    // The literal 3123456789 of type int is out of range
    // long max = 3123456789;
    long max = 3123456789L;
    // Type mismatch: cannot convert from double to float
    // float y = 2.1;
    float yfloat = 2.1F;

    long x = 5;
    long y = (x = 3);
    long y11 = x = 3;

    byte b1 = 30;
    byte b2 = 40;

    // !--
    // the cast operator has the highest precedence
    // byte b3 = (byte) b1 + b2;
    byte b4 = (byte) (b1 + b2);

    int returnTernary() {
        int x = 5;
        int y = 10;
        int z = 20;
        x = x > 0 ? y++ : z++;
        System.out.println(x + ", " + y + ", " + z + ", ");
        return x;
    }

    int getSortOrder(String firstName, final String lastName) {
        String middleName = "Patricia";
        final String suffix = "JRFinal";
        switch (firstName) {
        // case expressions must be constant expressions
        // case middleName:
        // case lastName:
        case "Test":
        case suffix:
            return 52;
        default:
            return -1;
        }
    }

    @Test
    public void test() {
        returnTernary();
        // !--
        // int x = 0;
        for (long y = 0, x = 4; x < 5 && y < 10; x++, y++) { // DOES NOT COMPILE
            System.out.print(x + " ");
        }
    }

}
