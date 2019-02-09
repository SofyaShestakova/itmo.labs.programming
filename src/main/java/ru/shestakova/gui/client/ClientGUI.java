package ru.shestakova.gui.client;

import static ru.shestakova.model.BookColor.RED;
import static ru.shestakova.utils.GUIUtils.createDialogWindow;

import com.sun.javafx.collections.ObservableListWrapper;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.shestakova.client.ClientApplicationAPI;
import ru.shestakova.client.ConsoleManager;
import ru.shestakova.model.*;
import ru.shestakova.server.ApplicationAPI;
import ru.shestakova.utils.CollectionUtils;
import ru.shestakova.utils.ColorAnimator;

public class ClientGUI extends Application {
    private static final String WINDOW_TITLE = "Лабораторная №8 (клиент)";
    private static final int INITIAL_WIDTH = 720;
    private static final int INITIAL_HEIGHT = 480;
    private LabLocale labLocale = LabLocale.getInstance();

    private static final Insets DEFAULT_MARGIN = new Insets (10, 0, 0, 10);
    private static final Insets DEFAULT_VIEW_MARGIN = new Insets (10, 10, 10, 10);

    private ForkJoinPool pool = new ForkJoinPool ( );
    private ExecutorService executorService = Executors.newFixedThreadPool (5);
    private ColorAnimator animator = new ColorAnimator ( );
    private ApplicationAPI api;

    private BookViewComponent bookViewComponent;
    private TreeView<String> view;

    private TextField bookNameField;
    private TextField bookAuthorField;
    private ChoiceBox<BookSize> bookSizeChoiceBox;
    private ComboBox<BookColor> colorComboBox;
    private Spinner<BookGenre> genreSpinner;

    private Button saveButton;


    private Slider generatedObjectsSlider;
    private Button generateObjectsButton;
    private Button refreshButton;
    private Button startEffectButton;
    private Button stopEffectButton;


    {
        pool.submit (() -> {
            Collection collection = new Collection (new ConcurrentLinkedDeque<> ( ));
            api = new ClientApplicationAPI (null, collection);

            ConsoleManager cm = new ConsoleManager (api);
            cm.readConsole ( );
        });

        bookNameField = new TextField ( );
        bookNameField.setPromptText (labLocale.getString ("Название"));

        bookAuthorField = new TextField ( );
        bookAuthorField.setPromptText (labLocale.getString ("Автор"));

        bookSizeChoiceBox = new ChoiceBox<> ( );
        bookSizeChoiceBox.getItems ( ).addAll (BookSize.values ( ));

        colorComboBox = new ComboBox<> ( );
        colorComboBox.getItems ( ).addAll (Arrays.asList (BookColor.values ( )));



        genreSpinner = new Spinner<> (
                new ObservableListWrapper<> (
                        Arrays.asList (BookGenre.values ( ))
                )
        );

        generatedObjectsSlider = new Slider (1, 5, 1);

        refreshButton = new Button (labLocale.getString ("Обновить"));
        generateObjectsButton = new Button (labLocale.getString ("Генерировать"));
        //removeButton = new Button("Удалить");
        startEffectButton = new Button (labLocale.getString ("Начать анимацию"));
        stopEffectButton = new Button (labLocale.getString ("Остановить анимацию"));

        labLocale.addLocaleChangeHandler(bundle -> {
            refreshButton.setText(bundle.getString("Обновить"));
            generateObjectsButton.setText(bundle.getString("Генерировать"));
            startEffectButton.setText(bundle.getString("Начать анимацию"));
            stopEffectButton.setText(bundle.getString("Остановить анимацию"));
            bookAuthorField.setPromptText (bundle.getString("Автор"));
            bookNameField.setPromptText (bundle.getString("Название"));

        });

    }

