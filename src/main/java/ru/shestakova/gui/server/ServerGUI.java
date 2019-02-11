package ru.shestakova.gui.server;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.shestakova.gui.client.DataBaseClient;
import ru.shestakova.model.*;
import ru.shestakova.server.ApplicationAPI;
import ru.shestakova.server.ServerApplicationAPI;
import ru.shestakova.server.ServerMain;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

import static ru.shestakova.utils.GUIUtils.createDialogWindow;




public class ServerGUI extends Application {

  private static final int SERVER_PORT = 32283;

  private static final String AUTH_WINDOW_TITLE = "Лабораторная №8 (сервер) | Авторизация";

  private static final Insets DEFAULT_MARGIN = new Insets(5, 10, 0, 10);
  private static final Insets DEFAULT_VIEW_MARGIN = new Insets(10, 10, 10, 10);
  private static final int INITIAL_WIDTH = 320;
  private static final int INITIAL_HEIGHT = 240;
  private static final Insets BUTTON_MARGIN = new Insets(10, 130, 0, 130);

  private ForkJoinPool pool = new ForkJoinPool();
  private ApplicationAPI api;
  private Collection collection;
  private TableView <Book> collectionTable;

  private BookComponent chosenBookComponent;

  /* AUTH ELEMENTS */
  private Button loginClearButton;

  /* CONTROL ELEMENTS */
  private TextField bookNameTextField;
  private TextField bookAuthorTextField;
  private ChoiceBox<BookSize> bookSizeChoiceBox;
  private ComboBox<BookColor> bookColorComboBox;
  private Spinner<BookGenre> bookGenreSpinner;
  private TextField bookPagesAmountTextField;


  private Button filterButton;
  private Button clearFieldsButton;
  private Button refreshButton;

  private Button addButton;
  private Button deleteButton;
  private Button addIfMaximumButton;
  private Button addIfMinimumButton;
  private Button deleteFirstButton;
  private Button deleteLastButton;
  private Button deleteGreaterButton;
  private Button deleteLessButton;

  private BookViewComponent bookViewComponent;
  private Button loginButton;

  private TextField usernameInput;
  private PasswordField userPasswordInput;

  private FileChooser fileChooser = new FileChooser();
  private Stage stage;
  private static ObjectMapper mapper = new ObjectMapper()
          .findAndRegisterModules();
  private static DataBaseClient<Book> databaseClient = DataBaseClient.getInstance();


