package ru.shestakova.utils;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

public class GUIUtils {
  /**
   * Метод, конструирующий диалоговые окна, готовые к отображению
   */
  public static Dialog createDialogWindow(String header, String content) {
    Dialog dialog = new Dialog ( );
    dialog.setTitle (header);
    dialog.setContentText (content);
    dialog.getDialogPane ( ).getButtonTypes ( ).add (ButtonType.FINISH);

    //следующая строка  для разбиения
    // диалогового окна на строки
    dialog.getDialogPane ( ).getChildren ( ).stream ( )
        .filter (node -> node instanceof Label)
        .forEach (node -> ((Label) node).setMinHeight (Region.USE_PREF_SIZE));
    return dialog;
  }
}
