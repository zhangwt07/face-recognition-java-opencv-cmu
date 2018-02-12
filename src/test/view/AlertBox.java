package test.view;

import java.util.ArrayList;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import test.controller.Addrecord;

public class AlertBox {

    ArrayList<String> result = new ArrayList<>();

    public ArrayList<String> dis() {
        Stage window = new Stage();
        window.setTitle("Not Found Alert");
        //modality要使用Modality.APPLICATION_MODEL
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(400);
        window.setMinHeight(200);

        // botton: add new person
//        Button btn1 = new Button();
//        btn1.setText("Add new person");
//        btn1.setOnAction(new EventHandler<ActionEvent>() {
//
//            @Override
//            public void handle(ActionEvent event) {
//
//                // a window to add new record
//                Platform.runLater(new Runnable() {
//                    public void run() {
//                        InfoInput ip = new InfoInput();
//                        result = ip.showInput();
//                    }
//                });
//                //window.close();
//                // 如果用户选择要加人
//                if (!result.isEmpty()) {
//                    System.out.println("add a new person");
//                    Addrecord ar = new Addrecord();
//                    ar.addrec(result);
//                }
//            }
//        });

        // botton: close window
        Button btn2 = new Button("Confirm");
        btn2.setOnAction(e -> window.close());

        HBox h = new HBox();
        h.getChildren().addAll(btn2);
        h.setSpacing(20);
        h.setAlignment(Pos.CENTER);

        Label label1 = new Label("This person is not in DB!!");
        Label label2 = new Label("If you want to add this person, please click on the button below");

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label1, label2, h);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        //使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
        window.showAndWait();

        return result;
    }
}
