/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.view;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author chenjiaxin
 */
public class ReportPane {

/**
 * 
 * @param m1
 * @param m2
 * @return 

 
 */
/**
 * Display table1
 * @param m1
 * @return the address of the output excel
 */   
    public String dis1(ArrayList<String> m1) {
        ArrayList<String> milk = new ArrayList<>();
        ArrayList<String> staff = new ArrayList<>();
        ArrayList<String> stapler = new ArrayList<>();
        ArrayList<String> enquiries = new ArrayList<>();
        ArrayList<String> fee = new ArrayList<>();
        ArrayList<String> mail = new ArrayList<>();
        ArrayList<String> others = new ArrayList<>();

        int num = m1.size() / 3;  
        System.out.println(num);
                                 //num means the number of student
        String[][] table1 = new String[num][3];

        for (int i = 0, j = 0; i < m1.size() && j < num; i = i + 3, j = j + 1) {
            table1[j][0] = m1.get(i);  //reason
            table1[j][1] = m1.get(i + 1);   //andrewid
            table1[j][2] = m1.get(i + 2);     //date
        }

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Report 1"); //name of the sheet
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("Reason"); 
        //name of row_0
        cell = row.createCell(1);
        cell.setCellValue("Count");
        
        cell = row.createCell(2);
        cell.setCellValue("Andrew ID");
        
        cell = row.createCell(3);
        cell.setCellValue("Visit Date");
        
        System.out.println("create sheet successful "+table1.length+"  "+table1[0].length);
        
        //   String result = "The students visiting in given dates:\n\n";
        
        
        for (int i = 0; i < table1.length; i++) {
            System.out.println("start create table");
            String reason = table1[i][0];
            String andrew = table1[i][1];
            String date = table1[i][2];
//           // System.out.println(reason);
//            System.out.println(andrew);
//            System.out.println(date);

            String[] a = new String[3];
            a[0] = reason;
            a[1] = andrew;
            a[2] = date;
            System.out.println( "reason:"+a[0] +" andrew id: "+ a[1] + "date " + a[2]);
            
            if (a[0].equals("Ask for milk")) {
                milk.add("Ask for milk");
                milk.add(a[1]);
                milk.add(a[2]);
            } else if (a[0].equals("Meet staff")) {
                staff.add("Meet staff");
                staff.add(a[1]);
                staff.add(a[2]);
            } 
           else if (a[0].equals("Get stapler")) {
                stapler.add("Get stapler");
                stapler.add(a[1]);
                stapler.add(a[2]);
            } 
           else if (a[0].equals("Enquiries")) {
                enquiries.add("Enquiries");
                enquiries.add(a[1]);
                enquiries.add(a[2]);
            } 
           else if (a[0].equals("Fee payment")) {
                fee.add("Fee payment");
                fee.add(a[1]);
                fee.add(a[2]);
            }
           else if (a[0].equals("Collect mail")) {
                mail.add("Collect mail");
                mail.add(a[1]);
                mail.add(a[2]);
            } 
           else if (a[0].equals("Other")) {
                others.add("Other");
                others.add(a[1]);
                others.add(a[2]);
            }
             
                     
        }

        int row_count = 0;
        //**************
        for (int rn = 1, c = 1; rn <= milk.size() / 3 && c <= (milk.size() - 2); rn++, c = c + 3) {
            HSSFRow row1 = sheet.createRow(rn + row_count);
             row1.createCell(0).setCellValue("Ask for milk");
             row1.createCell(1).setCellValue(rn);
             row1.createCell(2).setCellValue(milk.get(c));
             row1.createCell(3).setCellValue(milk.get(c + 1));
           
        }

        row_count = row_count + milk.size() / 3;

        for (int rn = 1, c = 1; rn <= staff.size() / 3 && c <=staff.size() - 2; rn++, c = c + 3) {
            HSSFRow row1 = sheet.createRow(rn + row_count);
            row1.createCell(0).setCellValue("Meet staff");
            row1.createCell(1).setCellValue(rn);
            row1.createCell(2).setCellValue(staff.get(c));
            row1.createCell(3).setCellValue(staff.get(c + 1));
        }
      
        row_count = row_count + staff.size() / 3;

        for (int rn = 1, c = 1; rn <= stapler.size() / 3 && c <= stapler.size() - 2; rn++, c = c + 3) {
            HSSFRow row1 = sheet.createRow(rn + row_count);
            row1.createCell(0).setCellValue("Get stapler");
            row1.createCell(1).setCellValue(rn);
            row1.createCell(2).setCellValue(stapler.get(c));
            row1.createCell(3).setCellValue(stapler.get(c + 1));
        }
        row_count = row_count + stapler.size() / 3;

        for (int rn = 1, c = 1; rn <= enquiries.size() / 3 && c <= enquiries.size() - 2; rn++, c = c + 3) {
            HSSFRow row1 = sheet.createRow(rn + row_count);
            row1.createCell(0).setCellValue("Enquiries");
            row1.createCell(1).setCellValue(rn);
            row1.createCell(2).setCellValue(enquiries.get(c));
            row1.createCell(3).setCellValue(enquiries.get(c + 1));
            System.out.println("assignments!!   "+rn);
        }
        row_count = row_count + enquiries.size() / 3;

        for (int rn = 1, c = 1; rn <= fee.size() / 3 && c <= fee.size() - 2; rn++, c = c + 3) {
            HSSFRow row1 = sheet.createRow(rn + row_count);
            row1.createCell(0).setCellValue("Fee payment");
            row1.createCell(1).setCellValue(rn);
            row1.createCell(2).setCellValue(fee.get(c));
            row1.createCell(3).setCellValue(fee.get(c + 1));
        }
        row_count = row_count + fee.size() / 3;

        for (int rn = 1, c = 1; rn <= mail.size() / 3 && c <= mail.size() - 2; rn++, c = c + 3) {
            HSSFRow row1 = sheet.createRow(rn + row_count);
            row1.createCell(0).setCellValue("Collect mail");
            row1.createCell(1).setCellValue(rn);
            row1.createCell(2).setCellValue(mail.get(c));
            row1.createCell(3).setCellValue(mail.get(c + 1));
        }
        row_count = row_count + mail.size() / 3;
        
        for (int rn = 1, c = 1; rn <= others.size() / 3 && c <= others.size() - 2; rn++, c = c + 3) {
            HSSFRow row1 = sheet.createRow(rn + row_count);
            row1.createCell(0).setCellValue("Other");
            row1.createCell(1).setCellValue(rn);
            row1.createCell(2).setCellValue(others.get(c));
            row1.createCell(3).setCellValue(others.get(c + 1));
        }
        	

         String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
         String filepath="/Users/chenjiaxin/Desktop/test/src/test/report/report_of_records_"+timeStamp+".xls";
        try { 
            FileOutputStream fos = new FileOutputStream(filepath);
            workbook.write(fos);
            System.out.println(" write into the repot1 successfully   ");
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
      
        System.out.println("finish write in process");
        return filepath;

    }

    
/**
 * Display table 2
 * @param m2
 * @return the address of the ouput excel 
 */
    public String dis2(ArrayList<String> m2) {
        int[] Total_reason = new int[10];
        int[] Total_sex = new int[10];

        int[][] counter = new int[10][10];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 2; j++) {
                counter[i][j] = 0;
            }
        }

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Report 2"); //name of the sheet
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        
        cell.setCellValue("      ");   //name of row_0
        cell = row.createCell(1);
        cell.setCellValue("Female"); //name of row_1
        cell = row.createCell(2);
        cell.setCellValue("Male"); //name of row_1
        cell = row.createCell(3);
        cell.setCellValue("Total");
        