  {
    pool.submit(() -> {
      api = new ServerApplicationAPI();

      try {
        ServerSocket server = new ServerSocket(SERVER_PORT, 0,
            InetAddress.getByName("localhost"));

        System.out.println("Started server at port: " + SERVER_PORT);

        while (true) {
          Socket socket = server.accept();
          System.out
              .println("Accepted connection from " + socket.getInetAddress().getHostAddress());
          new ServerMain(socket, api);
          Thread.sleep(50);
        }
      } catch (Exception e) {
        System.out.println("init error: " + e);
      }
    });

    usernameInput = new TextField();
    usernameInput.setPromptText("Логин");

    userPasswordInput = new PasswordField();
    userPasswordInput.setPromptText("Пароль");

    loginButton = new Button("Войти");

    /* AUTH ELEMENTS INITIALIZATION */

    loginClearButton = new Button("Очистить");


    /* CONTROL ELEMENTS INITIALIZATION */
    bookNameTextField = new TextField();
    bookNameTextField.setPromptText("Название");

    bookAuthorTextField = new TextField();
    bookAuthorTextField.setPromptText("Автор");

    bookSizeChoiceBox = new ChoiceBox<>();
    bookSizeChoiceBox.getItems().addAll(BookSize.values());

    bookColorComboBox = new ComboBox<>();
    bookColorComboBox.getItems().addAll(Arrays.asList(BookColor.values()));

    bookGenreSpinner = new Spinner<>();
    bookGenreSpinner = new Spinner<>(
        new ObservableListWrapper<>(
            Arrays.asList(BookGenre.values())
        )
    );

    bookPagesAmountTextField = new TextField();
    bookPagesAmountTextField.setPromptText("Количество страниц");

    filterButton = new Button("Выбрать");
    clearFieldsButton = new Button("Очистить");
    refreshButton = new Button("Обновить");

    addButton = new Button("Добавить");
    deleteButton = new Button("Удалить");
    addIfMaximumButton = new Button("Добавить е/макс");
    addIfMinimumButton = new Button("Добавить е/мин");
    deleteFirstButton = new Button("Удалить первую");
    deleteLastButton = new Button("Удалить последнюю");
    deleteGreaterButton = new Button("Удалить бóльшие");
    deleteLessButton = new Button("Удалить меньшие");

    filterButton.setOnMouseClicked(event -> {
      String name = bookNameTextField.getText();
      String author = bookAuthorTextField.getText();
      BookSize size = bookSizeChoiceBox.getValue();
      BookColor color = bookColorComboBox.getValue();
      BookGenre genre = bookGenreSpinner.getValue();

      String pagesStr = bookPagesAmountTextField.getText();
      int pages;
      try {
        pages = Integer.parseInt(pagesStr);
      } catch (NumberFormatException ex) {
        createDialogWindow("Ошибка",
            "Ошибка формата ввода. В поле \"Страницы\" нужно вводить натуральное число")
            .showAndWait();
        return;
      }

      if (pages <= 0) {
        createDialogWindow("Ошибка",
            "Количество страниц в книге должно быть натуральным числом")
            .showAndWait();
      }

      List<Book> filteredBooks = bookViewComponent.getCollection().getBooks().stream()
          .filter(book -> ((name == null) || name.isEmpty() || name.equals(book.getName())))
          .filter(book -> ((author == null) || author.isEmpty() || author.equals(book.getAuthor())))
          .filter(book -> ((size == null) ||
              ((size.getWidth() == book.getWidth()) && (size.getHeight() == book.getHeight()))))
          .filter(book -> ((color == null) || (color.getColor().equals(book.getColor()))))
          .filter(book -> ((genre == null) || (genre == book.getGenre())))
          .collect(Collectors.toList());

      bookViewComponent.setCollection(new Collection(new ArrayDeque<>(filteredBooks)));
      bookViewComponent.updateComponent();
    });

    clearFieldsButton.setOnMouseClicked(event -> {
      bookNameTextField.clear();
      bookAuthorTextField.clear();
//      bookSizeChoiceBox.setValue(BookSize.values()[0]);
//      bookColorComboBox.setValue(BookColor.values()[0]);
      bookGenreSpinner.getValueFactory().setValue(BookGenre.values()[0]);
      bookPagesAmountTextField.clear();
    });

    refreshButton.setOnMouseClicked(event -> {
      bookViewComponent.setCollection(api.getCollection());
      bookViewComponent.updateComponent();
    });

    addButton.setOnMouseClicked(event -> {
      Book book = parseBook();
      if (book != null) {
        api.getCollection().addBook(book);
        try {
          databaseClient.createItem ("BookCollection",String.valueOf (book.hashCode ()), book);
        } catch (SQLException e) {
          e.printStackTrace ( );
        }
      } else {
        createDialogWindow("Ошибка", "Заполните поля правильно!").showAndWait();
      }
    });

    deleteButton.setOnMouseClicked(event -> {
      String name = bookNameTextField.getText();
      String author = bookAuthorTextField.getText();
      BookSize size = bookSizeChoiceBox.getValue();
      BookColor color = bookColorComboBox.getValue();
      BookGenre genre = bookGenreSpinner.getValue();

      String pagesStr = bookPagesAmountTextField.getText();
      int pages;
      try {
        databaseClient.deleteItem ("BookCollection","book.hashcode()");
      } catch (SQLException e) {
        e.printStackTrace ( );
      }

      try {
        pages = Integer.parseInt(pagesStr);
      } catch (NumberFormatException ex) {
        createDialogWindow("Ошибка",
            "Ошибка формата ввода. В поле \"Страницы\" нужно вводить натуральное число")
            .showAndWait();
        return;
      }

      if (pages <= 0) {
        createDialogWindow("Ошибка",
            "Количество страниц в книге должно быть натуральным числом")
            .showAndWait();
      }

      List<Book> filteredBooks = bookViewComponent.getCollection().getBooks().stream()
          .filter(book -> ((name == null) || name.isEmpty() || name.equals(book.getName())))
          .filter(book -> ((author == null) || author.isEmpty() || author.equals(book.getAuthor())))
          .filter(book -> ((size == null) ||
              ((size.getWidth() == book.getWidth()) && (size.getHeight() == book.getHeight()))))
          .filter(book -> ((color == null) || (color.getColor().equals(book.getColor()))))
          .filter(book -> ((genre == null) || (genre == book.getGenre())))
          .collect(Collectors.toList());

      filteredBooks.forEach(book -> api.removeOne(book));
    });

    addIfMaximumButton.setOnMouseClicked(event -> pool.submit(() -> {
      Book book = parseBook();
      api.addIfMax(book);
    }));

    addIfMinimumButton.setOnMouseClicked(event -> pool.submit(() -> {
      Book book = parseBook();
      api.addIfMin(book);
    }));

    deleteFirstButton.setOnMouseClicked(event -> pool.submit(() -> api.removeFirst()));

    deleteLastButton.setOnMouseClicked(event -> pool.submit(() -> api.removeLast()));

    deleteGreaterButton.setOnMouseClicked(
        event -> pool.submit(() -> api.removeGreater(chosenBookComponent.getBook())));

    deleteLessButton.setOnMouseClicked(
        event -> pool.submit(() -> api.removeLower(chosenBookComponent.getBook())));

  }



