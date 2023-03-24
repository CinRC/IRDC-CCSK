package org.cinrc.UI;
import com.gluonhq.charm.glisten.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.cinrc.CCSInteractionHandler;
import org.cinrc.IRDC;
import org.cinrc.parser.CCSParser;
import org.cinrc.process.ProcessTemplate;
import org.cinrc.process.nodes.Label;
import org.cinrc.util.RCCSFlag;

import java.io.IOException;
import java.util.ArrayList;

public class WalkthroughController {

    @FXML
    TextArea outputField;
    @FXML
    TextField actInput;
    @FXML
    Button act;

    @FXML
    Button genRandomProcess;

    private boolean firstTime = true; // takes the process as initial input and then indexs for the act on
    private CCSInteractionHandler exported = null;
    private ArrayList<Label> actionable = null;

    public void genRandom() {
        reset();
        String[] rProcesses = {"((a)|('a))+((a)|('a))", "(a|'a)\\{a}", "a.b.c.d | 'a.'b.'c.'d", // generic processes
                "(a.b | 'b.'a)\\{a, b}", "(a)\\{'a}", // stuck processes
                "a.a.(b+c)", "a.a.b + a.a.c",  // bisiumlation
                "a.P", "a.b.P", "a.(b.X|Y)", "a.P\\{b}", "a.P|b.Q", "a.P + b.Q", "a.P|'a.Q"
        }; // examples from LTS/SOS
        actInput.setFloatText("");
        int randomNum = (int) Math.floor(Math.random() * rProcesses.length);
        actInput.setText(rProcesses[randomNum]);
    }

    public void act(){
        //takes a proccess to start
        try{
            if(firstTime){
                String process = actInput.getText();
                if(process.startsWith("\"")){
                    process = process.substring(1);
                }
                if(process.endsWith("\"")){
                    process = process.substring(0, process.length() - 1);
                }
                outputField.setText("");
                IRDC.config.add(RCCSFlag.GUI);
                ProcessTemplate template = CCSParser.parseLine(process); // parses the initial input
                exported = new CCSInteractionHandler(template.export());
                actionable = exported.getActionableLabels(); // gets all labels of exported process
                outputField.setText(exported.getProcessRepresentation() +
                        "\n----------------\nPlease input the index of the label you'd like to act on:");
                int i = 0;
                for(Label la : actionable){ // displays all actionable lables in "[i] "label format"
                    outputField.setText(outputField.getText() +
                            "\n[" + i++ + "] " + la);
                }

                firstTime = false;
                actInput.setText("");
            }
            else{
                // now takes indexes as input
                Label n = null;
                int index = Integer.parseInt(actInput.getText()); // takes user input in form of index, e.g. 0,1,2,3..
                try {
                    n = actionable.get(index);
                } catch (Exception e) {
                    outputField.setText(outputField.getText() + "\n------------------\nFailed to recogonize label!");
                }
                try{
                    exported.getContainer().act(n);
                }catch(Exception e){
                    outputField.setText(outputField.getText() + "\n------------------\nCould not act on label!");
                }
                actionable = exported.getActionableLabels(); // gets new actionable labels
                outputField.setText(exported.getProcessRepresentation() +
                        "\n----------------\nPlease input the index of the label you'd like to act on:");
                int i = 0;
                for(Label la : actionable){ // displays all actionable lables in "[i] "label format"
                    outputField.setText(outputField.getText() +
                            "\n[" + i++ + "] " + la);
                }
                actInput.setText("");
            }
        }
        catch(Exception e){
            actInput.setText("");
            outputField.setText(outputField.getText() + "\n----------------\n" + e); // displays errors if encountered
        }
    }

    public void reset() { // resets page, clears input and sets first to true to allow a process to be taken as input
        outputField.setText("");
        firstTime = true;
    }
    public void close(ActionEvent event) throws IOException { // displays landing page
        Parent root = FXMLLoader.load(getClass().getResource("/ui/main.fxml"));
        Stage stage;
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
