package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String create_table = "CREATE TABLE IF NOT EXISTS USER"
                + "(id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,"
                + "name VARCHAR(45) NOT NULL,"
                + "lastName VARCHAR(45) NOT NULL,"
                + "age INT NOT NULL)";

        try(Connection connection = Util.getInstance().getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(create_table);
            System.out.println("Таблица создана!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        String drop_table = "DROP TABLE IF EXISTS USER";

        try(Connection connection = Util.getInstance().getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(drop_table);
            System.out.println("Таблица удалена!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String save_user = "INSERT INTO USER (NAME, LASTNAME, AGE) VALUES(?, ?, ?)";

        try(Connection connection = Util.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(save_user);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.println("User с именем - " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        String delete_user = "DELETE FROM USER WHERE ID=?";

        try(Connection connection = Util.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(delete_user);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> listOfUsers = new ArrayList<>();
        String select_user = "SELECT * FROM USER";

        try(Connection connection = Util.getInstance().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(select_user);

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("ID"));
                user.setName(resultSet.getString("NAME"));
                user.setLastName(resultSet.getString("LASTNAME"));
                user.setAge(resultSet.getByte("AGE"));
                listOfUsers.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfUsers;
    }

    public void cleanUsersTable() {
        String delete_all_users = "TRUNCATE TABLE USER";
        try(Connection connection = Util.getInstance().getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(delete_all_users);
            System.out.println("Записи пользователей удалены в таблица User!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

