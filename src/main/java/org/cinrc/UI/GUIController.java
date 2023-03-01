package org.cinrc.UI;

import com.gluonhq.charm.glisten.control.TextField;
import javafx.css.CssParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


import org.cinrc.IRDC;
import org.cinrc.parser.CCSParser;
import org.cinrc.parser.LTTNode;
import org.cinrc.util.RCCSFlag;

import java.beans.EventHandler;


public class GUIController {
    @FXML
    Label outputField;

    @FXML
    TextField inputBox;

    @FXML
    Button enumerateBtn;

//    CSSParser // return process template
//    exmport process template
//
//    ProcessContainer
//
//            LTTNode

    public void enumerate(ActionEvent event){
        IRDC.config.add(RCCSFlag.ENUMERATE);
        String process = inputBox.getText();
        if(!process.contains("")){ // adds qutations if not already added
            process = '"' + process + '"';
        }
        CCSParser parser = new CCSParser();
        org.cinrc.process.process.Process p =
                CCSParser.parseLine(process).export();
        LTTNode node = new LTTNode(p);
        node.enumerate(true);
        outputField.setText(String.valueOf(node));
        inputBox.setText("");
        IRDC.config.remove(RCCSFlag.ENUMERATE);
    }


}
