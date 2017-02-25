package service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by rohan on 2/25/17.
 */
public class Main {
    public static void main(String[] args) {
        List<Integer> ids = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        boolean bool = ids.stream().anyMatch(id -> id == 1);
        System.out.println(bool);
    }
}
