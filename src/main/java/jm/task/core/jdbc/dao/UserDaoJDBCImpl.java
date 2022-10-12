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
        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS USER"
                    + "(id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,"
                    + "name VARCHAR(45) NOT NULL,"
                    + "lastName VARCHAR(45) NOT NULL,"
                    + "age INT NOT NULL)");
            System.out.println("Таблица создана!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Таблица НЕ создана!");
        }
    }

    public void dropUsersTable() {
        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS USER");
            System.out.println("Таблица удалена!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Таблица НЕ удалена!");
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (
                Connection connection = Util.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO USER (NAME, LASTNAME, AGE) VALUES(?, ?, ?)");) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.println("User с именем - " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("User " + name + " " + lastName + " не может быть добален в таблицу!");
        }
    }

    public void removeUserById(long id) {
        try (
                Connection connection = Util.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "DELETE FROM USER WHERE ID=?")) {

            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("User с ID = " + id + " не может быть удален!");
        }
    }

    public List<User> getAllUsers() {
        List<User> listOfUsers = new ArrayList<>();
        try (
                Connection connection = Util.getConnection(); Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM USER");) {

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
            System.out.println("Ошибка чтения из таблицы!");
        }
        return listOfUsers;
    }

    public void cleanUsersTable() {
        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate("TRUNCATE TABLE USER");
            System.out.println("Записи пользователей удалены в таблица User!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Таблица не может быть очищена!");
        }
    }
}

