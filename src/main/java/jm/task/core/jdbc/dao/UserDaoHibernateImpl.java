package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {
    }

    private SessionFactory factory = new Util().getInstance().getSessionFactory();

    @Override
    public void createUsersTable() {
        Transaction transaction = null;

        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery("CREATE TABLE IF NOT EXISTS USER"
                    + "(id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,"
                    + "name VARCHAR(45) NOT NULL,"
                    + "lastName VARCHAR(45) NOT NULL,"
                    + "age INT NOT NULL)").addEntity(User.class).executeUpdate();
            transaction.commit();
            System.out.println("Таблица создана!");
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Таблица НЕ создана!");
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        Transaction transaction = null;

        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS USER").addEntity(User.class).executeUpdate();
            transaction.commit();
            System.out.println("Таблица удалена!");
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Таблица НЕ удалена!");
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;

        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            session.save(new User(name, lastName, age));
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;

        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.load(User.class, id));
            transaction.commit();
            System.out.println("User с ID = " + id + " удален.");

        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("User с ID = " + id + " НЕ может быть удален!");
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        Transaction transaction = null;
        List listOfUsers = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            listOfUsers = session.createQuery("From User").list();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Ошибка вывода списка пользователей");
            e.printStackTrace();
        }
        return listOfUsers;
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;

        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery("TRUNCATE TABLE USER").executeUpdate();
            transaction.commit();
            System.out.println("Записи пользователей удалены в таблица User!");
        } catch (HibernateException e) {

            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Таблица не может быть очищена!");
            e.printStackTrace();
        }
    }
}
