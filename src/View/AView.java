package View;

import ViewModel.MyViewModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Observer;

public abstract class AView implements IView, Observer {
    private static Stage stage;
    protected MyViewModel myViewModel;



    public static Stage getStage() {
        return stage;
    }

    public void setPrimaryStage(Stage primaryStage)
    {
        AView.stage = primaryStage;
    }

    @Override
    public void switchScene(String fxmlName, Stage primaryStage) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlName));
        try {
            Parent root = fxmlLoader.load();
            AView newView = fxmlLoader.getController();
            newView.setViewModel(this.myViewModel);
            Scene newScene = new Scene(root,1000,650);

            primaryStage.setScene(newScene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setViewModel(MyViewModel myViewModel)
    {
        this.myViewModel= myViewModel;
        this.myViewModel.addObserver(this);
    }

    @Override
    public void help()
    {
        try{
            Stage newStage = new Stage();
            newStage.setTitle("Help");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("helpWindow.fxml"));
            Scene scene = new Scene(root, 1000, 500);
            newStage.setScene(scene);
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.show();
        }catch (Exception e){ }
    }

    @Override
    public void about()
    {

    }

    @Override
    public void properties()
    {

    }
    public void invalidParamAlert(String Message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(Message);
        alert.show();
    }
}
