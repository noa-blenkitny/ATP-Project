package View;

import ViewModel.MyViewModel;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Observer;

public abstract class AView implements IView, Observer {
    private MyViewModel myViewModel;
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

    @Override
    public void switchScene()
    {

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
