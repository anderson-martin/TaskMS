package service;

import objectModels.basicViews.GroupBasicView;
import objectModels.basicViews.UserBasicView;
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
import service.exception.StateConflict;
import service.exchange.userGroup.*;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by rohan on 2/24/17.
 */
public class TMSServiceImpl implements TMSService {
    // Utilities
    private final HierarchyGroupDAO gruopDAO = HierarchyGroupDAOimpl.getSingleInstance();
    private final UserDAO userDAO = UserDAOimpl.getSingleInstance();
    private final TMSTaskDAO taskDAO = TMSTaskDAOImpl.getSingleInstance();
    private final TMSIssueDAO issueDAO = TMSIssueDAOimpl.getSingleInstance();
    // singleton pattern
    private static final TMSService singleInstance = new TMSServiceImpl();

    public static TMSService getSingleInstance() {
        return singleInstance;
    }

    private TMSServiceImpl() {
        // initialize, create several group
        User hr = new User("hr","hr","hr");
        hr.setStatus(User.STATUS.HR_MANAGER);
        hr.setContactDetail(new ContactDetail("lintukorventie", "02660", "ESPOO","dang@dang.com", "0465672648"));
        userDAO.registerUser(hr);

        User manager = new User("manager", "dang", "nguyen");
        manager.setContactDetail(new ContactDetail("metsalinnunretti", "02660", "ESPOO","innolight@gmail.com", "0465672648"));
        userDAO.registerUser(manager);

        User cashier = new User("cashier", "thanh", "ng");
        cashier.setContactDetail(new ContactDetail("turkismiehentie", "01330", "HELSINKI", "thanh@th.com", "0422752736"));
        userDAO.registerUser(cashier);

        User cashierLead = new User("cashierLead", "arseni", "kurilov");
        cashierLead.setContactDetail(new ContactDetail("vittukatu", "04230", "ESPOO", "arseni@th.com", "0418736671"));
        userDAO.registerUser(cashierLead);

        HierarchyGroup managerG = new HierarchyGroup("manager");
        HierarchyGroup cashierLeadG = new HierarchyGroup("cashier_lead");
        HierarchyGroup cashierG = new HierarchyGroup("cashier");
        cashierLeadG.setManagerGroup(managerG);
        cashierG.setManagerGroup(cashierLeadG);
        gruopDAO.registerGroup(managerG);
        gruopDAO.registerGroup(cashierLeadG);
        gruopDAO.registerGroup(cashierG);

        userDAO.addUserToGroup(cashierG.getId(), cashier.getId());
        userDAO.addUserToGroup(cashierLeadG.getId(),cashierLead.getId());
        userDAO.addUserToGroup(managerG.getId(), manager.getId() );
    }

    /**
     * Check if the key refer to an User with HR role
     *
     * @param key
     * @return true if the key refer to HR user,
     * false if the key refer to non existing user or existing non-HR user
     */
    private boolean isHRmanager(Credential key) {
        User.STATUS status = null;
        try {
            status = userDAO.getUserStatus(Long.parseLong(key.getKey()));
        } catch (Exception ex) {
            throw new ForbiddenException("not hr user");
        }
        if (status == null || status != User.STATUS.HR_MANAGER) {
            return false;
        }
        return true;
    }

    @Override
    public List<UserBasicView> getAllUsers(Credential key) {
        if (!isHRmanager(key)) throw new ForbiddenException("Not a HR Manager");
        return new ArrayList<>(userDAO.getUsers(UserBasicView.class));
    }

    private User fromRegister(UserRegister userRegister) {
        User user = new User(userRegister.getUserName(), userRegister.getFirstName(), userRegister.getLastName());
        user.setStatus(userRegister.getStatus());
        user.setContactDetail(userRegister.getContactDetail());
        return user;
    }
    private HierarchyGroup fromRegister(GroupRegister groupRegister) {
        return new HierarchyGroup(groupRegister.getName());
    }

    @Override
    public UserBasicView registerUser(Credential key, UserRegister userRegister) {
        if (!isHRmanager(key)) throw new ForbiddenException("Not a HR Manager");

        User user = fromRegister(userRegister);

        try {
            userDAO.registerUser(user);
        } catch (RuntimeException ex) {
            if (userDAO.isRegisteredUser(user.getUserName())) {
                throw new StateConflict("userName already exists");
            } else throw new BadRequestException("there is error in your request, " +
                    ", you should provide valid information");
        }
        return UserBasicView.generate(user);
    }

