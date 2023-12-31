package sqlitetest;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sqlitetutorial.net
 */
public class SqliteTest {

    public static void createNewDatabase(String fileName) {
        String url
                = "jdbc:sqlite:" + basePath.replace("\\",
                        "/") + "/" + fileName;

        try (Connection conn
                = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta
                        = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createNewTable() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SqliteTest.class.getName()).
                    log(Level.SEVERE,
                            null,
                            ex);
        }

        // SQLite connection string
        String url
                = "jdbc:sqlite:" + basePath.replace("\\",
                        "/") + "/funny.db";

        // SQL statement for creating a new table
        String sql
                = "CREATE TABLE IF NOT EXISTS people (\n"
                + "	age integer PRIMARY KEY,\n"
                + "	name text NOT NULL,\n"
                + "	weight real\n"
                + ");";

        try (Connection conn
                = DriverManager.getConnection(url);
                Statement stmt
                = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private Connection connect() {
        // SQLite connection string

        String url
                = "jdbc:sqlite:" + basePath.replace("\\",
                        "/") + "/funny.db";
        Connection conn
                = null;
        try {
            conn
                    = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void insert(int age,
            String name,
            double weight) {
        String sql
                = "INSERT INTO people(age,name,weight) VALUES(?,?,?)";

        try (Connection conn
                = this.connect();
                PreparedStatement pstmt
                = conn.prepareStatement(sql)) {
            pstmt.setInt(1,
                    age);
            pstmt.setString(2,
                    name);
            pstmt.setDouble(3,
                    weight);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void selectAll() {
        String sql
                = "SELECT age, name, weight FROM people";

        try (Connection conn
                = this.connect();
                Statement stmt
                = conn.createStatement();
                ResultSet rs
                = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("age") + "\t"
                        + rs.getString("name") + "\t"
                        + rs.getDouble("weight"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void dropDatabase() {
        File myObj
                = new File(basePath + "\\" + dbName);
        if (myObj.delete()) {
            System.out.println("Database has been dropped.");
        } else {
            System.out.println("Failed to drop the database.");
        }
    }

    public static String basePath;
    public static String dbName = "funny.db";

    public static void main(String[] args) {
        basePath
                = new File("").getAbsolutePath();

        SqliteTest app
                = new SqliteTest();

        System.out.println("Enter your response below.");
        System.out.println("1 - Insert data into the funny database.");
        System.out.println("2 - Drop the funny database.");
        System.out.println(
                "3 - Select all and display the values from the funny db (not yet implemented).");
        Scanner input
                = new Scanner(System.in);
        String response
                = input.nextLine();
        switch (response.toLowerCase().
                charAt(0)) {
            case '1':
                createNewDatabase(dbName);
                createNewTable();

                // insert three new rows
                app.insert(21,
                        "Ethan'a7&F^(W*&\"",
                        135.4);
                app.insert(19,
                        "Anna",
                        122.1);
                app.insert(59,
                        "Carol",
                        122);

                break;
            case '2':
                app.dropDatabase();
                break;
            case '3':
                app.selectAll();
                break;
            default:
                System.out.println("Invalid option. Bye!");
                System.exit(0);
        }
    }
}
