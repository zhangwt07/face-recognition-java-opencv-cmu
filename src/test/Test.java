/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.mysql.jdbc.NonRegisteringDriver;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.opencv.core.Core;

/**
 *
 * @author Weitong
 */
public class Test extends Application {
    
    
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLlogin.fxml"));
        Scene sceneLogin = new Scene(root);
       
        stage.setTitle("System Lonin");
        stage.setScene(sceneLogin);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
                System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        launch(args);
    }
    

    
}

class Student{
    
    
    static ArrayList<String> studentInfo;
    public static void setInfo(String name,String andrew,String age,
            String program, String reason, String gender){
        studentInfo= new ArrayList<>();
        studentInfo.add(name);
        studentInfo.add(andrew);
        studentInfo.add(age);
        studentInfo.add(program);
        studentInfo.add(reason);
        studentInfo.add(gender);
        
    }
    
    public static ArrayList<String> getInfo(){
        return studentInfo;
    }
    
    
    
}