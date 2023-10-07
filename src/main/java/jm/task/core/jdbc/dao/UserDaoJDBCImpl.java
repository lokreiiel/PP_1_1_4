package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.util.Util.getConnection;


public class UserDaoJDBCImpl implements UserDao {
    private final Connection connection = getConnection();

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {

        String CREATE_TABLE = "create table if not exists users" +
                "(" +
                "    id  bigint auto_increment" +
                "        primary key," +
                "    name     varchar(45) not null," +
                "    lastName varchar(45) not null," +
                "    age      tinyint     not null" +
                ");";
        try {
            Statement statement = connection.createStatement();
            statement.execute(CREATE_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось создать таблицу");
        }
    }

    public void dropUsersTable() {
        String DROP_TABLE = "DROP TABLE IF EXISTS users";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(DROP_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось удалить таблицу");
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String INSERT = "INSERT INTO users (name, lastname, age) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось добавить User с именем – " + name + " в таблицу");
        }
    }

    public void removeUserById(long id) {
        String DELETE = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            System.out.println("User c id = " + id + " удален");
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось удалить User с id = " + id);
        }
    }

    public List<User> getAllUsers() {
        List<User> arrayList = new ArrayList<>();
        User user = new User();
        String GET_ALL = "SELECT * FROM users";
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(GET_ALL)) {
            while (resultSet.next()) {

                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                arrayList.add(user);
                System.out.println(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить пользователей");
        }
        return arrayList;
    }

    public void cleanUsersTable() {
        PreparedStatement preparedStatement;
        String DELETE_ALL = "TRUNCATE TABLE users";
        try {
            preparedStatement = connection.prepareStatement(DELETE_ALL);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось очистить таблицу");
        }
    }
}