    @Override
    public void start(Stage primaryStage) {
        final VBox rootNode = new VBox ( );

        MenuBar menuBar = labLocale.createLocaleMenu ();
        bookViewComponent = createBookView ( );
        VBox criteriasBox = createCriteriasBox ( );
        VBox generatingView = createGeneratingView ( );
        rootNode.getChildren ( ).addAll (
                menuBar,
                new HBox (
                        bookViewComponent,
                        new VBox (
                                criteriasBox,
                                generatingView
                        )
                )
        );

    VBox.setMargin(menuBar, DEFAULT_VIEW_MARGIN);
        VBox.setMargin (bookViewComponent, DEFAULT_VIEW_MARGIN);
        VBox.setMargin (criteriasBox, DEFAULT_VIEW_MARGIN);
        VBox.setMargin (generatingView, DEFAULT_VIEW_MARGIN);

        // Отрисовываем
        primaryStage.setScene (new Scene (rootNode, INITIAL_WIDTH, INITIAL_HEIGHT));
        primaryStage.setTitle (labLocale.getString ("Лабораторная №8 (клиент)"));
        primaryStage.setResizable (false);
        primaryStage.setFullScreen (false);

        primaryStage.centerOnScreen ( );
        primaryStage.show ( );

        labLocale.addLocaleChangeHandler(bundle -> {
            primaryStage.setTitle (bundle.getString ("Лабораторная №8 (клиент)"));

        });

        Runtime.getRuntime ( ).addShutdownHook (new Thread (() -> {
            api.save (null);

            System.out.println ("Ожидаю сохранения данных...");
            pool.shutdown ( );

            try {
                if (!pool.awaitTermination (10, TimeUnit.SECONDS)) {
                    System.err.println ("Слишком долгая запись, аварийное завершение");
                }
            } catch (InterruptedException e) {
                Thread.currentThread ( ).interrupt ( );
            }
        }));
    }

    private String getBookNameValue() {
        return bookNameField.getText ( );
    }

    private String getBookAuthorValue() {
        return bookAuthorField.getText ( );
    }

    private BookSize getBookSizeValue() {
        return bookSizeChoiceBox.getValue ( );
    }

    private BookColor getBookColorValue() {
        return colorComboBox.getValue ( );
    }

    private BookGenre getBookGenreValue() {
        return genreSpinner.getValue ( );
    }

    private int getGeneratedObjectsAmount() {
        return (int) generatedObjectsSlider.getValue ( );
    }
    private BookViewComponent createBookView() {
        Collection collection;
        if (api != null) {
            collection = api.getCollection ( );
        } else {
            collection = new Collection ( );
        }

        if (collection == null) {
            collection = new Collection ( );
        }

        bookViewComponent = new BookViewComponent (collection, 5);
        return bookViewComponent;
    }

    private VBox createGeneratingView() {
        Label label = new Label (labLocale.getString ("Генерировать значение:"));
        VBox generatingView = new VBox ( );

        HBox buttons = new HBox ( );
        buttons.getChildren ( ).addAll (generateObjectsButton, refreshButton);

        HBox effectButtonsBox = new HBox ( );
        effectButtonsBox.getChildren ( ).addAll (startEffectButton, stopEffectButton);

        generatingView.getChildren ( ).addAll (label, generatedObjectsSlider, buttons, effectButtonsBox);

        VBox.setMargin (label, DEFAULT_MARGIN);
        VBox.setMargin (generatedObjectsSlider, DEFAULT_MARGIN);

        HBox.setMargin (startEffectButton, DEFAULT_MARGIN);
        HBox.setMargin (stopEffectButton, DEFAULT_MARGIN);
        HBox.setMargin (generateObjectsButton, DEFAULT_MARGIN);
        HBox.setMargin (refreshButton, DEFAULT_MARGIN);

        generateObjectsButton.setOnMouseClicked (
                event -> pool.submit (() -> Stream.generate (CollectionUtils::generateRandomBook)
                        .limit ((long) getGeneratedObjectsAmount ( ))
                        .forEach (book -> api.add (book)))
        );

        refreshButton.setOnMouseClicked (event -> executorService.submit (() -> {
                    bookViewComponent.setCollection (api.getCollection ( ));
                    Platform.runLater (() -> bookViewComponent.updateComponent ( ));
                })
        );

        startEffectButton.setOnMouseClicked (event -> executorService.submit (() -> {
            String inputName = getBookNameValue ( );
            String inputAuthor = getBookAuthorValue ( );
            BookSize inputSize = getBookSizeValue ( );
            BookColor inputColor = getBookColorValue ( );
            BookGenre inputGenre = getBookGenreValue ( );

            java.util.List<BookComponent> components = bookViewComponent.getBookComponents ( ).stream ( )
                    .filter (component -> (inputName == null || inputName.isEmpty ( )
                            || component.getBook ( ).getName ( ).equals (inputName))) // Фильтруем название книги
                    .filter (component -> (inputAuthor == null || inputAuthor.isEmpty ( )
                            || component.getBook ( ).getAuthor ( )
                            .equals (inputAuthor))) // Фильтруем автора книги
                    .filter (component -> (inputSize == null || (
                            component.getBook ( ).getHeight ( ) == inputSize.getHeight ( )
                                    && (component.getBook ( ).getWidth ( ) == inputSize.getWidth ( )))))
                    .filter (component -> (inputColor == null ||
                            component.getBook ( ).getColor ( )
                                    .equals (inputColor.getColor ( )))) // Фильтруем цвет книги
                    .filter (component -> (inputGenre == null || inputGenre == BookGenre.NONE
                            || component.getBook ( ).getGenre ( ) == getBookGenreValue ( )))
                    .peek (component -> System.out.println (component.getBook ( )))
                    .collect (Collectors.toList ( ));

            if (!animator.isRunning ( )) {
                animator.startAnimation (components);
            }
        }));

        stopEffectButton.setOnMouseClicked (event -> pool.submit (() -> {
                    if (animator.isRunning ( )) {
                        animator.stopAnimation ( );
                    }
                }
        ));
        labLocale.addLocaleChangeHandler(bundle -> {
            label.setText(bundle.getString("Генерировать значение:"));
        });

        return generatingView;
    }

