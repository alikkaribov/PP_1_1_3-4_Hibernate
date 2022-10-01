package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        try {
            Session session = Util.getInstance().getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            String create_table = "CREATE TABLE IF NOT EXISTS USER"
                    + "(id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,"
                    + "name VARCHAR(45) NOT NULL,"
                    + "lastName VARCHAR(45) NOT NULL,"
                    + "age INT NOT NULL)";
            session.createSQLQuery(create_table).executeUpdate();
            transaction.commit();
            System.out.println("Таблица создана!");
        } catch (Exception ignored) {
            System.out.println("Таблица уже существует!");
        }
    }

    @Override
    public void dropUsersTable() {
        Session session = Util.getInstance().getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        String drop_table = "DROP TABLE IF EXISTS USER";
        session.createSQLQuery(drop_table).executeUpdate();
        transaction.commit();
        System.out.println("Таблица удалена!");
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = Util.getInstance().getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        User user = new User(name, lastName, age);
        session.save(user);
        transaction.commit();
        System.out.println("User с именем – " + name + " добавлен в базу данных.");
    }

    @Override
    public void removeUserById(long id) {
        Session session = Util.getInstance().getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        User user = session.load(User.class, id);
        if (user != null) {
            session.delete(user);
        }
        transaction.commit();
        System.out.println("User с ID = " + id + " удален.");
    }

    @Override
    public List<User> getAllUsers() {
        Session session = Util.getInstance().getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        List listOfUsers = session.createQuery("From User").list();
        transaction.commit();
        session.close();
        return listOfUsers;
    }

    @Override
    public void cleanUsersTable() {
        Session session = Util.getInstance().getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        String delete_all_users = "TRUNCATE TABLE USER";
        session.createSQLQuery(delete_all_users).executeUpdate();
        transaction.commit();
        System.out.println("Записи пользователей удалены в таблица User!");
    }
}
