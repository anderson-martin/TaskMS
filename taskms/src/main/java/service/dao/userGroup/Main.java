package service.dao.userGroup;

import java.util.Arrays;

/**
 * Created by rohan on 2/14/17.
 */
public class Main {
//    public static void read(long id) {
//        System.out.println("MOI " + id);
//    }
    public static void read(long id, long... ids) {
        System.out.println("HEI");
        System.out.println("1st args:" + id);
//        System.out.println("2nd args: " + (ids.length));
        System.out.println(Arrays.asList(ids, 1, "a"));
//        for(long s : ids) {
//            System.out.println(s);
//        }
    }
    public static void main(String[] args) {
        read(1,null);
    }
}
