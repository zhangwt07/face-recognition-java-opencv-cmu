/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.view;

import java.util.ArrayList;
import javafx.application.Platform;
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

/**
 *
 * @author chenjiaxin
 */
public class ChangePane {

    public ArrayList<String> show() {
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
        
        TextField id = new TextField();
        id.setPromptText("yitians");

        TextField count = new TextField();
        count.setPromptText("3");

        TextField reason = new TextField();
        reason.setPromptText("complaints");

        grid.add(new Label("AndrewId:"), 0, 0);
        grid.add(id, 1, 0);
        grid.add(new Label("Visit Times:"), 0, 1);
        grid.add(count, 1, 1);
        grid.add(new Label("Visit Reason:"), 0, 2);
        grid.add(reason, 1, 2);

        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);

        loginButton.setDisable(true);

        // Java 8 lambda 表达式进行校验
        count.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    loginButton.setDisable(newValue.trim().isEmpty() || reason.getText().trim().isEmpty());
                }
                );

        reason.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    loginButton.setDisable(newValue.trim().isEmpty() || count.getText().trim().isEmpty());
                }
                );

        dialog.getDialogPane()
                .setContent(grid);

// 默认光标在用户名上
        Platform.runLater(
                () -> id.requestFocus());

// 登录按钮后，将结果转为arrayList
        ArrayList<String> result = new ArrayList<>();
        dialog.setResultConverter(dialogButton
                -> {
            if (dialogButton == loginButtonType) {
                result.add(id.getText());
                result.add(count.getText());
                result.add(reason.getText());
                return result;
            }
            return null;
        }
        );

        dialog.showAndWait();
        //System.out.println("!!!!!!!!!the size of result is: " + result.size());
        return result;
    }
}
