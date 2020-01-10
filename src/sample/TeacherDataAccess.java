package sample;

import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

public class TeacherDataAccess {
    private Connection conn;
    private static final String teacherTable = "teacher";

    public TeacherDataAccess() throws SQLException, ClassNotFoundException {
        // Class.forName("org.hsqldb.jdbc.JDBCDriver" );
        //STEP 2: Check if JDBC driver is available
        Class.forName( "com.mysql.cj.jdbc.Driver");
        //STEP 3: Open a connection
        System.out.println("Connecting to database teachers...");
        conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/school",
                "root",
                "");
        // we will use this connection to write to a file
        conn.setAutoCommit(true);
        conn.setReadOnly(false);
    }

    public void closeDb() throws SQLException {
        conn.close();
    }
     /**
     * Get all db records
     * @return
     * @throws SQLException
     */
    public List<Teachers> getAllRows()   throws SQLException {
        String sql = "SELECT * FROM " + teacherTable + " ORDER BY teacher_id" ;
        PreparedStatement pstmnt = conn.prepareStatement(sql);
        ResultSet rs = pstmnt.executeQuery();
        List<Teachers> list = new ArrayList<>();
        while  (rs.next()) {
            String firstname = rs.getString("name" );
            String lastname = rs.getString("surname" );
            String email = rs.getString("email" );
            int i = rs.getInt("teacher_id" );
            list.add(new Teachers(i, firstname, lastname, email));
        }
        pstmnt.close(); // also closes related result set
        return list;
    }

    public boolean nameExists(Teachers teacher) throws SQLException {
        String sql = "SELECT COUNT(teacher_id) FROM " + teacherTable + " WHERE name = ? AND teacher_id <> ?" ;
        PreparedStatement pstmnt = conn.prepareStatement(sql);
        pstmnt.setString( 1, teacher.getFirstname());
        pstmnt.setString( 2, teacher.getLastname());
        ResultSet rs = pstmnt.executeQuery();
        rs.next();
        int count = rs.getInt(1 );
        pstmnt.close();

        if (count > 0) {
            return true ;
        }
        return false ;
    }

    public   int  insertRow(Teachers teacher) throws  SQLException {
        String dml = "INSERT INTO "  + teacherTable + " VALUES (?, ?, ?, ?)" ;
        PreparedStatement pstmnt = conn.prepareStatement(dml,
                PreparedStatement.RETURN_GENERATED_KEYS);
        pstmnt.setString(1, teacher.getFirstname());
        pstmnt.setString(2, teacher.getLastname());
        pstmnt.setString(3, teacher.getEmail());
        pstmnt.setInt(4, teacher.getId());
        pstmnt.executeUpdate(); // returns insert count
        // get identity column value
        ResultSet rs = pstmnt.getGeneratedKeys();
        rs.next();
        int id = rs.getInt( 1 );
        pstmnt.close();
        return id;
    }

    public void updateRow(Teachers teacher) throws SQLException {
        String dml = "UPDATE " + teacherTable + " SET name = ?, surname = ?, email = ? WHERE teacher_id = ?" ;
        PreparedStatement pstmnt = conn.prepareStatement(dml);
        pstmnt.setString(1,teacher.getFirstname());
        pstmnt.setString(2, teacher.getLastname());
        pstmnt.setString(3, teacher.getEmail());
        pstmnt.setInt(4, teacher.getId());
        pstmnt.executeUpdate(); //returns update count
        pstmnt.close();
    }

    public void deleteRow(Teachers teacher) throws SQLException {
        String dml = "DELETE FROM " + teacherTable + " WHERE teacher_id = ?" ;
        PreparedStatement pstmnt = conn.prepareStatement(dml);
        pstmnt.setInt(1 , teacher.getId());
        pstmnt.executeUpdate(); // returns delete count (0 for none)
        pstmnt.close();
    }
}
