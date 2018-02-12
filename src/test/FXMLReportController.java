/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import test.controller.ReportController;
import test.view.ReportPane;
import javafx.application.Platform;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 * FXML Controller class
 *
 * @author Weitong
 */
public class FXMLReportController implements Initializable {

    @FXML
    private Button btnCancel;
    @FXML
    private DatePicker dateStart;
    @FXML
    private DatePicker dateEnd;
    @FXML
    private Button btnGenerate1;
    @FXML
    private Button btnGenerate2;
    @FXML
    private Label lblWarning;
    @FXML
    private Label lblReport1;
    @FXML
    private Label lblReport2;
    @FXML
    private BarChart<String, Number> chart;
    @FXML
    private Button btnChart;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private CategoryAxis xAxis;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        btnCancel.setOnAction((event) -> {
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        });

        btnChart.setOnAction((event) -> {

            String start = null, end = null;
            int[] char_count = new int[7];
            String[] char_reason = new String[7];

            try {
                start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.sql.Date.valueOf(dateStart.getValue()));
                end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.sql.Date.valueOf(dateEnd.getValue()));

            } catch (java.lang.NullPointerException ex) {
                System.out.println("???");
            }
            if ((start.equals("") | end.equals(""))) {
                System.out.println("no data selected");
                lblWarning.setText("Please select start and end date!");
            } else {
                if (end.compareTo(start) < 0) {
                    lblWarning.setText("End date must after start date!");
                } else {
                    //start query
                    ReportController r = new ReportController();
                    ArrayList<String> a1 = new ArrayList<>();

                    try {
                        a1 = r.queryDate(start, end);
                        System.out.println("query done!!!!!");//****queryDate--for table 1
                    } catch (SQLException ex) {
                        Logger.getLogger(FXMLReportController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    for (int i = 0; i < a1.size(); i++) {
                        System.out.println(a1.get(i));
                    }

                    char_reason[0] = "Ask for milk";
                    char_reason[1] = "Meet staff";
                    char_reason[2] = "Get stapler";
                    char_reason[3] = "Enquiries";
                    char_reason[4] = "Fee payment";
                    char_reason[5] = "Collect mail";
                    char_reason[6] = "Other";

                    for (int i = 0; i < 7; i++) {
                        char_count[i] = NumberofCategory(char_reason[i], a1);
                        System.out.println(char_reason[i] + "char_count is:" + char_count[i]);
                    }

                    xAxis.setLabel("Category");
                    yAxis.setLabel("Count");
                    XYChart.Series s1 = new XYChart.Series();
                    //s1.getData().clear();
                    xAxis.setAnimated(false);
                    yAxis.setAnimated(true);
                    chart.setAnimated(true);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < char_reason.length; i++) {
                                s1.getData().add(new XYChart.Data<String, Number>(char_reason[i], (Number) char_count[i]));
                            }
                            chart.getData().addAll(s1);
                        }
                    });

                }
            }

        });

        btnGenerate1.setOnAction((event) -> {
            //get the time di d2 return the address of the excel and present it
            String start = null, end = null;
            try {
                start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.sql.Date.valueOf(dateStart.getValue()));
                end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.sql.Date.valueOf(dateEnd.getValue()));

            } catch (java.lang.NullPointerException ex) {
                System.out.println("???");
            }

            if ((start.isEmpty() || end.isEmpty())) {
                lblWarning.setText("Please select start and end date!");
            } else {
                if (end.compareTo(start) < 0) {
                    lblWarning.setText("End date must after start date!");
                } else {
                    //start query
                    ReportController r = new ReportController();
                    ArrayList<String> a1 = new ArrayList<>();
                    try {
                        a1 = r.queryDate(start, end); //****queryDate--for table 1
                    } catch (SQLException ex) {
                        Logger.getLogger(FXMLReportController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    ReportPane re = new ReportPane();
                    String display = re.dis1(a1);
                    lblReport1.setText(display);

                    try {
                        Desktop.getDesktop().open(new File(display));
                        //Runtime.getRuntime().exec("cmd/c  start /Users/chenjiaxin/Desktop/test/src/test/report/report.xls");
                    } catch (IOException e) {
                        System.out.print("fail to open excel file");
                    }
                }
            }

            // QueryPane q = new QueryPane();
            //ArrayList<String> q1 = q.show();
        });

        btnGenerate2.setOnAction((event) -> {
            String start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.sql.Date.valueOf(dateStart.getValue()));
            String end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.sql.Date.valueOf(dateEnd.getValue()));
            if ((start.isEmpty() || end.isEmpty())) {
                lblWarning.setText("Please select start and end date!");
            } else {
                if (end.compareTo(start) < 0) {
                    lblWarning.setText("End date must after start date!");
                } else {
                    ReportController r = new ReportController();
                    ArrayList<String> a2 = new ArrayList<>();  //***queryCount -- for table2
                    a2 = r.queryCount(start, end); //****queryDate--for table 1

                    ReportPane re = new ReportPane();
                    String display = re.dis2(a2);
                    lblReport2.setText(display);

                    try {
                        Desktop.getDesktop().open(new File(display));
                        //Runtime.getRuntime().exec("cmd/c  start /Users/chenjiaxin/Desktop/test/src/test/report/report.xls");
                    } catch (IOException e) {
                        System.out.print("fail to open excel file");
                    }
                }
            }

        });

    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
    }

    private int NumberofCategory(String a, ArrayList<String> a1) {
        int count = 0;
        int num = a1.size() / 3;
        //String[] reason = new String[num];
        for (int j = 0; j < a1.size() - 3; j = j + 3) {
            String reason = a1.get(j);
            if (reason.equals(a)) {
                count = count + 1;
            }
        }

        return count;
    }
}
