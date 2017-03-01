package service;

import org.junit.jupiter.api.Test;
import service.dao.msg.TMSTaskDAO;
import service.dao.msg.TMSTaskDAOImpl;
import service.dao.userGroup.UserDAO;
import service.dao.userGroup.UserDAOimpl;
import service.exchange.msg.TaskView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by rohan on 3/1/17.
 */
class TMSServiceImplTest {
    private static final TMSService service = TMSServiceImpl.getSingleInstance();
    private static final UserDAO userDAO = UserDAOimpl.getSingleInstance();
    private static final TMSTaskDAO taskDAO = TMSTaskDAOImpl.getSingleInstance();
    @Test
    void getTasks() {
        long userId = 4;
        List<TaskView> tasks = new ArrayList<>();
        userDAO.getGroupsForUser(userId, Long.class).forEach(groupId ->
                tasks.addAll(taskDAO.getGroupSentTasks(groupId).stream()
                        .map(TaskView::generate).collect(Collectors.toList())));

        tasks.addAll(taskDAO.getUserReceivedTasks(userId).stream().map(TaskView::generate).collect(Collectors.toList()));
        System.out.println(tasks.size());
    }

}