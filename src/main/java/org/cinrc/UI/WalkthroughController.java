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

    private boolean firstTime = true; // takes the process as initial input and then indexs for the act on
    private CCSInteractionHandler exported = null;
    private ArrayList<Label> actionable = null;

    public void act(){ // not yet working the way we would like, will fix this next
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
                ProcessTemplate template = CCSParser.parseLine(process);
                exported = new CCSInteractionHandler(template.export());
                actionable = exported.getActionableLabels();
                outputField.setText(exported.getProcessRepresentation() +
                        "\n----------------\nPlease input the index of the label you'd like to act on:");
                int i = 0;
                for(Label la : actionable){
                    outputField.setText(outputField.getText() +
                            "\n[" + i++ + "] " + la);
                }

                firstTime = false;
                actInput.setText("");
            }
            else{
                // now takes indexes as input
                Label n = null;
                int index = Integer.parseInt(actInput.getText());
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
                actionable = exported.getActionableLabels();
                outputField.setText(exported.getProcessRepresentation() +
                        "\n----------------\nPlease input the index of the label you'd like to act on:");
                int i = 0;
                for(Label la : actionable){
                    outputField.setText(outputField.getText() +
                            "\n[" + i++ + "] " + la);
                }
                actInput.setText("");
            }
        }
        catch(Exception e){
            actInput.setText("");
            outputField.setText(outputField.getText() + "\n----------------\n" + e);
        }


    }

    public void reset() {
        outputField.setText("");
        firstTime = true;
    }
    public void close(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ui/main.fxml"));
        Stage stage;
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
