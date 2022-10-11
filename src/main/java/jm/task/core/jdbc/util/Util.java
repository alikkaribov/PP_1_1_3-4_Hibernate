package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.LogManager;

public class Util {
    private static final String URL = "jdbc:mysql://localhost:3306/test?autoReconnect=true&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "кщще";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static Connection connection; private static volatile Util instance;

    private static final AtomicBoolean isCreated = new AtomicBoolean();

    private static final Lock locker = new ReentrantLock();
    private static SessionFactory sessionFactory;
    public static Util getInstance() {
        if (!isCreated.get()) {
            locker.lock();
            if (instance == null) {
                instance = new Util();
                isCreated.set(true);
            }
            locker.unlock();
        }
        return instance;
    }

    public static Connection getConnection() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Class.forName(DRIVER);
            System.out.println("Соединение с БД прошел успешно!");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Соединение с БД не прошел!");
        }
        return connection;
    }


    public SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration();
        Properties properties = new Properties();

        properties.put(Environment.URL, URL);
        properties.put(Environment.USER, USERNAME);
        properties.put(Environment.PASS, PASSWORD);
        properties.put(Environment.DRIVER, DRIVER);
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");

        LogManager.getLogManager().getLogger("").setLevel(Level.SEVERE);

        configuration.setProperties(properties);
        configuration.addAnnotatedClass(User.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        return sessionFactory;
    }

}
