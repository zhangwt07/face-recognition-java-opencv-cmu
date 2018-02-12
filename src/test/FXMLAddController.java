/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import test.controller.StoreStudent;

/**
 * FXML Controller class
 *
 * @author Weitong
 */
public class FXMLAddController implements Initializable {

    @FXML
    private Button btnCancel;
    @FXML
    private Button btnConfirm;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtAndrew;
    @FXML
    private ChoiceBox<String> chboxGender;
    @FXML
    private ChoiceBox<String> chboxReason;
    @FXML
    private Label lblWarning;
    @FXML
    private ChoiceBox<String> chboxProgram;
    @FXML
    private TextField txtAge;
    //static String newName;
    
    private Student s;
    
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        chboxGender.getItems().removeAll(chboxGender.getItems());
        chboxGender.getItems().addAll("Male", "Female");
        
        chboxProgram.getItems().removeAll(chboxProgram.getItems());
        chboxProgram.getItems().addAll("Global MISM","MSIT","MSPPM");
        

        chboxReason.getItems().removeAll(chboxReason.getItems());
        chboxReason.getItems().addAll("Ask for milk", "Meet staff", "Get stapler", "Enquiries","Fee payment","Collect mail","Other");

        //cancel button, to close the window
        btnCancel.setOnAction((event) -> {
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        });

        //confirm button, move to take photo window and close the window
        btnConfirm.setOnAction((event) -> {
            //if all fields are entered
            String name = txtName.getText();
            String andrewID = txtAndrew.getText();
            String gender = chboxGender.getValue();
            String program = chboxProgram.getValue();
            String reason = chboxReason.getValue();
            String age = txtAge.getText();
            if ((name.isEmpty() || andrewID.isEmpty() || gender.isEmpty() 
                    || program.isEmpty() || reason.isEmpty())) {
                lblWarning.setText("Please enter all fields!");
            } else {
                lblWarning.setText("");
                s = new Student();
                s.setInfo(name, andrewID, age, program, reason, gender);
                //do sth to the data here
                StoreStudent t = new StoreStudent();
                t.initializeDB1();
                t.storeDB1(andrewID,reason,1);   //???
                
                
                
                //move to photo taking page
                try {
                        Stage stagePhoto = new Stage();
                        Parent root;
                        root = FXMLLoader.load(FXMLAddController.this.getClass().getResource("FXMLTakePhoto.fxml"));
                        Scene scenePhoto = new Scene(root);
                        stagePhoto.setScene(scenePhoto);
                        stagePhoto.setTitle("New Entry - Profile Photo");
                        stagePhoto.show();
                    }catch (IOException ex) {
                        Logger.getLogger(FXMLAddController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                //close the window
                Stage stage = (Stage) btnConfirm.getScene().getWindow();
                stage.close();
            }

        });

    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
    }
    
    
}