    @Override
    public DeactivationEffect deactivateUser(Credential key, Long userId) {
        if (!isHRmanager(key)) throw new ForbiddenException("Not a HR Manager");
        if (!userDAO.isRegisteredUser(userId)) throw new BadRequestException("Invalid User Id");

        User user = userDAO.getUser(userId);
        user.setStatus(User.STATUS.CLOSED);

        Set<Long> affectedGroupIds = new HashSet<>();
        user.getGroups().forEach(group -> affectedGroupIds.add(group.getId()));
        user.setGroups(new HashSet<>());

        userDAO.updateUser(user);
        return new DeactivationEffect(new HashSet<>(), affectedGroupIds);
    }

    /* For valid id*/
    private UserView generateUserView(long userId) {
        UserView userView = new UserView();
        User user = userDAO.getUser(userId);
        userView.setId(user.getId());
        userView.setUserName(user.getUserName());
        userView.setFirstName(user.getFirstName());
        userView.setLastName(user.getLastName());
        userView.setContactDetails(user.getContactDetail());
        userView.setGroups(new HashSet<>());
        user.getGroups().forEach( group ->
            userView.getGroups().add(generateGroupView(group)));
        return userView;
    }
    /* For persistent group only*/
    private GroupView generateGroupView(HierarchyGroup group) {
        GroupView groupView = new GroupView();
        groupView.setId(group.getId());
        groupView.setName(group.getName());
        groupView.setStatus(group.getStatus());
        groupView.setManagerGroup(GroupBasicView.generate(group.getManagerGroup()));
        groupView.setSubordinateGroups(new HashSet<>());
        group.getSubordinateGroups().forEach( sub ->
                groupView.getSubordinateGroups().add(GroupBasicView.generate(sub)));
        groupView.setUsers(userDAO.getUsersInGroup(group.getId(), UserBasicView.class));
        return groupView;
    }
    /* For valid id*/
    private GroupView generateGroupView(long groupId) {
        return generateGroupView(gruopDAO.getGroup(groupId));
    }

    @Override
    public UserView updateUser(Credential key, long userId, UserUpdater userUpdater) {
        // checking userUpdaterValidity
        if (!isHRmanager(key)) throw new ForbiddenException("Not a HR Manager");
        if (!userDAO.isRegisteredUser(userId)) throw new BadRequestException("Invalid User Id");
        if(userUpdater.getFirstName() == null || userUpdater.getLastName() == null || userUpdater.getStatus() == null)
            throw new BadRequestException("Cannot update invalid information!");
        if(userUpdater.getGroups() != null) {
            userUpdater.getGroups().forEach(groupId -> {
                if(!gruopDAO.isRegisteredGroup(groupId)) throw new BadRequestException("Can not put user to invalid group");
            });
        }
        // now begin update
        User user = userDAO.getUser(userId);
        // > update group
        user.setGroups(new HashSet<>());
        if(userUpdater.getGroups() != null) {
            userUpdater.getGroups().forEach(groupId -> {
                HierarchyGroup hierarchyGroup = new HierarchyGroup();
                hierarchyGroup.setId(groupId);
                user.getGroups().add(hierarchyGroup);
            });
        }
        user.setFirstName(userUpdater.getFirstName());
        user.setLastName(userUpdater.getLastName());
        user.setStatus(userUpdater.getStatus());
        user.setContactDetail(userUpdater.getContactDetails());

        userDAO.registerUser(user);

        return generateUserView(user.getId());
    }

    @Override
    public UserView getUserInfo(Credential key, long userId) {
        if (!isHRmanager(key)) throw new ForbiddenException("Not a HR Manager");
        if (!userDAO.isRegisteredUser(userId)) throw new BadRequestException("Invalid User Id");
        return generateUserView(userId);
    }

    @Override
    public List<GroupBasicView> getAllGroups(Credential key) {
        if (!isHRmanager(key)) throw new ForbiddenException("Not a HR Manager");
        return new ArrayList<>(gruopDAO.getGroups(GroupBasicView.class));
    }

