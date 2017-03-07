package service;

import objectModels.userGroup.ContactDetail;
import objectModels.userGroup.HierarchyGroup;
import objectModels.userGroup.User;
import service.dao.msg.TMSIssueDAO;
import service.dao.msg.TMSIssueDAOimpl;
import service.dao.msg.TMSTaskDAO;
import service.dao.msg.TMSTaskDAOImpl;
import service.dao.userGroup.HierarchyGroupDAO;
import service.dao.userGroup.HierarchyGroupDAOimpl;
import service.dao.userGroup.UserDAO;
import service.dao.userGroup.UserDAOimpl;

import java.util.Arrays;
import java.util.List;

public class PopulateDatabases {
    private static final HierarchyGroupDAO groupDAO = HierarchyGroupDAOimpl.getSingleInstance();
    private static final UserDAO userDAO = UserDAOimpl.getSingleInstance();
    private static final TMSTaskDAO taskDAO = TMSTaskDAOImpl.getSingleInstance();
    private static final TMSIssueDAO issueDAO = TMSIssueDAOimpl.getSingleInstance();

    private static final String manager = "Salad Line Manager";
    private static final String rawMaterial = "Raw Material";
    private static final String trimming = "Trimming";
    private static final String hygiene = "Hygiene";
    private static final String packing = "Packing";
    private static final String warehouse = "Warehouse";
    private static final List<String> groupNames = Arrays.asList(manager, rawMaterial, trimming, hygiene, packing, warehouse);


    public PopulateDatabases() {
    }

    public void populateGroupUsers() {
        ContactDetail contactDetail = new ContactDetail("vahan maantie 6", "02600", "Helsinki", "user@metropolia.fi", "0465672626");

        List<User> userInManagerGroup = Arrays.asList(
                new User("dangNg", "Dang", "Nguyen"),
                new User("arseniKu", "Arseni", "Kurilov"),
                new User("thanhNg", "Thanh", "Nguyen"),
                new User("martinKl", "Martin", "Klovar")
        );
        userInManagerGroup.forEach( user -> {
            user.setContactDetail(contactDetail);
            userDAO.registerUser(user);
        });
        long managerGroupId = groupDAO.registerGroup(new HierarchyGroup(groupNames.get(0)));
        userInManagerGroup.forEach(user -> userDAO.addUserToGroup(managerGroupId, user.getId()));

        for(int i = 1; i < groupNames.size(); i++) {
            long operatorId = groupDAO.registerGroup(new HierarchyGroup(groupNames.get(i) + " Operator"));
            groupDAO.setManagerGroup(managerGroupId, operatorId);

            User operator1 = new User("operator" + (2*i-1), "Ope", "Rator" );
            User operator2 = new User("operator" + (2*i), "Ope", "Rator" );
            operator1.setContactDetail(contactDetail);
            operator2.setContactDetail(contactDetail);
            userDAO.registerUser(operator1);
            userDAO.registerUser(operator2);
            userDAO.addUserToGroup(operatorId, operator1.getId());
            userDAO.addUserToGroup(operatorId, operator2.getId());

            long workerId  = groupDAO.registerGroup(new HierarchyGroup(groupNames.get(i) + " Worker"));
            groupDAO.setManagerGroup(operatorId, workerId);

            User worker1 = new User("worker" + (2*i-1), "Wor", "Ker" );
            User worker2 = new User("worker" + (2*i), "Wor", "Ker" );
            worker1.setContactDetail(contactDetail);
            worker2.setContactDetail(contactDetail);
            userDAO.registerUser(worker1);
            userDAO.registerUser(worker2);
            userDAO.addUserToGroup(workerId, worker1.getId());
            userDAO.addUserToGroup(workerId, worker2.getId());

        }
    }

    public static void main(String[] args) {
        User hr = userDAO.getUser(1);
        hr.setStatus(User.STATUS.HR_MANAGER);
        userDAO.updateUser(hr);
    }

}
