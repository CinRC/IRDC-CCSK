package org.cinrc.UI;
import com.gluonhq.charm.glisten.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.cinrc.CCSInteractionHandler;
import org.cinrc.IRDC;
import org.cinrc.parser.CCSParser;
import org.cinrc.parser.LTTNode;
import org.cinrc.process.ProcessTemplate;
import org.cinrc.process.nodes.Label;
import org.cinrc.util.RCCSFlag;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.ResourceBundle;

public class GUIController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public TextArea outputField;
    @FXML
    public TextField inputBox;

    @FXML
    ChoiceBox<String> myChoiceBox;
    @FXML
    Button genRandomProcess;

    private String[] choices = {"Walkthrough", "Enumerate", "Equivalence"}; // values for dropdown box
                                                              // populated in initailize

    public void genRandom() {
        outputField.setText("");
        // processes taken from https://github.com/CinRC/IRDC-CCSK/blob/dev/docs/example_processes.md
        String[] rProcesses = {"((a)|('a))+((a)|('a))", "(a|'a)\\{a}", "a.b.c.d | 'a.'b.'c.'d", // generic processes
                "(a.b | 'b.'a)\\{a, b}", "(a)\\{'a}", // stuck processes
                "a.a.(b+c)", "a.a.b + a.a.c",  // bisiumlation
                "a.P", "a.b.P", "a.(b.X|Y)", "a.P\\{b}", "a.P|b.Q", "a.P + b.Q", "a.P|'a.Q"
        }; // examples from LTS/SOS
        inputBox.setFloatText("");
        int randomNum = (int) Math.floor(Math.random() * rProcesses.length);
        inputBox.setText(rProcesses[randomNum]);
    }

    public void evaluate(ActionEvent event) throws IOException {
        String action = myChoiceBox.getValue();
        if(action.equals("Enumerate")){
            enumerate();
        }
        if(action.equals("Walkthrough")){
            walkthroughPage(event, inputBox.getText());
        }
        if(action.equals("Equivalence")){
            equivalence();
        }
    }

    private void equivalence() {
        //does not do anything yet
    }

    public void enumerate(){
        try{
            IRDC.config.add(RCCSFlag.ENUMERATE); // adds enumerate flag to allow enumeration of process
            String process = inputBox.getText();
            if(process.startsWith("\"")){
                process = process.substring(1);
            }
            if(process.endsWith("\"")){
                process = process.substring(0, process.length() - 1);
            }
            CCSParser parser = new CCSParser();
            org.cinrc.process.process.Process p = parser.parseLine(process).export(); // parses through user input
            LTTNode node = new LTTNode(p);
            node.enumerate(true); // enumerates through process
            outputField.setText(String.valueOf(node)); // outputs process
            inputBox.setText("");
            IRDC.config.remove(RCCSFlag.ENUMERATE);
        }
        catch(Exception e){
            IRDC.config.remove(RCCSFlag.ENUMERATE);
            outputField.setText(String.valueOf(e));
        }
    }
    public void walkthroughPage(ActionEvent event, String proccess) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/ui/walkthroughPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene (root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }
    //switches the view from the main page to the main information page.
    public void infoPage(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/ui/infoPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene (root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    public void openLink(ActionEvent event) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://github.com/CinRC/IRDC-CCSK"));
    }
    public void openReadme(ActionEvent event) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://github.com/CinRC/IRDC-CCSK/blob/master/README.md"));
    };

    public void openProcessExamples(ActionEvent event) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://github.com/CinRC/IRDC-CCSK/blob/dev/docs/example_processes.md"));
    };
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        myChoiceBox.getItems().addAll(choices);
        myChoiceBox.setValue("Walkthrough");
    }
}
