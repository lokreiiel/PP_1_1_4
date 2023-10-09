package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.List;

import static jm.task.core.jdbc.util.Util.getSessionFactory;


public class UserDaoHibernateImpl implements UserDao {
    private static final SessionFactory sessionFactory = getSessionFactory();
    private Transaction transaction;

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("create table if not exists users" +
                    "(" +
                    "    id  bigint auto_increment" +
                    "        primary key not null," +
                    "    name     varchar(45) not null," +
                    "    last_name varchar(45) not null," +
                    "    age      tinyint     " +
                    ");").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("no");
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("drop table if exists users").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("no");
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(new User(name, lastName, age));
            session.getTransaction().commit();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        } catch (Exception e) {
            System.err.println("no3");
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = session.get(User.class, id);
            session.delete(user);
            session.getTransaction().commit();

        } catch (Exception e) {
            System.err.println("no4");
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<User> users = session.createSQLQuery("select * from users").addEntity(User.class).list();
            session.getTransaction().commit();

            for (User us : users) {
                System.out.println(us);
            }
            return users;
        } catch (Exception e) {
            System.err.println("no5");
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return Collections.emptyList();
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {

            session.beginTransaction();
            session.createQuery("delete User").executeUpdate();
            session.getTransaction().commit();

        } catch (Exception e) {
            System.err.println("no6");
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }
}
