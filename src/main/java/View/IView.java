package View;

import ViewModel.MyViewModel;
import javafx.stage.Stage;

public interface IView {
    void switchScene(String fxmlName, Stage primaryStage );
    void setViewModel(MyViewModel myViewModel);
    void help();
    void about();
    void properties();
    void chooseMusic(String Scene);
}