        HSSFRow row1 = sheet.createRow(1);
        HSSFRow row2 = sheet.createRow(2);
        HSSFRow row3 = sheet.createRow(3);
        HSSFRow row4 = sheet.createRow(4);
        HSSFRow row5 = sheet.createRow(5);
        HSSFRow row6 = sheet.createRow(6);
        HSSFRow row7 = sheet.createRow(7);
        HSSFRow row8 = sheet.createRow(8);
        

        int num = m2.size() / 3;
        int gender_index =num*2;
        System.out.println(num);  
        String[][] table2 = new String[num+5][10];

        for (int i = 0, j = 0; i < m2.size() && j < num; i = i + 3, j = j + 1) {
            table2[j][0] = m2.get(i);    //reasons
            table2[j][1] = m2.get(i + 1);   
            table2[j][2] = m2.get(gender_index+j);  //gender
        }

        for (int k = 0; k < num; k++) {
            String reason = table2[k][0];
            String gender = table2[k][2];
            System.out.println("reason is:" +reason);
            System.out.println("gender is:" +gender);

            if (reason.equals("Ask for milk")) {
                if (gender.equals("Female")) {
                    counter[0][0]++;
                } else {
                    counter[0][1]++;
                }
            } else if (reason.equals("Meet staff")) {
                if (gender.equals("Female")) {
                    counter[1][0]++;
                } else {
                    counter[1][1]++;
                }
            } else if (reason.equals("Get stapler")) {
                if (gender.equals("Female")) {
                    counter[2][0]++;
                } else {
                    counter[2][1]++;
                }
            } else if (reason.equals("Enquiries")) {
                if (gender.equals("Female")) {
                    counter[3][0]++;
                } else {
                    counter[3][1]++;
                };
            } else if (reason.equals("Fee payment")) {
                if (gender.equals("Female")) {
                    counter[4][0]++;
                } else {
                    counter[4][1]++;
                }
                } else if (reason.equals("Collect mail")) {
                if (gender.equals("Female")) {
                    counter[5][0]++;
                } else {
                    counter[5][1]++;
                }
            } else if (reason.equals("Other")) {
                if (gender.equals("Female")) {
                    counter[6][0]++;
                } else {
                    counter[6][1]++;
                }
            }
        }
  
