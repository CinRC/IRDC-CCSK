package org.cinrc.UI;

import javafx.application.Application;

public class GUIThread extends Thread {

  public GUIThread(String s) {
    super(s);
  }

  public void run() {
    Application.launch(RCCS_FX.class);
  }

}
