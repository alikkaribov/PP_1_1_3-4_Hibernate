package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }
    private SessionFactory factory = new Util().getSessionFactory();
    @Override
    public void createUsersTable() {
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("CREATE TABLE IF NOT EXISTS USER"
                    + "(id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,"
                    + "name VARCHAR(45) NOT NULL,"
                    + "lastName VARCHAR(45) NOT NULL,"
                    + "age INT NOT NULL)").executeUpdate();
            session.getTransaction().commit();
            System.out.println("Таблица создана!");
        } catch (Exception ignored) {
            System.out.println("Ошибка создания таблицы");
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS USER").executeUpdate();
            session.getTransaction().commit();
            System.out.println("Таблица удалена!");
        } catch (Exception ignored) {
            System.out.println("Таблицы не существует");
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            session.save(new User(name, lastName, age));
            session.getTransaction().commit();
            System.out.println("User с именем – " + name + " добавлен в базу данных.");
        } catch (Exception ignored) {
            System.out.println("User с именем – " + name + "уже существует!");
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            User user = session.load(User.class, id);
            if (user != null) {
                session.delete(user);
            }
            session.getTransaction().commit();
            System.out.println("User с ID = " + id + " удален.");
        } catch (Exception ignored) {
            System.out.println("User с ID = " + id + "уже существует!");
        }
    }

    @Override
    public List<User> getAllUsers() {
        List listOfUsers = null;
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            listOfUsers = session.createQuery("From User").list();
            session.getTransaction().commit();
        } catch (Exception ignored) {
            System.out.println("Ошибка вывода списка пользователей");
        }
        return listOfUsers;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("TRUNCATE TABLE USER").executeUpdate();
            session.getTransaction().commit();
            System.out.println("Записи пользователей удалены в таблица User!");
        } catch (Exception ignored) {
            System.out.println("Ошибка удаления записей!");
        }
    }
}