  @Override
  public void start(Stage primaryStage) throws SQLException {
    stage = primaryStage;

    final VBox rootNode = new VBox(createLoginBox());
    VBox.setMargin(rootNode, DEFAULT_VIEW_MARGIN);

    // Отрисовываем
    primaryStage.setScene(new Scene(rootNode, INITIAL_WIDTH, INITIAL_HEIGHT));
    primaryStage.setTitle(AUTH_WINDOW_TITLE);
    primaryStage.setResizable(false);
    primaryStage.setFullScreen(false);
    primaryStage.centerOnScreen();
    primaryStage.show();

    if (checkLoginAndPassword()) {
      Scene collectionScene = createCollecitonControlsScene();

      primaryStage.setTitle(AUTH_WINDOW_TITLE);
      primaryStage.setScene(collectionScene);
      primaryStage.setResizable(false);
      primaryStage.setFullScreen(false);
      primaryStage.centerOnScreen();
      primaryStage.show();
    }
    if( !databaseClient.isTableExist ("BookCollection")
       ){
      databaseClient.createTable ("BookCollection",Book.class);
    }

  }

  private Book parseBook() {
    String name = bookNameTextField.getText();
    String author = bookAuthorTextField.getText();
    BookSize size = bookSizeChoiceBox.getValue();
    BookColor color = bookColorComboBox.getValue();
    BookGenre genre = bookGenreSpinner.getValue();
    String pagesStr = bookPagesAmountTextField.getText();
    ZonedDateTime creationTime = ZonedDateTime.now ();
    if (name == null || author == null || pagesStr == null
        || name.isEmpty() || author.isEmpty() || pagesStr.isEmpty()
        || size == null || color == null || genre == null || genre == BookGenre.NONE ) {
      return null;
    }

    int pages;
    try {
      pages = Integer.parseInt(pagesStr);
    } catch (NumberFormatException ex) {
      return null;
    }

    return new Book(name, author, pages, size, color, genre, creationTime);
  }

  private Scene createCollecitonControlsScene() {

    VBox mainBox = new VBox();

    MenuBar menuBar = createMenuBar();

    /* Upper box definition */
    HBox upperBox = new HBox();

    /*    Control buttons box definition */
    VBox controlButtonsBox = new VBox();
    controlButtonsBox.getChildren()
        .addAll(addButton, deleteButton, addIfMaximumButton, addIfMinimumButton, deleteFirstButton,
            deleteLastButton, deleteGreaterButton, deleteLessButton);

    VBox.setMargin(addButton, DEFAULT_MARGIN);
    VBox.setMargin(deleteButton, DEFAULT_MARGIN);
    VBox.setMargin(addIfMaximumButton, DEFAULT_MARGIN);
    VBox.setMargin(addIfMinimumButton, DEFAULT_MARGIN);
    VBox.setMargin(deleteFirstButton, DEFAULT_MARGIN);
    VBox.setMargin(deleteLastButton, DEFAULT_MARGIN);
    VBox.setMargin(deleteGreaterButton, DEFAULT_MARGIN);
    VBox.setMargin(deleteLessButton, DEFAULT_MARGIN);

    /*    Control fields vertical box definition */
    VBox controlFieldsVerticalBox = new VBox();
    controlFieldsVerticalBox.setAlignment(Pos.CENTER);

    /*    Filter and refresh buttons horizontal box definition */
    HBox filterNRefreshButtonBox = new HBox();
    filterNRefreshButtonBox.getChildren().addAll(filterButton, clearFieldsButton, refreshButton);

    HBox.setMargin(filterButton, DEFAULT_MARGIN);
    HBox.setMargin(clearFieldsButton, DEFAULT_MARGIN);
    HBox.setMargin(refreshButton, DEFAULT_MARGIN);

    Label fieldsText = new Label("Выбор книги");
    GridPane controlFieldsPane = createCriteriasGrid();
    controlFieldsVerticalBox.getChildren()
        .addAll(fieldsText, controlFieldsPane, filterNRefreshButtonBox);

    upperBox.getChildren().addAll(controlButtonsBox, controlFieldsVerticalBox);

    /* Below pane definition */
//    Stream.generate(CollectionUtils::generateRandomBook).limit(20).forEach(book -> api.add(book));
    bookViewComponent = new BookViewComponent(
        api.getCollection(),
        4,
        (component) -> {
          if (chosenBookComponent != null) {
            component.setBorder(Border.EMPTY);
            /*component.setBorder(new Border(new BorderStroke(
                javafx.scene.paint.Color.rgb(
                    component.getBook().getColor().getRed(),
                    component.getBook().getColor().getGreen(),
                    component.getBook().getColor().getBlue(), 1
                ), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));*/
//            chosenBookComponent.setColor(chosenBookComponent.getBook().getColor());
          }

          chosenBookComponent = component;
          component
              .setBorder(new Border(new BorderStroke(javafx.scene.paint.Color.rgb(102, 102, 255, 1),
                  BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5))));
//          chosenBookComponent.setColor(new Color(102, 102, 255));
        });
    bookViewComponent.updateComponent();

