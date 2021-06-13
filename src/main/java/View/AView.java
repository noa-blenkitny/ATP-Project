package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.Observer;

public abstract class AView implements IView, Observer {

    private static Stage stage;
    protected static MediaPlayer mp;
    protected static Media me;


    /**
     * @param fxmlName the new scene FXML name
     * @param primaryStage The stage where we will replace the scene
     */
    @Override
    public void switchScene(String fxmlName, Stage primaryStage) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlName));
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


    /**
     * menu help button click
     */
    @Override
    public void help()
    {
        try{
            Stage newStage = new Stage();
            newStage.setTitle("Help");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getClassLoader().getResource("helpWindow.fxml"));
            Scene scene = new Scene(root, 800, 500);
            newStage.setScene(scene);
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.show();
        }catch (Exception e){ }
    }
    /**
     * menu about button click
     */
    @Override
    public void about()
    {
        try{
            Stage newStage = new Stage();
            newStage.setTitle("About");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getClassLoader().getResource("aboutWindow.fxml"));
            Scene scene = new Scene(root, 800, 500);
            newStage.setScene(scene);
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.show();
        }catch (Exception e){ }
    }
    /**
     * menu properties button click
     */
    @Override
    public void properties()
    {
        try
        {
            Stage newStage = new Stage();
            newStage.setTitle("Properties");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("properties.fxml"));
            Parent root = fxmlLoader.load();
            AView newView = fxmlLoader.getController();
            newView.setViewModel(this.myViewModel);
            setPreviousStage(getStage());
            setStage(newStage);
            Scene scene = new Scene(root, 800, 500);
            newStage.setScene(scene);
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.setOnHiding((new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    getStage().close();
                    setStage(getPreviousStage());

                }}));
            newStage.showAndWait();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @param Message to indicate what is wrong with the input parameter
     */
    public void invalidParamAlert(String Message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(Message);
        alert.show();
    }
    public void exit(ActionEvent actionEvent) {
        getStage().fireEvent(
                new WindowEvent(getStage(), WindowEvent.WINDOW_CLOSE_REQUEST));
    }


    /**
     * @param scene music according to the specific scene selected
     */
    public void chooseMusic(String scene)
    {
        MediaPlayer.Status status = MediaPlayer.Status.PLAYING;
        if (mp != null)
        {
            status = mp.getStatus();
            mp.stop();

        }
        String path = switch (scene) {
            case "open" -> "resources/media/In-the-Jungle-the-mighty-jungle.mp3";
            case "maze" -> "resources/media/jungleDrums.mp3";
            case "goal" -> "resources/media/Applause-Crowd-Cheering-sound-ef.mp3";
            default -> "";
        };

        me=new Media(new File(path).toURI().toString());
        mp = new MediaPlayer(me);
       // mp.play();
        switch (status)
        {
            case PLAYING -> mp.play();
            case PAUSED -> mp.pause();
            case STOPPED -> mp.stop();
        }
        mp.setOnEndOfMedia(() -> mp.seek(Duration.ZERO));

    }
    public void handleMusic()
    {
        if (mp != null)
        {

            if (mp.getStatus() == MediaPlayer.Status.PLAYING)
            {
                mp.pause();

            }
            else
            {
                mp.play();
            }
        }
    }
    /**
     * getters and setters
     */
    public static Stage getPreviousStage() {
        return previousStage;
    }

    public static void setPreviousStage(Stage previousStage) {
        AView.previousStage = previousStage;
    }

    public static Stage previousStage;
    protected MyViewModel myViewModel;

    public static void setStage(Stage stage) {
        AView.stage = stage;
    }

    public static Stage getStage() {
        return stage;
    }

    public void setPrimaryStage(Stage primaryStage)
    {
        AView.stage = primaryStage;
    }

    public void setViewModel(MyViewModel myViewModel)
    {
        this.myViewModel= myViewModel;
        this.myViewModel.addObserver(this);
    }
}