    private VBox createCriteriasBox() {
        final VBox addVBox = new VBox ( );

        view = new TreeView<> (new TreeItem<> ("Коллекция:"));
        view.getRoot ( ).setExpanded (true);

        GridPane grid = new GridPane ( );
        grid.add (addVBox, 1, 0);
        grid.add (view, 0, 0);

        Label label = new Label (labLocale.getString ("Информация_о_новом_элементе"));

        addVBox.getChildren ( )
                .addAll (label, bookNameField, bookAuthorField, bookSizeChoiceBox, colorComboBox,
                        genreSpinner);

        VBox.setMargin (label, DEFAULT_MARGIN);
        VBox.setMargin (bookNameField, DEFAULT_MARGIN);
        VBox.setMargin (bookAuthorField, DEFAULT_MARGIN);
        VBox.setMargin (bookSizeChoiceBox, DEFAULT_MARGIN);
        VBox.setMargin (colorComboBox, DEFAULT_MARGIN);
        VBox.setMargin (genreSpinner, DEFAULT_MARGIN);
        labLocale.addLocaleChangeHandler(bundle -> {
            label.setText(bundle.getString("Информация_о_новом_элементе"));
        });

        return addVBox;
    }
    private MenuBar createMenuBar() {
        // Объявление пунктов контекстного меню
        final MenuBar menuBar = new MenuBar();

        final Menu fileMenu = new Menu("Файл");
        final MenuItem saveOption = new MenuItem("Сохранить");
        final MenuItem loadOption = new MenuItem("Загрузить");
        final MenuItem exitOption = new MenuItem("Выход");
        fileMenu.getItems().addAll(loadOption, saveOption, exitOption);

        menuBar.getMenus().addAll(fileMenu);

        // Save action
        saveOption.setOnAction((event) -> pool.submit(() -> {
            System.out.println("Saving collection");


      /*File file = fileChooser.showOpenDialog(null);
//      File file = fileChooser.showOpenDialog(stage);

      if(file != null) {
        api.save(file.getAbsolutePath());
      }*/

            api.save(null);
        }));
        // Load action
        loadOption.setOnAction((event) -> pool.submit(() -> {
            System.out.println("Loading collection");
            api.load(null);
        }));
        exitOption.setOnAction((event) -> Platform.exit());
        return menuBar;
    }

    private static String getHelpString(String generateButtonText, String loadOptionText, String saveOptionText, String fileMenuText) {
        return "В левой части программы Вы можете видеть Вашу коллекцию элементов класса Book." + '\n'
                + '\n'

                + "Кроме того, новый элемент вы можете добавить при помощи слайдера. "
                + "Слайдером необхожимо указать сколько новых элементов необходимо сгенерировать. "
                + "Затем необходимо нажать "
                + "\"" + generateButtonText + "\". "
                + "Наверняка, вас повеселит результат." + '\n' + '\n'

                + "Есть так же команды "
                + "\"" + loadOptionText + "\" "
                + "и "
                + "\"" + saveOptionText + "\" "
                + "в подменю "
                + "\"" + fileMenuText + "\""
                + ", причём первая выполняется при каждой загрузке, а вторая при каждом выходе из программы."
                + '\n' + '\n'

                + "\nАвтор - Шестакова Софья, гр. P3202.\nСПб, 2019.";
    }
}