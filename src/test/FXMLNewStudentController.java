/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static test.FXMLRecoController.currentName;
import test.controller.StoreStudent;

/**
 * FXML Controller class
 *
 * @author Weitong
 */
public class FXMLNewStudentController implements Initializable {

    @FXML
    private Button btnCancel;
    @FXML
    private Button btnConfirm;
    @FXML
    private ChoiceBox<String> chboxReason;
    @FXML
    private Label lblWarning;
    @FXML
    private Label lblName;
    @FXML
    private TextField txtAndrew;
    @FXML
    private Label lblAndrew;

    @FXML
    private void handleButtonAction(ActionEvent event) {
    }

    String andrewID, visitReason;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        String testID = FXMLRecoController.currentID;
        chboxReason.getItems().removeAll(chboxReason.getItems());
        chboxReason.getItems().addAll("Ask for milk", "Meet staff", "Get stapler", "Enquiries","Fee payment","Collect mail","Other");
        lblName.setText(FXMLRecoController.currentName);
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                lblGender.setText(testID);
//            }
//        });

        btnCancel.setOnAction((event) -> {
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        });

        btnConfirm.setOnAction((event) -> {
            andrewID = txtAndrew.getText();
            visitReason = chboxReason.getValue();
            if (!visitReason.equals("")) {
                if (andrewID.isEmpty()) {
                    //store to database, andrewID = currentID
                    StoreStudent t = new StoreStudent();
                    t.initializeDB1();
                    t.storeDB1(testID,visitReason);
                    
                    
                    
                    System.out.println(testID);
                    //close the stage
                    Stage stage = (Stage) btnConfirm.getScene().getWindow();
                    stage.close();
                } else {
                    //check in the database if this andrew id exist
                    //if exist, store to database
                    if (getAllStudents().contains(andrewID)) {
                        //store to database records, andrew ID = andrewID
                        System.out.println("record store to database");
                        StoreStudent t = new StoreStudent();
                        t.initializeDB1();
                        t.storeDB1(andrewID,visitReason);
                   
                    //close the stage
                    Stage stage = (Stage) btnConfirm.getScene().getWindow();
                    stage.close();
                    } else {
                        //if not in database, send a warning, do not store
                        lblWarning.setText("This person is not in the database! Check or Add new");
                        System.out.println("andrew id not in database");
                    }

                }

            } else {
                lblWarning.setText("Enter all fields!");
            }

        });

    }

    public ArrayList<String> getAllStudents() {
        Statement stmt = null;
        ArrayList<String> names = new ArrayList<>();

        //initializeDB();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //System.out.println("Driver loaded");

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/Students", "root", "login");
            //System.out.println("Database Connected");

            stmt = connection.createStatement();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            String q = "select andrewid from StudentInfo";
            ResultSet res = stmt.executeQuery(q);
            //System.out.println(res.toString());
            while (res.next()) {
                names.add(res.getString("andrewid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("name error");
        }
        return names;

    }

}
