/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import test.controller.StoreStudent;
import test.utils.Utils;
import test.view.InfoInput;

/**
 * FXML Controller class
 *
 * @author Weitong
 */
public class FXMLTrainingController implements Initializable {

    @FXML
    private ImageView imgCamera;
    @FXML
    private ImageView imgPhoto;
    @FXML
    private Button btnTakePhoto;
    @FXML
    private Button btnFinish;
    int count = 0;
    // the OpenCV object that performs the video capture
    private VideoCapture capture;
    // a flag to change the button behavior
    private boolean cameraActive;
    private ScheduledExecutorService timer;
    @FXML
    private ImageView img1;
    @FXML
    private ImageView img2;
    @FXML
    private ImageView img3;
    @FXML
    private ImageView img4;
    @FXML
    private ImageView img5;
    @FXML
    private ImageView img6;
    @FXML
    private ImageView img7;
    @FXML
    private ImageView img8;
    @FXML
    private ImageView img9;
    @FXML
    private ImageView img10;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        btnFinish.setDisable(true);
        startCamera();

        btnTakePhoto.setOnAction((event) -> {
            //take a photo
            count++;
            
            Image im = getPicture();
            
            String filePath = InfoInput.storeImg(im, Student.studentInfo, InfoInput.getCount(), count);
            Student.studentInfo.add(filePath);
            
            
          
            
            switch(count){
                case 1:img1.setImage(im);break;
                case 2:img2.setImage(im);break;
                case 3:img3.setImage(im);break;
                case 4:img4.setImage(im);break;
                case 5:img5.setImage(im);break;
                case 6:img6.setImage(im);break;
                case 7:img7.setImage(im);break;
                case 8:img8.setImage(im);break;
                case 9:img9.setImage(im);break;
                case 10:img10.setImage(im);break;
            }
            //show this photo in lower pane
            //add count
            

            if (count == 10) {
                btnFinish.setDisable(false);
                btnTakePhoto.setDisable(true);
            }
            btnTakePhoto.setText("Take Photo - " + (10 - count) + " remains");
        });

        btnFinish.setOnAction((event) -> {
            System.out.println("add finish");
            //do sth to add into db
                 
            storeStudent(Student.studentInfo);
            //close stage
            Stage stage = (Stage) btnFinish.getScene().getWindow();
            stage.close();
        });
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
    }

    public void startCamera() {
        capture = new VideoCapture();

        //System.out.println("start");
        // if this camera is off now
        if (!this.cameraActive) {

            // start the video capture
            capture.open(0);

            // is the video stream available?
            if (this.capture.isOpened()) {
                this.cameraActive = true;

                // use a thread to grap frames
                // grab a frame every 33 ms (30 frames/sec)
                Runnable frameGrabber = new Runnable() {

                    @Override
                    public void run() {
                        // effectively grab and process a single frame
                        Mat frame = grabFrame();

                        // update recognition frame
                        Image imageToShow = Utils.mat2Image(frame);

                        Platform.runLater(new Runnable() {

                            @Override
                            public void run() {
                                imgCamera.setImage(imageToShow);
                            }
                        });
                    }

                };

                this.timer = Executors.newSingleThreadScheduledExecutor();
                this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

            }
        }
    }

    private Mat grabFrame() {
        Mat frame = new Mat();

        // check if the capture is open
        if (this.capture.isOpened()) {
            try {
                // read the current frame
                this.capture.read(frame);
                //System.out.println("read frame success!");

                // if the frame is not empty, process it
                if (!frame.empty()) {
                    // face detection
                    //this.detectAndDisplay(frame);
                    //System.out.println("detect success!");
                }

            } catch (Exception e) {
                // log the (full) error
                System.err.println("Exception during the image elaboration: " + e);
            }
        }

        return frame;
    }
    // getPicture() method is to get trainning pictures for strangers.
    //return an image

    public Image getPicture() {
        //VideoCapture capture = new VideoCapture();
        //this.cameraActive = false;
        Image imageToTest = null;
        Mat frame = null;

        // if this camera is off now
        //if (!cameraActive) {
        // start the video capture
        //this.capture.open(0);
        // is the video stream available?
        if (this.capture.isOpened()) {
            cameraActive = true;
            frame = new Mat();

            try {
                // read the current frame
                this.capture.read(frame);
                System.out.println("read frame success!");
            } catch (Exception e) {
                // log the (full) error
                System.err.println("Exception during the image elaboration: " + e);
            }
        }

        // convert and show the frame
        System.out.println("转好了！！！");
        imageToTest = Utils.mat2Image(frame);
        //}

        //stopAcquisition();
        return imageToTest;
    }

    private void stopAcquisition() {
        this.cameraActive = false;
        if (this.timer != null && !this.timer.isShutdown()) {
            try {
                // stop the timer
                this.timer.shutdown();
                this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                // log any exception
                System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
            }
        }

        if (this.capture.isOpened()) {
            // release the camera
            this.capture.release();
        }
    }

    public void storeStudent(ArrayList<String> s){
        StoreStudent ss = new StoreStudent();
        int count = ss.getCount()+1;
        
        
        Statement stmt;
         //connect
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //System.out.println("Driver loaded");

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/Students", "root", "login");
            //System.out.println("Database Connected");

            stmt = connection.createStatement();
            //insert a new data
            Date date = new Date();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String str = format.format(date);
            Date newd = format.parse(str);
            java.sql.Date sd = new java.sql.Date(newd.getTime());
            String updateStmt = "INSERT INTO StudentInfo (name, andrewId, age, program, photo, lastVisited, count, reason, label,gender) VALUES ("
                    + "\"" + s.get(0) + "\"," + "\"" + s.get(1) + "\"," + s.get(2) + ","
                    + "\"" + s.get(3) + "\"," + "\"" + s.get(6) + "\"," + "\"" + sd + "\","
                    + 1 + "," + "\"" + s.get(4) + "\"," + count + ", \"" + s.get(5) + "\"" + ")";

            System.out.println(updateStmt);
            stmt.executeUpdate(updateStmt);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
       
        

    }

   

}
