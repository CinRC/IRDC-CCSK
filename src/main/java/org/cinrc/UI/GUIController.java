package org.cinrc.UI;
import com.gluonhq.charm.glisten.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.cinrc.IRDC;
import org.cinrc.parser.CCSParser;
import org.cinrc.parser.LTTNode;
import org.cinrc.util.RCCSFlag;

import java.awt.*;
import java.beans.EventHandler;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class GUIController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
   TextArea outputField;
    @FXML
    TextField inputBox;
    @FXML
    Button enumerateBtn;

    public void enumerate(ActionEvent event){
        try{
            IRDC.config.add(RCCSFlag.ENUMERATE); // adds enumerate flag to allow enumeration of process
            String process = inputBox.getText(); // gets the user inputed text
            if(process.startsWith("\"")){ // removes qutation marks
                process = process.substring(1);
            }
            if(process.endsWith("\"")){ // removes qutation marks
                process = process.substring(0, process.length() - 1);
            }
            CCSParser parser = new CCSParser();
            org.cinrc.process.process.Process p = parser.parseLine(process).export(); // parses through user input
            LTTNode node = new LTTNode(p);
            node.enumerate(true); // enumerates through process
            outputField.setText(String.valueOf(node)); // outputs process
            inputBox.setText(""); // clears input
            IRDC.config.remove(RCCSFlag.ENUMERATE); // removes enumerate flag
        }
        catch(Exception e){
            IRDC.config.remove(RCCSFlag.ENUMERATE);
            outputField.setText(String.valueOf(e));
        }

    }
    //switches the view from the information page to the main page.
    public void main(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ui/main.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene (root);
        stage.setScene(scene);
        stage.show();
    }
    //switches the view from the main page to the main information page.
    public void infoPage(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/ui/infoPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene (root);
        stage.setScene(scene);
        stage.show();
    }

    public void openLink(ActionEvent event) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://github.com/CinRC/IRDC-CCSK"));
    }
}
