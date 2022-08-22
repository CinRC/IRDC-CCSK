package me.gmx.thread;

import javafx.application.Application;
import me.gmx.UI.RCCS_FX;

public class GUIThread extends Thread{

    public GUIThread(String s){
        super(s);
    }

    public void run(){
        Application.launch(RCCS_FX.class);
    }

}
