package service.dao.userGroup;

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
        System.out.println("2nd args: " + (ids.length));
        for(long s : ids) {
            System.out.println(s);
        }
    }
    public static void main(String[] args) {
        read(1,1,2,3,4,5,6,7,6);
    }
}
