import java.sql.DriverManager;
import java.sql.*;

/**
 * Created by Anna on 21.01.2018.
 */
public class DataBase {
    Statement stmt;
    int pom = 1;

    public DataBase() {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3306/ankieta";
        try {
            Class.forName(driver).newInstance();
            Connection con = DriverManager.getConnection(url, "root", null);
            stmt = con.createStatement();

            DatabaseMetaData dmd = con.getMetaData();
            ResultSet rs = dmd.getTables(null, null, "odpowiedzi", null);
            if (!rs.next())
                createTable();


        } catch (InstantiationException | IllegalAccessException
                | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public String readDataBase(int id) {
        String pytanie = "";
        String sql = ("SELECT pytanie, odpA,odpB,odpC,odpD FROM bazapytan WHERE ID= " + id);
        int numer;
        ResultSet rs;
        try {
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                for (numer = 1; numer < 6; numer++)
                    pytanie += rs.getString(numer) + "\n";
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pytanie;
    }

    public int numberOfRows() {
        int number = 0;
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT COUNT(*) FROM bazapytan");
            while (rs.next())
                number = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return number;
    }

    public void saveInDB(String answer) {
        int i = numberOfRows();
        try {
            if (pom <= i)
                stmt.executeUpdate("INSERT INTO odpowiedzi VALUES ('" + pom + "',' " + answer + "')");
            pom++;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        String sql = "CREATE TABLE odpowiedzi(Id int primary key, odp varchar(255))";
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int plotValues(int id,int odp) {
        int number = 0;
        ResultSet rs;

        String sql = "SELECT COUNT(*) FROM odpowiedzi WHERE Id=" + id + " AND odp=" + odp;
        try {
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                number = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return number;
    }

    public String plotTitle(int id) {
        String message = "", sql;
        ResultSet rs;
        sql = "SELECT pytanie FROM `bazapytan` WHERE Id=" + id;
        try {
            rs = stmt.executeQuery(sql);
            while (rs.next())
                message = rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }

    public String plotName(int id, String odp) {
        String message = "", sql;
        ResultSet rs;
        sql = "SELECT " + odp + " FROM `bazapytan` WHERE Id=" + id;
        try {
            rs = stmt.executeQuery(sql);
            while (rs.next())
                message = rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }
}
