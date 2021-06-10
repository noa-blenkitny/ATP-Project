//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;

public class Main extends Application {
    public Main() {
    }

    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("MyView.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        primaryStage.setTitle("Jungle Adventure");
        primaryStage.setScene(new Scene(root, 1000.0D, 650.0D));
        primaryStage.show();
        IModel model = new MyModel();
        MyViewModel myViewModel = new MyViewModel(model);
        MyViewController myViewController = (MyViewController)fxmlLoader.getController();
        myViewController.setViewModel(myViewModel);
        myViewController.setPrimaryStage(primaryStage);
        SetStageCloseEvent(primaryStage, model);

    }

    public void SetStageCloseEvent(Stage stage, IModel model){
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    model.stopServers();
                    Platform.exit();
                    System.exit(0);
                }
                else{
                    event.consume();
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