    mainBox.getChildren().addAll(menuBar, upperBox, bookViewComponent);

    VBox.setMargin(upperBox, DEFAULT_VIEW_MARGIN);
    VBox.setMargin(bookViewComponent, DEFAULT_VIEW_MARGIN);

    return new Scene(mainBox);
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
      fileChooser.setTitle("Save collection file");

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
      /*FileChooser chooser = new FileChooser();
      chooser.setTitle("Load collection File");

      File file = fileChooser.showOpenDialog(stage);

      if (file != null) {
        api.load(file.getAbsolutePath());
      }*/

      api.load(null);
    }));

    exitOption.setOnAction((event) -> Platform.exit());

    return menuBar;
  }

  private GridPane createCriteriasGrid() {
    GridPane grid = new GridPane();
//    grid.setMinSize(400, 200);
    grid.setPadding(new Insets(10, 10, 10, 10));
    grid.setVgap(5);
    grid.setHgap(5);
    grid.setAlignment(Pos.CENTER);

    grid.add(new Label("Название"), 0, 0);
    grid.add(bookNameTextField, 1, 0);
    grid.add(new Label("Автор"), 0, 1);
    grid.add(bookAuthorTextField, 1, 1);
    grid.add(new Label("Цвет"), 0, 2);
    grid.add(bookColorComboBox, 1, 2);
    grid.add(new Label("Жанр"), 0, 3);
    grid.add(bookGenreSpinner, 1, 3);
    grid.add(new Label("Размер"), 0, 4);
    grid.add(bookSizeChoiceBox, 1, 4);
    grid.add(new Label("Страницы"), 0, 5);
    grid.add(bookPagesAmountTextField, 1, 5);

    return grid;
  }

  private VBox createLoginBox() {
    final VBox loginBox = new VBox();
    loginBox.getChildren().addAll(usernameInput, userPasswordInput, loginButton);

    VBox.setMargin(usernameInput, new Insets(70, 30, 0, 30));
    VBox.setMargin(userPasswordInput, new Insets(10, 30, 0, 30));
    VBox.setMargin(loginButton, BUTTON_MARGIN);

    loginButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
      if (checkLoginAndPassword()) {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        Scene scene = createCollecitonControlsScene();

        stage.setScene(scene);
        stage.show();
      }
    });

    return loginBox;
  }

  public boolean checkLoginAndPassword() {
    String user = usernameInput.getText();
    String password = userPasswordInput.getText();

    boolean loginSuccess = false;
    switch (user) {
      case "Sofya":
        loginSuccess = (password.equals("12345"));
        break;

      case "Misha":
        loginSuccess = (password.equals("2281488"));
        break;

      case "Polina":
        loginSuccess = (password.equals("87TY56"));
        break;

      case "Kristina":
        loginSuccess = (password.equals("34456TyUi"));
        break;

      case "Pavel":
        loginSuccess = (password.equals("228AWQ"));
        break;
    }

    return loginSuccess;
  }
}
