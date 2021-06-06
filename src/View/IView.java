package View;

import ViewModel.MyViewModel;
import javafx.stage.Stage;

public interface IView {
    void switchScene(String fxmlName, Stage primaryStage );
    void setViewModel(MyViewModel myViewModel);
    void save();
    void load();
    void New();
    void help();
    void about();
    void properties();
}
