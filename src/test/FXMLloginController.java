/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Weitong
 */
public class FXMLloginController implements Initializable {
    
    @FXML
    private ImageView background;
    @FXML
    private TextField txtUsername;
    String username;
    String password;
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnExitSystem;
    @FXML
    private TextField txtPassword;
    @FXML
    private Label labelWrongLogin;
    
 
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        background.setImage(new Image(FXMLloginController.class.getResourceAsStream("images/bg3.png")));
        
        btnLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("login!!");
                username = txtUsername.getText();
                password = txtPassword.getText();
                System.out.println(username+"\n"+password);
                if ((username.equals("admin"))&&(password.equals("admin"))) {
                   labelWrongLogin.setText("Logging in...");
                    //wait for 1 sec and close this stage
                    try {
                        
                        System.out.println("success");
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(FXMLloginController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Stage stage = (Stage) btnLogin.getScene().getWindow();
                    stage.close();
                    
                    //start new stage
                    try {
                        Stage stageReco = new Stage();
                        Parent root;
                        root = FXMLLoader.load(FXMLloginController.this.getClass().getResource("FXMLReco.fxml"));
                        Scene sceneReco = new Scene(root);
                        stageReco.setScene(sceneReco);
                        stageReco.setTitle("Student Recognition System");
                        stageReco.show();
                    }catch (IOException ex) {
                        Logger.getLogger(FXMLloginController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   
                } else {
                    labelWrongLogin.setText("Wrong username/password! Input again.");     
                }
            }
        });
        btnExitSystem.setOnAction((event)->{
           System.out.println("System exit at login page"); 
           System.exit(0);
        });
    }    

    @FXML
    private void handleButtonAction(InputMethodEvent event) {
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
    }
    
}
