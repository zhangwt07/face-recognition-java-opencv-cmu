/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.ParallelCamera;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.opencv.core.Mat;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import static test.FXMLRecoController.train;
import test.controller.InfoDispatcher;
import test.utils.Utils;
import test.view.AlertBox;
import test.view.InfoInput;

/**
 * FXML Controller class
 *
 * @author Weitong
 */
public class FXMLTakePhotoController implements Initializable {

    @FXML
    private ImageView imgPhoto;
    @FXML
    private Button btnTake;
    @FXML
    private Button btnConfirm;
    @FXML
    private ImageView imgMale;
    @FXML
    private ImageView imgFemale;
    // the OpenCV object that performs the video capture
    private VideoCapture capture;
    // a flag to change the button behavior
    private boolean cameraActive;
    private ScheduledExecutorService timer;
    @FXML
    private Button btnCamera;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        btnConfirm.setDisable(true);
        btnTake.setDisable(true);
//        imgFemale.setImage(new Image(FXMLTakePhotoController.class.getResourceAsStream("images/female.png")));
        String gender = Student.studentInfo.get(5);
        if (gender.equals("Female")) {
            System.out.println("equals female");
            imgFemale.setImage(new Image(FXMLTakePhotoController.class.getResourceAsStream("images/female.png")));
        } else {
            System.out.println("equals male");
            imgMale.setImage(new Image(FXMLTakePhotoController.class.getResourceAsStream("images/male.png")));
        }

        btnCamera.setOnAction((event) -> {
            startCamera();
            btnTake.setDisable(false);
            btnCamera.setDisable(true);
            btnConfirm.setDisable(true);
            //imgPhoto.setImage(getPicture());
        });

        btnTake.setOnAction((event) -> {
            //take the photo
            imgPhoto.setImage(getPicture());
            
            
            stopAcquisition();
            btnConfirm.setDisable(false);
            btnCamera.setDisable(false);
            btnTake.setDisable(true);

            //change text to retake and un-disable confirm button
//            if (btnTake.getText().equals("Take Photo")) {
//                //startCamera();
//                btnTake.setText("Retake Photo");
//                btnConfirm.setDisable(false);
//                imgPhoto.setImage(getPicture());
//                stopAcquisition();
//            } else {
//                btnTake.setText("Take Photo");
//                btnConfirm.setDisable(true);
//                //imgPhoto.setImage(getPicture());
//                startCamera();
//            }
        });

        btnConfirm.setOnAction((event) -> {
            //save the photo

            //go to training photos
            try {
                Stage stageTrain = new Stage();
                Parent root;
                root = FXMLLoader.load(FXMLTakePhotoController.this.getClass().getResource("FXMLTraining.fxml"));
                Scene sceneTrain = new Scene(root);
                stageTrain.setScene(sceneTrain);
                stageTrain.setTitle("New Entry - Training Photos");
                stageTrain.show();
            } catch (IOException ex) {
                Logger.getLogger(FXMLTakePhotoController.class.getName()).log(Level.SEVERE, null, ex);
            }

            //and close the window
            Stage stage = (Stage) btnConfirm.getScene().getWindow();
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
                        //System.out.println("照好了！！！");
                        // convert and show the frame
                        //Image imageToTest = Utils.mat2Image(frame);
//                      

                        // update recognition frame
                        Image imageToShow = Utils.mat2Image(frame);
                        //System.out.println("zhuan haole !!!!!!");
//                        originalFrame = updateImageView(originalFrame, imageToShow);
//
                        Platform.runLater(new Runnable() {

                            @Override
                            public void run() {
                                imgPhoto.setImage(imageToShow);
//                                pane.getChildren().setAll(originalFrame);
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
        imageToTest = Utils.mat2Image(frame);
        System.out.println("转好了！！！");
        //}
        InfoInput.storeBigImage(imageToTest, Student.studentInfo, InfoInput.getCount());
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
}
