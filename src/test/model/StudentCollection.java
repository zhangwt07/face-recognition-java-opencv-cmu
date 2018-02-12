
package test.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author chenjiaxin
 */
public class StudentCollection {

    private ArrayList<StudentInfo> students = new ArrayList<>();
    private Statement stmt;

    /**
     *
     * @return students arraylist
     */
    public ArrayList<StudentInfo> getStudents() {
        return this.students;
    }

    /**
     *
     * @return students arraylist
     */
    public ArrayList<StudentInfo> getAllStudents(int predictedLabel) {
        initializeDB();
        return showStudents(predictedLabel);
    }
    
    // init the database connection
    private void initializeDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //System.out.println("Driver loaded");

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/Students", "root", "login");
            //System.out.println("Database Connected");

            stmt = connection.createStatement();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // get all the students in database
    private ArrayList<StudentInfo> showStudents(int predictedLabel) {
        try {
            String query = "select * from StudentInfo where label = " + predictedLabel;

            ResultSet rset = stmt.executeQuery(query);
            
            // put students into arraylist
            if (rset.next()) {
                String sname = rset.getString(1);
                String sid = rset.getString(2);
                int sage = rset.getInt(3);
                String sprogram = rset.getString(4);
                String sphoto = rset.getString(5);
                Date slastVisited = rset.getDate(6);
                int scount = rset.getInt(7);
                String sreason = rset.getString(8);
                int slabel = rset.getInt(9);
                String sgender = rset.getString(10);
                StudentInfo s = new StudentInfo(sname, sid, sage, sprogram, sphoto, slastVisited, scount, sreason, slabel, sgender);
                students.add(s);
                //System.out.println(s.getLabel());
            } else {
                System.out.println("Not Found");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return students;
    }

}
