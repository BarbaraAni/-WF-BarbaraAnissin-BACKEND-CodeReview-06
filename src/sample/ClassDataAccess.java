package sample;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;
import  java.sql.PreparedStatement;
import java.sql.ResultSet;
import  java.util.List;
import java.util.ArrayList;

public class ClassDataAccess {
    private Connection conn;
    private static final String classTable = "class";

    public ClassDataAccess() throws SQLException, ClassNotFoundException {
        // Class.forName("org.hsqldb.jdbc.JDBCDriver" );
        //STEP 2: Check if JDBC driver is available
        Class.forName( "com.mysql.cj.jdbc.Driver");
        //STEP 3: Open a connection
        System.out.println("Connecting to database classes...");
        conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/school",
                "root",
                "");
        // we will use this connection to write to a file
        conn.setAutoCommit(true);
        conn.setReadOnly( false);
    }

    public void closeDb() throws SQLException {
        conn.close();
    }
    /**
     * Get all db records
     * @return
     * @throws SQLException
     */
    public List<Classes> getAllRows() throws SQLException {
        String sql = "SELECT * FROM " + classTable + " ORDER BY class_name" ;
        PreparedStatement pstmnt = conn.prepareStatement(sql);
        ResultSet rs = pstmnt.executeQuery();
        List<Classes> list = new  ArrayList<>();
        while  (rs.next()) {
            int i = rs.getInt("class_id" );
            String name = rs.getString("class_name" );
            list.add( new Classes(i, name));
        }
        pstmnt.close(); // also closes related result set
        return list;
    }


    public  boolean nameExists(Classes classes) throws SQLException {
        String sql = "SELECT COUNT(class_id) FROM " + classTable + " WHERE class_name = ? AND class_id <> ?" ;
        PreparedStatement pstmnt = conn.prepareStatement(sql);
        pstmnt.setString( 1 , classes.getClassname());
        pstmnt.setInt( 2 , classes.getClassId());
        ResultSet rs = pstmnt.executeQuery();
        rs.next();
        int count = rs.getInt(1 );
        pstmnt.close();
        if (count > 0 ) {
            return true ;
        }
        return false ;
    }

    public   int  insertRow(Classes classes)
            throws  SQLException {

        String dml = "INSERT INTO "  + classTable + " VALUES (DEFAULT, ?, ?)" ;
        PreparedStatement pstmnt = conn.prepareStatement(dml,
                PreparedStatement.RETURN_GENERATED_KEYS);
        pstmnt.setInt( 1 , classes.getClassId());
        pstmnt.setString( 2 , classes.getClassname());
        pstmnt.executeUpdate(); // returns insert count

        // get identity column value
        ResultSet rs = pstmnt.getGeneratedKeys();
        rs.next();
        int id = rs.getInt( 1 );
        pstmnt.close();
        return id;
    }

    public void  updateRow(Classes classes)
            throws SQLException {

        String dml = "UPDATE "  + classTable + " SET class_name = ? WHERE id = ?" ;
        PreparedStatement pstmnt = conn.prepareStatement(dml);
        pstmnt.setString( 1 , classes.getClassname());
        pstmnt.setInt( 2 , classes.getClassId());
        pstmnt.executeUpdate(); // returns update count
        pstmnt.close();
    }

    public void  deleteRow(Classes classes) throws SQLException {
        String dml = "DELETE FROM "  + classTable + " WHERE class_id = ?" ;
        PreparedStatement pstmnt = conn.prepareStatement(dml);
        pstmnt.setInt(1 , classes.getClassId());
        pstmnt.executeUpdate(); // returns delete count (0 for none)
        pstmnt.close();
    }
}

