/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.controller;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.opencv_core;
import static org.bytedeco.javacpp.opencv_core.CV_32SC1;
import org.bytedeco.javacpp.opencv_face;
import static org.bytedeco.javacpp.opencv_face.createFisherFaceRecognizer;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;
import test.utils.Utils;
import test.view.AlertBox;
import test.view.InfoInput;

/**
 *
 * @author chenjiaxin
 */
public class RecogController {

    // start button
    private Button cameraButton;

    // student information text region
    private Text text;

    // the recognition frame and photo frame
    private ImageView originalFrame;
    // the student photo frame
    private ImageView sphoto;
    private VBox box;

    // basic scene and pane
    private Scene scene;
    private Pane pane;

    //number of people
    private Text nofpeople;
    private HBox Hbox;
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

    /**
     *
     * @param btn
     * @param text
     * @param sphoto
     * @param box
     * @param originalFrame
     */
    public RecogController(Button btn, Text text, ImageView sphoto, VBox box, ImageView originalFrame, Text nofpeople, HBox Hbox, Pane imgPane) {

        this.cameraButton = btn;
        this.text = text;
        this.sphoto = sphoto;
        this.box = box;

        this.capture = new VideoCapture();
        this.faceCascade = new CascadeClassifier();
        this.absoluteFaceSize = 0;

        this.Hbox = Hbox;
        this.pane = imgPane;
        this.nofpeople = nofpeople;
        // set a fixed width for the frame
        originalFrame.setFitWidth(1280);
        originalFrame.setFitHeight(720);
        // preserve image ratio
        originalFrame.setPreserveRatio(true);

        getNames();
        train();

        // import the classifier
        this.loadCascade("/Users/chenjiaxin/Downloads/opencv-3.3.0/data/haarcascades/haarcascade_frontalface_alt.xml");
    }

    public void startCamera() {
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
                        //System.out.println("store in test file ok");

                        // detect + 画绿框框
                        detectAndDisplay(frame);
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
                                ArrayList<String> sInfo = dispatcher.afterRecog(labels.get(0));

                                // update student info text region
                                text.setText(sInfo.get(0));
                                // update student photo
                                Image simg = new Image("file:" + sInfo.get(1), 400, 700, true, true);
                                sphoto = updateImageView(sphoto, simg);
                                // update box container
                                Platform.runLater(new Runnable() {
                                    public void run() {
                                        Text t = new Text("                               Student Information");
                                        box.getChildren().setAll(t, sphoto, text);
                                    }
                                });
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
                                    public void run() {
                                        AlertBox alert = new AlertBox();
                                        alert.dis();
                                    }
                                });

                            }
                        }

                        // update recognition frame
                        Image imageToShow = Utils.mat2Image(frame);
                        originalFrame = updateImageView(originalFrame, imageToShow);

                        Platform.runLater(new Runnable() {

                            public void run() {
                                pane.getChildren().setAll(originalFrame);
                            }
                        });
                    }

                };

                this.timer = Executors.newSingleThreadScheduledExecutor();
                this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

                // update the button content
                this.cameraButton.setText("Stop Camera");
            } else {
                // log the error
                System.err.println("Failed to open the camera connection...");
            }
        } else {
            // the camera is not active at this point
            this.cameraActive = false;
            // update again the button content
            this.cameraButton.setText("Start Camera");

            // stop the timer
            this.stopAcquisition();
        }
    }

    /**
     * Method for face detection and tracking
     *
     * @param frame it looks for faces in this frame
     */
    private void detectAndDisplay(Mat frame) {
        MatOfRect faces = new MatOfRect();
        Mat grayFrame = new Mat();

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

        // each rectangle in faces is a face: draw them!
        facesArray = faces.toArray();
        facenumber = facesArray.length;
        nofpeople.setText("" + facenumber);
        System.out.println("Num of faces:" + facenumber);
        //ArrayList<Integer> predictedLabels = new ArrayList<>();

        if (facenumber == 0) {
            return;
        }
//
//      // 加框框
        for (int i = 0; i < facenumber; i++) {
            Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(255,255 , 255), 3);
        }
    }

    // 加名字
    public void addName(Mat frame, int label,int conf) {
        if (label == 0 || facenumber == 0) {
            return;
        }
        Imgproc.putText(frame, names.get(label)+" "+conf+"%", facesArray[0].tl(), 1, 2.8, new Scalar(0, 255, 0), 3);
    }

    public static opencv_face.FaceRecognizer train() {

        String trainingDir = "/Users/chenjiaxin/Desktop/cmu1/java/team/teamProj/src/teamproj/training_data";
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
        RecogController.faceRecognizer = faceRecognizer;
        faceRecognizer.save("/Users/chenjiaxin/Desktop/cmu1/java/team/teamProj/src/teamproj/training_data/trainedModel.YAML");
        return faceRecognizer;

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
        System.out.println("confidence:"+(confidence.get(0)/1900));
        int confi = (int) (((10000-confidence.get(0))/10000)*100);
        int predictedLabel = label.get(0);
        
        res[0] = predictedLabel;
        res[1] = confi;

        System.out.println("Predicted label: " + predictedLabel);

//        if (facenumber == 0) {
//            return 0;
//        }
        return res;
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
        this.cameraButton.setDisable(false);
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

    /**
     * Update the ImageView
     *
     * @param view the {@link ImageView} to update
     * @param image the {@link Image} to show
     */
    private ImageView updateImageView(ImageView view, Image image) {
        view = new ImageView(image);
        return view;
    }

    /**
     * Stop the acquisition from the camera and release all the resources
     */
    private void stopAcquisition() {
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
