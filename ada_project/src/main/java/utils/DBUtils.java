package utils;
import bean.User;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBUtils {

    static DataSource dataSource;

    // connecting to the database via static initialization block
    static {
        try {
            Context initContext = new InitialContext();
            Context webContext = (Context) initContext.lookup("java:/comp/env");
            dataSource = (DataSource) webContext.lookup("jdbc/demo_datasource");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkIfUserExists(String email) throws SQLException {
        String sql = "SELECT count(1) AS user_exists FROM users WHERE email=?";
        ResultSet resultSet = null;
        try (Connection connection = DBUtils.dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            resultSet = statement.executeQuery();
            if (resultSet != null && resultSet.next() && resultSet.getInt("user_exists") == 0) {
                return false;
            }
        } finally {
            if (resultSet != null) resultSet.close();
        }

        return true;
    }

    public static boolean authenticateWithCredentials(String email, String password) throws SQLException {
        String sql = "SELECT COUNT(1) AS user_exists FROM users WHERE email=? AND password=? LIMIT 1";
        ResultSet resultSet = null;
        String hashedPassword = Security.hashPassword(password);
        try (Connection connection = DBUtils.dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, hashedPassword);
            resultSet = statement.executeQuery();
            if (resultSet != null && resultSet.next() && resultSet.getInt("user_exists") > 0) {
                return true;
            }
        } finally {
            if (resultSet != null) resultSet.close();
        }
        return false;
    }

    public static User getUserDataWithEmail(String email) throws SQLException {
        User user = null;
        String sql = "SELECT first_name, last_name, city, country, age FROM users WHERE email=?";
        ResultSet resultSet = null;
        try (Connection connection = DBUtils.dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            resultSet = statement.executeQuery();
            if (resultSet != null) {
                user = new User();
                while (resultSet.next()) {
                    user.setFirstName(resultSet.getString("first_name"));
                    user.setLastName(resultSet.getString("last_name"));
                    user.setAge(resultSet.getInt("age"));
                    user.setCity(resultSet.getString("city"));
                    user.setCountry(resultSet.getString("country"));
                }
            }
        } finally {
            if (resultSet != null) resultSet.close();
        }
        return user;
    }

    public static List<String> getAvailableCourses(String email) throws SQLException {
        String sql = "SELECT array_agg(title) titles FROM courses WHERE title " +
                "NOT IN (SELECT course FROM course_registrations cr JOIN users u ON cr.email=u.email WHERE u.email=?);";
        ResultSet resultSet = null;
        List<String> courses = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            System.out.println(statement);
            resultSet = statement.executeQuery();
            if (resultSet != null) {
                while (resultSet.next()) {
                    if (resultSet.getArray("titles") != null) {
                        courses = Arrays.asList((String []) resultSet.getArray("titles").getArray());
                    }
                }
            }
        } finally {
            if (resultSet != null) resultSet.close();
        }
        return courses;
    }

    public static List<String> getRegisteredCoursesWithEmail(String email) throws SQLException {
        String sql = "SELECT array_agg(course) AS courses " +
                "FROM course_registrations JOIN users ON users.email=course_registrations.email WHERE users.email=?";
        ResultSet resultSet = null;
        List<String> courses = null;
        try (Connection connection = DBUtils.dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            resultSet = statement.executeQuery();
            if (resultSet != null) {
                while (resultSet.next()) {
                    if (resultSet.getArray("courses") != null) {
                        courses = Arrays.asList((String[]) resultSet.getArray("courses").getArray());
                    }
                }
            }
        } finally {
            if (resultSet != null) resultSet.close();
        }
        return courses;
    }

    public static void registerWithCredentials(String email, String password) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
        String sql = "INSERT INTO users(email, password) VALUES(?, ?)";
        String hashedPassword = Security.hashPassword(password);
        try (Connection connection = DBUtils.dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, email);
            statement.setString(2, hashedPassword);
            statement.executeUpdate();
            System.out.println("registered user");
        }
    }

    public static void updateFirstNameWithEmail(String firstName, String email) throws SQLException {
        String sql = "UPDATE users SET first_name=? WHERE email=?";
        try (Connection connection = DBUtils.dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, firstName);
            statement.setString(2, email);
            statement.executeUpdate();
            System.out.println("updated first name");
        }
    }

    public static void updateLastNameWithEmail(String lastName, String email) throws SQLException {
        String sql = "UPDATE users SET last_name=? WHERE email=?";
        try (Connection connection = DBUtils.dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, lastName);
            statement.setString(2, email);
            statement.executeUpdate();
        }
    }

    public static void updateAgeWithEmail(String age, String email) throws SQLException {
        String sql = "UPDATE users SET age=NULLIF(?, '')::int WHERE email=?";
        try (Connection connection = DBUtils.dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, age);
            statement.setString(2, email);
            statement.executeUpdate();
        }
    }

    public static void updateCityWithEmail(String city, String email) throws SQLException {
        String sql = "UPDATE users SET city=? WHERE email=?";
        try (Connection connection = DBUtils.dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, city);
            statement.setString(2, email);
            statement.executeUpdate();
        }
    }

    public static void updateCountryWithEmail(String country, String email) throws SQLException {
        String sql = "UPDATE users SET country=? WHERE email=?";
        try (Connection connection = DBUtils.dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, country);
            statement.setString(2, email);
            statement.executeUpdate();
        }
    }

    public static void deleteCourse(String email, String course) throws SQLException {
        String sql = "DELETE FROM course_registrations USING users WHERE users.email=course_registrations.email AND users.email=? and course=?;";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, course);
            statement.executeUpdate();
        }
    }

    public static void registerForCourse(String email, String course) throws SQLException {
        String sql = "INSERT INTO course_registrations VALUES(?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, email);
            statement.setString(2, course);
            statement.executeUpdate();
        }
    }


}
