package sample;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeachingDataAccess {
        private Connection conn;
        private static final String teachingTable = "teaching";

        public TeachingDataAccess() throws SQLException, ClassNotFoundException {
            // Class.forName("org.hsqldb.jdbc.JDBCDriver" );
            //STEP 2: Check if JDBC driver is available
            Class.forName( "com.mysql.cj.jdbc.Driver");
            //STEP 3: Open a connection
            System.out.println("Connecting to database teaching...");
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
        public List<Teaching> getAllRows() throws SQLException {
          /*  String sql = "SELECT * FROM " + teachingTable + " WHERE fk_teacher_id = teacher.teacher_id" ; */
            String sql = "SELECT * FROM `teaching` INNER JOIN `teacher` on fk_teacher_id = teacher_id;";
            String sql1 = "SELECT * FROM `teaching` INNER JOIN `classes` on fk_class_id = class_id";
            PreparedStatement pstmnt = conn.prepareStatement(sql);
            PreparedStatement pstmnt1 = conn.prepareStatement(sql1);
            ResultSet rs = pstmnt.executeQuery();
            List<Teaching> list = new ArrayList<>();
            while  (rs.next()) {
                int i1 = rs.getInt("fk_class_id");
                int i2 = rs.getInt("fk_teacher_id" );
                list.add(new Teaching(i1,i2));
            }
            pstmnt.close(); // also closes related result set
            pstmnt1.close(); // also closes related result set
            return list;
        }
}
