package View;

import Server.Configurations;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.lang.module.Configuration;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

public class propertiesController extends AView implements Initializable
{
    public Configurations config;
    public ChoiceBox<String> searchingAlgorithm;
    public ChoiceBox<String> generatingAlgorithm;
    public TextField threadSize;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        generatingAlgorithm.getItems().addAll("Empty" , "Simple" , "My");
        searchingAlgorithm.getItems().addAll("Best" , "BFS" , "DFS");

     try {
         config = Configurations.getInstance();
         String searchingAlgo = config.getMazeSearchingAlgorithm().getName();
         String generatingAlgo = config.getMazeGeneratingAlgorithm().getName();
         switch (searchingAlgo)
         {
             case "BestFirstSearch":
                 generatingAlgorithm.setValue("Best");
                 break;
             case "DepthFirstSearch":
                 generatingAlgorithm.setValue("DFS");
                 break;
             case "BreadthFirstSearch":
                 generatingAlgorithm.setValue("BFS");
                 break;
         }
         switch (generatingAlgo)
         {
             case "MyMazeGenerator":
                 generatingAlgorithm.setValue("My");
                 break;
             case "SimpleMazeGenerator":
                 generatingAlgorithm.setValue("Simple");
                 break;
             case "EmptyMazeGenerator":
                 generatingAlgorithm.setValue("Empty");
                 break;
         }

         threadSize.setText(String.valueOf(config.getThreadPoolSize()));
     }

     catch (Exception e)
     {
         e.printStackTrace();
     }
    }
    public void updateClicked(){


        if(checkParam(threadSize.getText()) == true)
        {
            config.setMazeGeneratingAlgorithm(generatingAlgorithm.getValue());
            config.setMazeSearchingAlgorithm(searchingAlgorithm.getValue());
            config.setThreadPoolSize(threadSize.getText());
            getStage().close();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Invalid parameter entered.\nPlease enter an integer greater than 0.");
            alert.show();
        }
    }

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
