/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.IOException;
import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.opencv_core;
import static org.bytedeco.javacpp.opencv_core.CV_32SC1;
import static org.bytedeco.javacpp.opencv_face.createFisherFaceRecognizer;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import org.opencv.core.MatOfRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.Objdetect;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.bytedeco.javacpp.opencv_face;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import sun.swing.plaf.synth.DefaultSynthStyle;
import test.utils.Utils;
import test.view.AlertBox;
import test.controller.InfoDispatcher;

/**
 * FXML Controller class
 *
 * @author Weitong
 */
public class FXMLRecoController implements Initializable {

    @FXML
    private Button btnStartCamera;
    @FXML
    private Button btnReport;
    @FXML
    private ImageView background;
    @FXML
    private ImageView imgCmu;
    @FXML
    private ImageView imgSeal;
    @FXML
    private Button btnLogout;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnRecord;
    @FXML
    private TitledPane txtInfo;
    @FXML
    private Label lblName;
    @FXML
    private Label lblGender;
    @FXML
    private Label lblProgram;
    @FXML
    private Label lblAge;
    @FXML
    private Label lblVisitTime;
    @FXML
    private Label lblReason;

    //number of people
    private Text nofpeople;
    // a timer for acquiring the video stream
    private ScheduledExecutorService timer;
    // the OpenCV object that performs the video capture
    private VideoCapture capture;
    // a flag to change the button behavior
    private boolean cameraActive;

    // face cascade classifier
    private static opencv_face.FaceRecognizer faceRecognizer;
    private CascadeClassifier faceCascade;
    private int absoluteFaceSize;

    private int facenumber = 0;
    Rect[] facesArray = null;
    private HashMap<Integer, String> names = new HashMap<Integer, String>();

    int stranger = 0;
    private Label numberFaces;
    @FXML
    private Label lblNumberFaces;
    static ArrayList<String> sInfo;
    static String currentName;
    static String currentID;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO


        background.setImage(new Image(FXMLloginController.class.getResourceAsStream("images/background.png")));
        imgCmu.setImage(new Image(FXMLloginController.class.getResourceAsStream("images/cmulogoshadow.png")));
        imgSeal.setImage(new Image(FXMLloginController.class.getResourceAsStream("images/cmutransseal.png")));

        btnRecord.setDisable(true);
        btnAdd.setDisable(true);
        btnReport.setDisable(true);

        btnStartCamera.setOnAction((event) -> {
            if (btnStartCamera.getText().equals("Start Camera")) {
                btnStartCamera.setText("Stop Camera");
                btnRecord.setDisable(false);
                btnReport.setDisable(false);
                btnAdd.setDisable(false);
                startCamera();
            } else {
                //stop camera
                stopCamera();
            }

        });

