package org.cinrc.UI;

import com.gluonhq.charm.glisten.control.TextField;
import javafx.css.CssParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


import org.cinrc.parser.LTTNode;

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
        String process = inputBox.getText();
        inputBox.setText("");
        outputField.setText(process);
    }


}