    @Override
    public GroupBasicView registerGroup(Credential key, GroupRegister groupRegister) {
        if (!isHRmanager(key)) throw new ForbiddenException("Not a HR Manager");
        HierarchyGroup toBeRegistered = fromRegister(groupRegister);
        try {
            gruopDAO.registerGroup(toBeRegistered);
        } catch (RuntimeException ex) {
            if (gruopDAO.isRegisteredGroup(toBeRegistered.getName())) {
                throw new StateConflict("group name already exists");
            } else throw new BadRequestException("there is error in your request, " +
                    ", you should provide valid information");
        }
        return GroupBasicView.generate(toBeRegistered);
    }

    // problem: this is not atomic amount of work :((
    @Override
    public DeactivationEffect deactivateGroup(Credential key, Long groupId) {
        if (!isHRmanager(key)) throw new ForbiddenException("Not a HR Manager");
        if (!gruopDAO.isRegisteredGroup(groupId)) throw new BadRequestException("invalid group id");

        DeactivationEffect deef = new DeactivationEffect();
        deef.setUsers(userDAO.getUsersInGroup(groupId, Long.class));
        Set<Long> subordinateIds = gruopDAO.getSubordinateGroups(Long.class, groupId);
        deef.setGroups(subordinateIds);

        Long managerId = gruopDAO.getManagerGroup(Long.class, groupId);
        if( managerId != null) {
            deef.getGroups().add(managerId);
            gruopDAO.unsetManagerGroup(groupId);
        }
        gruopDAO.unsetManagerGroup(convert(subordinateIds));
        gruopDAO.setGroupStatus(groupId, HierarchyGroup.STATUS.CLOSED);
        userDAO.removeAllUsersFromGroup(groupId);

        return deef;
    }
    private long[] convert(Set<Long> set ) {
        long[] array = new long[set.size()];
        int i = 0;
        for(Long l : set) {
            array[i++] = l;
        }
        return array;
    }

    @Override
    public GroupView updateGroup(Credential key, long groupId, GroupUpdater gu) {
        if (!isHRmanager(key)) throw new ForbiddenException("Not a HR Manager");
        if (!gruopDAO.isRegisteredGroup(groupId)) throw new BadRequestException("invalid group id");
        // checking groupUpdater validity
        if(gu.getSubordinateGroups() != null && !gu.getSubordinateGroups().isEmpty()) {
            gu.getSubordinateGroups().forEach(groupIdd -> {
                if(!gruopDAO.isRegisteredGroup(groupIdd)) throw new BadRequestException("invalid subordinate group id");
            });
        }
        if(gu.getUsers() != null && !gu.getUsers().isEmpty()) {
            gu.getUsers().forEach(userId -> {
                if(!userDAO.isRegisteredUser(userId)) throw new BadRequestException("invalid subordinate group id");
            });
        }
        if(gu.getManagerGroup() != 0 && !gruopDAO.isRegisteredGroup(gu.getManagerGroup()))
            throw new BadRequestException("invalid manager group id");
        if(gu.getName() == null || gu.getName().trim().length() == 0)
            throw new BadRequestException("invalid group name");

        // update
        // > update subordinates
        HierarchyGroup group = gruopDAO.getGroup(groupId);
        group.setSubordinateGroups( new HashSet<>());
        if(gu.getSubordinateGroups() != null && !gu.getSubordinateGroups().isEmpty()) {
            gu.getSubordinateGroups().forEach(groupIdd -> {
                HierarchyGroup sub = new HierarchyGroup();
                sub.setId(groupIdd);
                group.getSubordinateGroups().add(sub);
            });
        }
        // update manager
        if(gu.getManagerGroup() == 0) {
            group.setManagerGroup(null);
        } else {
            HierarchyGroup manager = new HierarchyGroup();
            manager.setId(gu.getManagerGroup());
            group.setManagerGroup(manager);
        }
        group.setName(gu.getName());
        gruopDAO.updateGroup(group);
        // update user
        if(gu.getUsers() != null && !gu.getUsers().isEmpty()) {
            gu.getUsers().forEach(userId -> userDAO.removeUserFromGroup(group.getId(), userId));
        }

        return generateGroupView(groupId);
    }

    @Override
    public GroupView getGroupInfo(Credential key, long groupId) {
        if (!isHRmanager(key)) throw new ForbiddenException("Not a HR Manager");
        if (!gruopDAO.isRegisteredGroup(groupId)) throw new BadRequestException("invalid group id");
        return generateGroupView(groupId);
    }
}
