/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.view;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javax.imageio.ImageIO;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import test.FXMLTakePhotoController;
import test.controller.StoreStudent;
import test.utils.Utils;
import test.Test;

/**
 *
 * @author chenjiaxin
 */
public class InfoInput {

    public ArrayList<String> showInput() {

        Dialog<ArrayList<String>> dialog = new Dialog<>();
        dialog.setTitle("Personal Information Input");
        dialog.setHeaderText("Carnegie Mellon University");

        // 设置头部图片
        dialog.setGraphic(new ImageView(new Image("file:/Users/chenjiaxin/Desktop/cmu1/java/team/teamProj/src/teamproj/icon/cmu.jpg")));

        ButtonType loginButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, cancel);

        GridPane grid = new GridPane();

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField name = new TextField();
        name.setPromptText("Rachel Sun");

        TextField id = new TextField();
        id.setPromptText("yitians");

        TextField age = new TextField();
        age.setPromptText("22");

        TextField program = new TextField();
        program.setPromptText("MISM");

        TextField photo = new TextField();
        photo.setPromptText("photo path");

        TextField reason = new TextField();
        reason.setPromptText("complaints");

        TextField gender = new TextField();
        reason.setPromptText("gender:");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(name, 1, 0);
        grid.add(new Label("AndrewId:"), 0, 1);
        grid.add(id, 1, 1);
        grid.add(new Label("Age:"), 0, 2);
        grid.add(age, 1, 2);
        grid.add(new Label("Program:"), 0, 3);
        grid.add(program, 1, 3);
        grid.add(new Label("Reason:"), 0, 5);
        grid.add(reason, 1, 4);
        grid.add(new Label("Gender:"), 0, 6);
        grid.add(gender, 1, 5);

        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);

        loginButton.setDisable(true);

        // Java 8 lambda 表达式进行校验
        name.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    loginButton.setDisable(newValue.trim().isEmpty() || id.getText().trim().isEmpty());
                }
                );

        id.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    loginButton.setDisable(newValue.trim().isEmpty() || name.getText().trim().isEmpty());
                }
                );
        age.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    loginButton.setDisable(newValue.trim().isEmpty() || id.getText().trim().isEmpty());
                }
                );

        dialog.getDialogPane()
                .setContent(grid);

// 默认光标在用户名上
        Platform.runLater(
                () -> name.requestFocus());

// 登录按钮后，将结果转为arrayList
        ArrayList<String> result = new ArrayList<>();
        dialog.setResultConverter(dialogButton
                -> {
            if (dialogButton == loginButtonType) {
                result.add(name.getText());
                result.add(id.getText());
                result.add(age.getText());
                result.add(program.getText());
                //result.add(photo.getText());
                result.add(reason.getText());
                result.add(gender.getText());
                return result;
            }
            return null;
        }
        );

        dialog.showAndWait();
        return result;
    }

    //240*150 training
    //get the current number in the database
    public static int getCount() {
        StoreStudent ss = new StoreStudent();
        return ss.getCount() + 1;
    }

    // getPicture() method is to get trainning pictures for strangers.
    //return an image
    public Image getPicture() {
        VideoCapture capture = new VideoCapture();
        boolean cameraActive = false;
        Image imageToTest = null;
        Mat frame = null;

        // if this camera is off now
        if (!cameraActive) {

            // start the video capture
            capture.open(0);

            // is the video stream available?
            if (capture.isOpened()) {
                cameraActive = true;
                frame = new Mat();

                try {
                    // read the current frame
                    capture.read(frame);
                    //System.out.println("read frame success!");
                } catch (Exception e) {
                    // log the (full) error
                    System.err.println("Exception during the image elaboration: " + e);
                }
            }

            // convert and show the frame
            imageToTest = Utils.mat2Image(frame);
        }
        return imageToTest;
    }

    //store the training picture
    //input image got from getPicture(), ArrayList from showinput(), count from getCount
    public static String storeImg(Image image, ArrayList<String> arraylist, int count, int click) {
        String name = arraylist.get(0);

        String testFile = "/Users/chenjiaxin/Desktop/test/src/test/training_data/" + count + "-" + name + "_"+click + ".jpg";
        Utils.storeImg(image, testFile);
        Image image1 = new Image("file:" + testFile, 240,150 , false, true);
        Utils.storeImg(image1, testFile);
        
        return testFile;
    }

    public static void storeBigImage(Image image, ArrayList<String> arraylist, int count) {
        String name = arraylist.get(0);

        String testFile = "/Users/chenjiaxin/Desktop/test/src/test/photos/" + count + "-" + name+ ".jpg";
        
        
        File outputFile = new File(testFile);

        
        //String testFile = "/Users/chenjiaxin/Desktop/cmu1/java/team/teamProj/src/teamproj/training_data/" + count + "-" + name + click + ".jpg";
        Utils.storeImg(image, testFile);
        Image image1 = new Image("file:" + testFile, 800,800 , false, true);
        Utils.storeImg(image1, testFile);




//Utils.storeImg(image1, testFile);
        arraylist.add(testFile); //

        //return arraylist;
    }

}
