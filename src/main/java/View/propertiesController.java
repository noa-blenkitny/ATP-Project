package View;

import Model.IModel;
import Server.Configurations;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.lang.module.Configuration;
import java.net.URL;
import java.util.Observable;
import java.util.Optional;
import java.util.ResourceBundle;

public class propertiesController extends AView implements Initializable
{
    public Configurations config;
    public ChoiceBox<String> searchingAlgorithm;
    public ChoiceBox<String> generatingAlgorithm;
    public TextField threadSize;
    private Stage newStage;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        config = Configurations.getInstance();
        generatingAlgorithm.getItems().addAll("Empty" , "Simple" , "My");
        searchingAlgorithm.getItems().addAll("Best" , "BFS" , "DFS");
        generatingAlgorithm.setValue(config.getMazeGeneratingAlgorithm().getName());
        searchingAlgorithm.setValue("BFS");
        threadSize.setText(String.valueOf(config.getThreadPoolSize()));
        generatingAlgorithm.getSelectionModel().selectedItemProperty().addListener((v,oldValue, newValue)->generatingAlgorithm.setValue(newValue));
        searchingAlgorithm.getSelectionModel().selectedItemProperty().addListener((v,oldValue, newValue)->searchingAlgorithm.setValue(newValue));

    }

    /**
     * on update button click will update the configuration properties
     */
    public void updateClicked(){


        if(checkParam(threadSize.getText()) == true)
        {
            config.setMazeSearchingAlgorithm(searchingAlgorithm.getValue());
            config.setMazeGeneratingAlgorithm(generatingAlgorithm.getValue());
            config.setThreadPoolSize(threadSize.getText());
            getStage().close();
            setStage(getPreviousStage());

        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Invalid parameter entered.\nPlease enter an integer greater than 0.");
            alert.show();
        }
    }

    /**
     * @param num the param we want to validate
     * @return true if correct, false if not
     */
    public boolean checkParam(String num)
    {
            try
            {
                if(Integer.parseInt(num) < 1)
                    return false;
            }
            catch(NumberFormatException ex)
            {
                return false;
            }
            return true;
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