        row1.createCell(0).setCellValue("Ask for milk");
        row2.createCell(0).setCellValue("Meet staff");
        row3.createCell(0).setCellValue("Get stapler");
        row4.createCell(0).setCellValue("Enquiries");
        row5.createCell(0).setCellValue("Fee payment");
        row6.createCell(0).setCellValue("Collect mail");
        row7.createCell(0).setCellValue("Others");
        row8.createCell(0).setCellValue("Total");

        row1.createCell(1).setCellValue(counter[0][0]);
        row1.createCell(2).setCellValue(counter[0][1]);
        row2.createCell(1).setCellValue(counter[1][0]);
        row2.createCell(2).setCellValue(counter[1][1]);
        row3.createCell(1).setCellValue(counter[2][0]);
        row3.createCell(2).setCellValue(counter[2][1]);
        row4.createCell(1).setCellValue(counter[3][0]);
        row4.createCell(2).setCellValue(counter[3][1]);
        row5.createCell(1).setCellValue(counter[4][0]);
        row5.createCell(2).setCellValue(counter[4][1]);
        row6.createCell(1).setCellValue(counter[5][0]);
        row6.createCell(2).setCellValue(counter[5][1]);
        row7.createCell(1).setCellValue(counter[6][0]);
        row7.createCell(2).setCellValue(counter[6][1]);

        for (int d = 0; d < 7; d++) {
            Total_reason[d] = counter[d][0] + counter[d][1];
        }

        for (int d = 0; d < 7; d++) {
            Total_sex[0] = Total_sex[0] + counter[d][0];
            Total_sex[1] = Total_sex[1] + counter[d][1];
            Total_sex[2] = Total_sex[2] + Total_reason[d];
        }

        row1.createCell(3).setCellValue(Total_reason[0]);
        row2.createCell(3).setCellValue(Total_reason[1]);
        row3.createCell(3).setCellValue(Total_reason[2]);
        row4.createCell(3).setCellValue(Total_reason[3]);
        row5.createCell(3).setCellValue(Total_reason[4]);
        row6.createCell(3).setCellValue(Total_reason[5]);
        row7.createCell(3).setCellValue(Total_reason[6]);

        row8.createCell(1).setCellValue(Total_sex[0]);
        row8.createCell(2).setCellValue(Total_sex[1]);
        row8.createCell(3).setCellValue(Total_sex[2]);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        String filepath="/Users/chenjiaxin/Desktop/test/src/test/report/report_of_cate_and_gender_"+timeStamp+".xls";
        try {
            FileOutputStream fos = new FileOutputStream(filepath);
            workbook.write(fos);
            System.out.println("write into report2 successfully！ ");
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return filepath;
    }
}
