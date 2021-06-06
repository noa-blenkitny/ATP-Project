package View;

import ViewModel.MyViewModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Observer;

public abstract class AView implements IView, Observer {
    private static Stage stage;
    protected MyViewModel myViewModel;
    @Override
    public void save()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(("./resources")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Maze Files (*.maze)", "*.maze")
        );
        fileChooser.setInitialFileName("mazeGame");
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            myViewModel.saveMaze(file.getPath());
        }
    }

    @Override
    public void load() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("./resources"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Maze Files (*.maze)", "*.maze")
        );
        File file = fileChooser.showOpenDialog(null);
        if(file != null)
        {
            myViewModel.loadMaze(file.getPath());
        }
    }

    @Override
    public void New() {


    }

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

    }

    @Override
    public void about()
    {

    }

    @Override
    public void properties()
    {

    }
}
