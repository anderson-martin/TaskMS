package service.dao.userGroup;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rohan on 2/14/17.
 */
public class Main {
//    public static void read(long id) {
//        System.out.println("MOI " + id);
//    }
private static List<Long> converts(long... nums) {
    List<Long> longs = new ArrayList<>();
    for(long num : nums) {
        longs.add(num);
    }
    return longs;
}

    public static void read(long id, long... ids) {
        System.out.println("HEI");
        System.out.println("1st args:" + id);
        System.out.println(converts(ids));
    }
    public static void main(String[] args) {
        read(1,null);
    }
}
