package objectModels;

import service.dao.userGroup.UserDAO;
import service.dao.userGroup.UserDAOimpl;


/**
 * Created by rohan on 2/19/17.
 */
public class Main {
    public static final UserDAO userDAO = UserDAOimpl.getSingleInstance();

    // testing with different view

    public static <T> void whatIsThis(Class<T> param) {
        if(param == Object.class) {
            System.out.println("got an object");
        } else if (param == String.class) {
            System.out.println("got a string object");
        } else {
            System.out.println("Vittu");
        }
    }

    public static void main(String[] args ) {
//        long id = userDAO.registerUser(new User("dangng", "dang","nguyen"));
//        User user = JPASessionUtil.getEntityManager().find(User.class, id);
//        UserBasicView userBasicView = JPASessionUtil.getEntityManager().find(UserBasicView.class, id);
//        System.out.println(user);
//        System.out.println(userBasicView);
//
//        long id2 = HierarchyGroupDAOimpl.getSingleInstance().registerGroup(new HierarchyGroup("dangng"));
//        HierarchyGroup hierarchyGroup = JPASessionUtil.getEntityManager().find(HierarchyGroup.class, id2);
//        GroupBasicView groupBasicView = JPASessionUtil.getEntityManager().find(GroupBasicView.class, id2);
//
//        System.out.println(hierarchyGroup);
//        System.out.println(groupBasicView);
        whatIsThis(String.class);
    }
}
