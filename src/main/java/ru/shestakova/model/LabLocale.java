package ru.shestakova.model;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class LabLocale {
    private static LabLocale instance = null;

    public static LabLocale getInstance() {
        return instance == null ? instance = new LabLocale ( ) : instance;
    }

    private Locale currentLocale = null;
    private ResourceBundle resourceBundle = null;
    private List<LabLocaleUpdateHandler> labLocaleUpdateHandlers = null;

    private LabLocale() {
        currentLocale = Locale.getDefault ( );
        resourceBundle = ResourceBundle.getBundle ("ru.shestakova.model.SyntaxBundle", currentLocale);
        labLocaleUpdateHandlers = new ArrayList<> ( );
    }


    public MenuBar createLocaleMenu() {
        // Объявление пунктов контекстного меню
        final MenuBar menuBar = new MenuBar ( );
        final Menu languageMenu = new Menu ("Язык");
        final MenuItem russianLanguageOption = new MenuItem ("Русский");
        final MenuItem turkishLanguageOption = new MenuItem ("Турецкий");
        final MenuItem lithuanianLanguageOption = new MenuItem ("Литовский");
        final MenuItem spanishLanguageOption = new MenuItem ("Испанский");
        languageMenu.getItems ( ).addAll (russianLanguageOption,turkishLanguageOption,lithuanianLanguageOption,spanishLanguageOption);
        menuBar.getMenus().addAll(languageMenu);
        addLocaleChangeHandler(bundle -> {
            languageMenu.setText(resourceBundle.getString("Язык"));
            russianLanguageOption.setText(resourceBundle.getString("Русский"));
            turkishLanguageOption.setText(resourceBundle.getString("Турецкий"));
            lithuanianLanguageOption.setText(resourceBundle.getString("Литовский"));
            spanishLanguageOption.setText(resourceBundle.getString("Испанский"));
        });

        russianLanguageOption.setOnAction (event -> setLocale(new Locale ("ru")));
        turkishLanguageOption.setOnAction (event -> setLocale(new Locale("tu")));
        lithuanianLanguageOption.setOnAction (event -> setLocale(new Locale("li")));
        spanishLanguageOption.setOnAction (event -> setLocale(new Locale("sp")));

        return menuBar;
    }
    public void addLocaleChangeHandler(LabLocaleUpdateHandler labLocaleUpdateHandler) {
        labLocaleUpdateHandlers.add (labLocaleUpdateHandler);
    }

    public String getString(String key) {
        return resourceBundle.getString (key);
    }

    private void setLocale(Locale locale) {
        Locale.setDefault (locale);
        currentLocale = locale;
        resourceBundle = ResourceBundle.getBundle ("ru.shestakova.model.SyntaxBundle", currentLocale);
        if (labLocaleUpdateHandlers != null)
            labLocaleUpdateHandlers.forEach (labLocaleUpdateHandler -> labLocaleUpdateHandler.handle (resourceBundle));
    }

    public Locale getLocale() {
        return currentLocale;
    }
}