        btnRecord.setOnAction((ActionEvent event) -> {
            System.out.println("record clicked");
            stopCamera();

            try {
                Stage stageNewStudent = new Stage();
                Parent root;
                root = FXMLLoader.load(FXMLRecoController.this.getClass().getResource("FXMLNewStudent.fxml"));
                Scene sceneNewStudent = new Scene(root);
                stageNewStudent.setScene(sceneNewStudent);
                stageNewStudent.setTitle("New Student Record - "+currentName);
                stageNewStudent.show();
            } catch (IOException ex) {
                Logger.getLogger(FXMLloginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        btnAdd.setOnAction((event) -> {
            System.out.println("add clicked");
            stopCamera();

            try {
                Stage stageAdd = new Stage();
                Parent root;
                root = FXMLLoader.load(FXMLRecoController.this.getClass().getResource("FXMLAdd.fxml"));
                Scene sceneAdd = new Scene(root);
                stageAdd.setScene(sceneAdd);
                stageAdd.setTitle("New Student Information");
                stageAdd.show();
            } catch (IOException ex) {
                Logger.getLogger(FXMLloginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        btnReport.setOnAction((event) -> {
            System.out.println("Report clicked");
            stopCamera();

            try {
                Stage stageReport = new Stage();
                Parent root;
                root = FXMLLoader.load(FXMLRecoController.this.getClass().getResource("FXMLReport.fxml"));
                Scene sceneReport = new Scene(root);
                stageReport.setScene(sceneReport);
                stageReport.setTitle("Generate Report");
                stageReport.show();
            } catch (IOException ex) {
                Logger.getLogger(FXMLloginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        btnLogout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("log out clicked");
                Stage stage = (Stage) btnLogout.getScene().getWindow();
                stage.close();

                try {
                    Stage stageStart = new Stage();
                    Parent root;
                    root = FXMLLoader.load(FXMLRecoController.this.getClass().getResource("FXMLlogin.fxml"));
                    Scene scene = new Scene(root);
                    stageStart.setScene(scene);
                    stageStart.setTitle("System Login");
                    stageStart.show();
                } catch (IOException ex) {
                    Logger.getLogger(FXMLloginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
    }

    public void startCamera() {
        this.capture = new VideoCapture();
        this.faceCascade = new CascadeClassifier();
        this.absoluteFaceSize = 0;
        getNames();
        train();
        this.loadCascade("/Users/chenjiaxin/Downloads/opencv-3.3.0/data/haarcascades/haarcascade_frontalface_alt.xml");

        //System.out.println("start");
        // if this camera is off now
        if (!this.cameraActive) {

            // start the video capture
            this.capture.open(0);

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
                        // convert and show the frame
                        Image imageToTest = Utils.mat2Image(frame);
//                        imgCmu.setImage(imageToTest);
                        // show the grabbed image info
                        //System.out.println("Width: " + imageToShow.getWidth());
                        //System.out.println("Height: " + imageToShow.getHeight());
                        // after grap a photo, 先放一张image去test directory中
                        String testFile = "/Users/chenjiaxin/Desktop/cmu1/java/team/teamProj/src/teamproj/test_data/" + "img" + ".png";
                        Utils.storeImg(imageToTest, testFile);
                        // 改图片大小
                        Image imageToTest1 = new Image("file:" + testFile, 240, 150, false, true);
                        //System.out.println("Width1: " + imageToShow1.getWidth());
                        //System.out.println("Height1: " + imageToShow1.getHeight());
                        Utils.storeImg(imageToTest1, testFile);
                        System.out.println("store in test file ok");

                        // detect + 画绿框框
                        detectAndDisplay(frame);
                        System.out.println("detect");
                        // 识别 + 加名字
                        ArrayList<Integer> labels = new ArrayList<>();
                        for (int i = 0; i < facenumber; i++) {
                            int[] res = recog();
                            int predicted = res[0];
                            int conf = res[1];
                            labels.add(predicted);
                            addName(frame, predicted, conf);

                            // show Student information
                            if (labels.size() != 0 && labels.get(0) != 0) {
                                InfoDispatcher dispatcher = new InfoDispatcher();
                                sInfo = dispatcher.afterRecog(labels.get(0));
                                // update student info text region
                                //text.setText(sInfo.get(0));
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Update UI here.
                                        currentName = sInfo.get(0);
                                        currentID = sInfo.get(7);
                                        lblName.setText(sInfo.get(0));
                                        lblGender.setText(sInfo.get(1));
                                        lblProgram.setText(sInfo.get(2));
                                        lblAge.setText(sInfo.get(3));
                                        lblVisitTime.setText(sInfo.get(4));
                                        lblReason.setText(sInfo.get(5));
                                    }
                                });
                                // update student photo
                                Image simg = new Image("file:" + sInfo.get(6), 400, 700, true, true);
                                imgSeal.setImage(simg);
//                                sphoto = updateImageView(sphoto, simg);
                                // update box container
//                                Platform.runLater(new Runnable() {
//                                    public void run() {
//                                        Text t = new Text("                               Student Information");
//                                        box.getChildren().setAll(t, sphoto, text);
//                                    }
//                                });
                            } else if (labels.size() != 0 && labels.get(0) == 0) {
                                // 当遇到陌生人时
                                if (stranger < 15) {
                                    stranger++;
                                    continue;
                                }
                                stranger = 0;
                                System.out.println(stranger);

                                // a window to alert that this person is not in the database
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertBox alert = new AlertBox();
                                        alert.dis();
                                    }
                                });

                            }
                        }

                        // update recognition frame
                        Image imageToShow = Utils.mat2Image(frame);

//                        originalFrame = updateImageView(originalFrame, imageToShow);
//
                        Platform.runLater(new Runnable() {

                            @Override
                            public void run() {
                                imgCmu.setImage(imageToShow);
//                                pane.getChildren().setAll(originalFrame);
                            }
                        });
                    }

                };

                this.timer = Executors.newSingleThreadScheduledExecutor();
                this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

            } else {
                // log the error
                System.err.println("Failed to open the camera connection...");
            }

        }
    }

    public void stopCamera() {
        btnStartCamera.setText("Start Camera");
        btnRecord.setDisable(true);
        btnReport.setDisable(true);
        stopAcquisition();
        imgCmu.setImage(new Image(FXMLloginController.class.getResourceAsStream("images/cmulogoshadow.png")));
        imgSeal.setImage(new Image(FXMLloginController.class.getResourceAsStream("images/cmutransseal.png")));
    }

    /**
     * Method for face detection and tracking
     *
     * @param frame it looks for faces in this frame
     */
    public void detectAndDisplay(Mat frame) {
        MatOfRect faces = new MatOfRect();
        Mat grayFrame = new Mat();
        System.out.println("start detect");
        // convert the frame in gray scale
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        // equalize the frame histogram to improve the result
        Imgproc.equalizeHist(grayFrame, grayFrame);

        // compute minimum face size (20% of the frame height, in our case)
        if (this.absoluteFaceSize == 0) {
            int height = grayFrame.rows();
            if (Math.round(height * 0.2f) > 0) {
                this.absoluteFaceSize = Math.round(height * 0.2f);
            }
        }

        // detect faces
        this.faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
                new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());
        System.out.println("faces");
        // each rectangle in faces is a face: draw them!
        facesArray = faces.toArray();
        facenumber = facesArray.length;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lblNumberFaces.setText("Number of Faces in Camera: " + facenumber);
            }
        });
        //lblNumberFaces.setText("Number of Faces in Camera: "+ facenumber);
//        nofpeople.setText("" + facenumber);
        System.out.println("Num of faces:" + facenumber);
        //ArrayList<Integer> predictedLabels = new ArrayList<>();

        if (facenumber == 0) {
            return;
        }
//
//      // 加框框
        for (int i = 0; i < facenumber; i++) {
            Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(255, 255, 255), 3);
        }
    }

    /**
     * Face Recognition for students
     *
     * @return predictedLabel
     */
    public int[] recog() {
        int[] res = new int[2];

        org.bytedeco.javacpp.opencv_core.Mat testImage = imread("/Users/chenjiaxin/Desktop/cmu1/java/team/teamProj/src/teamproj/test_data/img.png", CV_LOAD_IMAGE_GRAYSCALE);

        // predict the test image label
        IntPointer label = new IntPointer(1);
        DoublePointer confidence = new DoublePointer(1);
        this.faceRecognizer.predict(testImage, label, confidence);
        System.out.println("confidence:" + (confidence.get(0) / 1900));
        int confi = (int) (((10000 - confidence.get(0)) / 10000) * 100);
        int predictedLabel = label.get(0);

        res[0] = predictedLabel;
        res[1] = confi;

        System.out.println("Predicted label: " + predictedLabel);

//        if (facenumber == 0) {
//            return 0;
//        }
        return res;
    }

    /**
     * Get a frame from the opened video stream (if any)
     *
     * @return the {@link Image} to show
     */
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

    private void getNames() {

        // init the database connection
        try {
            Class.forName("com.mysql.jdbc.Driver");

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/Students", "root", "login");

            Statement stmt = connection.createStatement();
            String queryStmt = "SELECT name, label FROM StudentInfo";
            ResultSet rs = stmt.executeQuery(queryStmt);
            while (rs.next()) {
                String name = rs.getString("name");
                int label = rs.getInt("label");
                names.put(label, name);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void addName(Mat frame, int label, int conf) {
        if (label == 0 || facenumber == 0) {
            return;
        }
        Imgproc.putText(frame, names.get(label) + " " + conf + "%", facesArray[0].tl(), 1, 2.8, new Scalar(0, 255, 0), 3);
    }

    public static opencv_face.FaceRecognizer train() {

        String trainingDir = "/Users/chenjiaxin/Desktop/test/src/test/training_data";
        File root = new File(trainingDir);
        // process the name of images
        FilenameFilter imgFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                name = name.toLowerCase();
                return name.endsWith(".JPG") || name.endsWith(".jpg") || name.endsWith(".png");
            }
        };

        File[] imageFiles = root.listFiles(imgFilter);

        // convert images to matvector
        opencv_core.MatVector images = new opencv_core.MatVector(imageFiles.length);

        org.bytedeco.javacpp.opencv_core.Mat labels = new org.bytedeco.javacpp.opencv_core.Mat(imageFiles.length, 1, CV_32SC1);
        IntBuffer labelsBuf = labels.createBuffer();

        int counter = 0;

        // get all the labels
        for (File image : imageFiles) {
            org.bytedeco.javacpp.opencv_core.Mat img = imread(image.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);

            int label = Integer.parseInt(image.getName().split("\\-")[0]);

            images.put(counter, img);

            labelsBuf.put(counter, label);

            counter++;
        }

        // 5, 1500
        opencv_face.FaceRecognizer faceRecognizer = createFisherFaceRecognizer(16, 1900);
        double threshold = faceRecognizer.getThreshold();
        //System.out.println(threshold);

        // train the face recogniton model
        faceRecognizer.train(images, labels);
        FXMLRecoController.faceRecognizer = faceRecognizer;
        faceRecognizer.save("/Users/chenjiaxin/Desktop/test/src/test/training_data/trainedModel.YAML");
        return faceRecognizer;

    }

    /**
     * Method for loading a classifier trained set from disk
     *
     * @param classifierPath the path on disk where a classifier trained set is
     * located
     */
    private void loadCascade(String classifierPath) {
        // load the classifier(s)
        this.faceCascade.load(classifierPath);

        // now the video capture can start
        //btnStartCamera.setDisable(false);
    }

    /**
     * Stop the acquisition from the camera and release all the resources
     */
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
