package org.cinrc.UI;

import com.gluonhq.charm.glisten.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.cinrc.IRDC;
import org.cinrc.parser.CCSParser;
import org.cinrc.parser.LTTNode;
import org.cinrc.util.RCCSFlag;
import java.beans.EventHandler;
public class GUIController {
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
}
