package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();

        userService.createUsersTable();

        userService.saveUser("Hello", "Peter", (byte) 57);
        userService.saveUser("Arsen", "Venger", (byte) 71);
        userService.saveUser("Ivan", "Ivanov", (byte) 18);
        userService.saveUser("Ivar", "Boneless", (byte) 22);


        List<User> users = userService.getAllUsers();
        for (User user : users) {
            System.out.println(user);
        }

        userService.cleanUsersTable();

        userService.dropUsersTable();
    }
}
