/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 *
 * @author chenjiaxin
 */
public class StoreStudent {

    private Statement stmt;
    private PreparedStatement stmt1;
    private Statement stmt2;
    private Statement stmt3;

    public void store(ArrayList<String> s, int label) {
        initializeDB();
        storeDB(s, label);
    }

    public int getCount() {
        initializeDB();
        int num = 0;
        try {
            String q = "select count(*) from StudentInfo";
            ResultSet res = stmt.executeQuery(q);
            //System.out.println(res.toString());
            res.next();
            num = res.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("count error");
        }
        return num;
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

    // store data into DB studeng info
    private void storeDB(ArrayList<String> s, int label) {

        try {
            //insert a new data
            Date date = new Date();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String str = format.format(date);
            Date newd = format.parse(str);
            java.sql.Date sd = new java.sql.Date(newd.getTime());
            String updateStmt = "INSERT INTO StudentInfo (name, andrewId, age, program, photo, lastVisited, count, reason, label,gender) VALUES ("
                    + "\"" + s.get(0) + "\"," + "\"" + s.get(1) + "\"," + s.get(2) + ","
                    + "\"" + s.get(3) + "\"," + "\"" + s.get(6) + "\"," + "\"" + sd + "\","
                    + 1 + "," + "\"" + s.get(4) + "\"," + label + ", \"" + s.get(5) + "\"" + ")";

            System.out.println(updateStmt);
            stmt.executeUpdate(updateStmt);
            //System.out.println("store success");

        } catch (Exception e) {
            System.out.print("MYSQL ERROR:" + e.getMessage());
        }

    }

    // init the database connection
    public void initializeDB1() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //System.out.println("Driver loaded");

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/Students", "root", "login");
            //System.out.println("Database Connected");

            stmt1 = connection.prepareStatement("select count from Students.Records where andrewid =?");
            stmt3 = connection.createStatement();
            
            System.out.println("prepare state");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void initializeDB2() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //System.out.println("Driver loaded");

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/Students", "root", "login");
            //System.out.println("Database Connected");

            stmt2 = connection.createStatement();
            System.out.println("prepare state");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // store data into DB records
    public void storeDB1(String andrewID, String reason) {

        try {
            //insert a new data
            Date date = new Date();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String str = format.format(date);
            System.out.println(str);
            //Date newd = format.parse(str);
            //java.sql.Date sd = new java.sql.Date(newd.getTime());

            //get current max count
            ArrayList<Integer> counts = new ArrayList<>();

            System.out.println("enter storeDB1");
            //String find = "SELECT count FROM Students.Records WHERE andrewid = \'" + andrewID+"'\";
            stmt1.setString(1, andrewID);
            ResultSet rs = stmt1.executeQuery();
            //ResultSet rs = stmt1.executeQuery(find);

            System.out.println("is count null?" + rs == null);
            while (rs.next()) {
                counts.add(rs.getInt("count"));
            }
            int count = Collections.max(counts);

            int newCount = count + 1;
            System.out.println(newCount);

            //get the max number
            int num = getNumber();
            System.out.println("num in store method" + num);

            //add a column number as PK
            String updateStmt = "INSERT INTO Students.Records (number,andrewId, count, reason, date) VALUES ("
                    + "\"" + num + "\"," + "\"" + andrewID + "\"," + "\"" + newCount + "\"," + "\"" + reason + "\"," + "\"" + str + "\")";

            //String updateStmt = INSERT INTO `Students`.`Records` (`andrewid`, `count`, `reason`) VALUES ('shuningc', '4', 'milk'); 
            
            
            
            //update information in studentInfo table
            
            String updateStmt1 = "UPDATE Students.StudentInfo SET lastVisited = '"+str+"', count = '"+ newCount+"', reason = '"+reason+"' where andrewID = '"+ andrewID+"'";
            //UPDATE `Students`.`StudentInfo` SET `lastVisited`='2017-11-23', `count`='10' WHERE `andrewId`='yitians';

            
            
            stmt3.executeUpdate(updateStmt1);
            
            System.out.println("store in studentInfo");
            
            //System.out.println(updateStmt);
            stmt1.executeUpdate(updateStmt);
            //System.out.println("store success");

        } catch (Exception e) {
            //if the person is not in the database records
            System.out.print("MYSQL ERROR:" + e.getMessage());
        }

    }

    public void storeDB1(String andrewID, String reason, int count) {

        try {
            //insert a new data
            Date date = new Date();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String str = format.format(date);
            //store  

            // get the max number
            int num = getNumber();
            System.out.println("num in store method" + num);

            String updateStmt = "INSERT INTO Students.Records (number, andrewId, count, reason, date) VALUES ("
                    + "\"" + num + "\"," + "\"" + andrewID + "\"," + "\"" + count + "\"," + "\"" + reason + "\"," + "\"" + str + "\")";

            //String updateStmt = INSERT INTO `Students`.`Records` (`andrewid`, `count`, `reason`) VALUES ('shuningc', '4', 'milk'); 
            System.out.println(updateStmt);
            stmt1.executeUpdate(updateStmt);
            //System.out.println("store success");

        } catch (Exception e) {
            //if the person is not in the database records
            System.out.print("MYSQL ERROR:" + e.getMessage());
        }

    }

    public int getNumber() throws SQLException {
        initializeDB2();

        ArrayList<Integer> number = new ArrayList<>();
        System.out.println("Is it here????");
        String find_num = "SELECT number FROM Students.Records WHERE count>0";
        ResultSet rs1 = stmt2.executeQuery(find_num);

        System.out.println("is number null?" + rs1 == null);
        while (rs1.next()) {
            number.add(rs1.getInt("number"));
        }

        System.out.println("successfully get the max number+1!@");

        return Collections.max(number) + 1;

    }

}
