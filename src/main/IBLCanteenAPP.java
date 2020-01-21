/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Robot;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 *
 * @author DaCodin
 */
public class IBLCanteenAPP extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/views/TP_Section.fxml"));

        Scene scene = new Scene(root);

        stage.getIcons().add(new Image(IBLCanteenAPP.class.getResourceAsStream("/image/tool.png")));

        stage.setScene(scene);
        //set stage to undecorate to disable minimize, maximize and close buttons

        stage.initStyle(StageStyle.UNDECORATED);

        stage.show();
//stage.initModality(Modality.APPLICATION_MODAL);
        stage.setFullScreenExitHint("....");
        //make application startup with full screen and disable esc to exit full screen
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        //stage.setFullScreen(true);
        stage.setMaximized(true);

        Platform.setImplicitExit(false);

//Prevent putting another window on application
//
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    try {
                        Thread.sleep(100); //buy little millieseconds
                    } catch (InterruptedException e) {
                    }

                    Platform.runLater(() -> {

//                         stage.toFront();
                        // stage.setAlwaysOnTop(true);
                        //bring your UI on top of everyone
                    });

                }

            }
        }).start();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);

    }

}
