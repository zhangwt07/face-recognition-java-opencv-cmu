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
import java.util.Date;

/**
 *
 * @author chenjiaxin
 */
public class ReportController {

    private Statement stmt;

    public ArrayList<String> queryDate(String d1, String d2) throws SQLException {
        initializeDB();
        return queryDB1(d1, d2);
    }

    public ArrayList<String> queryCount(String d1, String d2) {
        initializeDB();
        return queryDB2(d1, d2);
    }

    // init the database connection
    private void initializeDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver3 loaded");

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/Students", "root", "login");
            System.out.println("Database3 Connected");

            stmt = connection.createStatement();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//table 1    
    private ArrayList<String> queryDB1(String d1, String d2) throws SQLException {
        ArrayList<String> result = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver3 loaded");

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/Students", "root", "login");
            System.out.println("Database3 Connected");

            stmt = connection.createStatement();
            try {
                String queryStmt = "SELECT  andrewid, reason, date FROM Students.Records WHERE date between " + "\'" + d1
                        + "\'" + "and " + "\'" + d2 + "\'";

                ResultSet rs = stmt.executeQuery(queryStmt);
                while (rs.next()) {
                    result.add(rs.getString("reason"));
                    result.add(rs.getString("andrewid"));
                    result.add(rs.getString("date"));  //add r1,r2,r3 to the ArrayList<String>
                    System.out.println("QWQ");
                }

            } catch (Exception e) {
                System.out.print("?????MYSQL ERROR:" + e.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

//table2    
    private ArrayList<String> queryDB2(String d1, String d2) {
        ArrayList<String> result = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver3 loaded");

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/Students", "root", "login");
            System.out.println("arrive here???????");

            stmt = connection.createStatement();
            try {

                String queryStmt = "SELECT  andrewid, reason FROM Students.Records WHERE date between " + "\'" + d1
                        + "\'" + "and " + "\'" + d2 + "\'";

                ResultSet rs1 = stmt.executeQuery(queryStmt);
                String id="";
                while (rs1.next()) {
                    id = rs1.getString("andrewid");
                    String reason = rs1.getString("reason");
                    result.add(id);
                    result.add(reason);
                    
                    System.out.println("^^^^^");
                }
                 
                for(int i=0;i<result.size();i=i+2)  {
                     String re=result.get(i);
                     String queryStmt2 = "SELECT gender FROM Students.StudentInfo WHERE andrewId = '" + re+"'";
                       ResultSet rs2 = stmt.executeQuery(queryStmt2);
                       while(rs2.next()){
                       String gender = rs2.getString("gender");
                       result.add(gender);
                 }
                      
                       System.out.println("!!!!!!!!!!!!!!!!!!!!");
                       }
                    
                    System.out.println("AWA");
                   
                
                
                
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } catch (Exception e) {
            System.out.print("$$MYSQL ERROR:" + e.getMessage());
        }
       
        for (String a : result) {
            System.out.println("@@@@@@@@@@@");
            System.out.println("["+a+"]");
        }

        return result;
    }

}
