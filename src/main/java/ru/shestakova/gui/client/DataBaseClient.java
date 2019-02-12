package ru.shestakova.gui.client;

import ru.shestakova.model.BookGenre;
import ru.shestakova.model.SqlHelper;

import java.awt.*;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static ru.shestakova.model.BookGenre.getOrdinal;


public class DataBaseClient<T> {
    private final static String HOST = "localhost";
    private final static String ENDPOINT = "jdbc:postgresql://" + HOST + ":5432/lab8";
    private final static String DRIVER = "org.postgresql.Driver";
    private final static String USER = "postgres"; //введи свои логин и пароль от гелиоса
    private final static String PASSWORD = "";
    private Connection connection = null;
    private static DataBaseClient instance = null;

    public static DataBaseClient getInstance() {
        return instance == null ? instance = new DataBaseClient ( ) : instance;
    }

    private DataBaseClient() {
        try {
            SqlHelper.registration (DRIVER);
            connection = SqlHelper.connection (ENDPOINT, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace ( );
        }
    }

    public boolean isTableExist(String tableName) {
        String query = format ("SELECT * FROM %s LIMIT 1",
                normalizeTableName (tableName));

        try (Statement statement = connection.createStatement ( )) {
            statement.executeQuery (query);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public int createTable(String tableName, Class itemClass) throws SQLException {
        String query = format ("CREATE TABLE %s (key VARCHAR(4096) PRIMARY KEY, %s);",
                normalizeTableName (tableName),
                Arrays.stream (itemClass.getDeclaredFields ( ))
                        .map (field -> {
                            String type = "VARCHAR(4096)";
                            if (field.getType ( ) == Double.class) {
                                type = "double precision";
                            } else if (field.getType ( ) == Integer.class) {
                                type = "integer";
                            } else if (field.getType ( ) == Boolean.class) {
                                type = "boolean";
                            } else if (field.getType ( ) == ZonedDateTime.class) {
                                type = "TIMESTAMP";
                            } else if (field.getType ( ) == Color.class) {
                                type = "integer";
                            } else if (field.getType ( ) == String.class) {
                                type = "VARCHAR(4096)";
                            } else if (field.getType ( ) == BookGenre.class) {
                                type = "integer";
                            }

                            return String.format ("%s %s", field.getName ( ), type);
                        })
                        .collect (Collectors.joining (", "))
        );
        try (Statement statement = connection.createStatement ( )) {
            return statement.executeUpdate (query);
        }
    }

    public int dropTable(String tableName) throws SQLException {
        String query = format ("DROP TABLE %s CASCADE CONSTRAINTS", normalizeTableName (tableName));
        try (Statement statement = connection.createStatement ( )) {
            return statement.executeUpdate (query);
        }
    }

    public int createItem(String tableName, String newKey, Object newItem) throws SQLException {
        Class itemClass = newItem.getClass ( );
        String query = format ("INSERT INTO %s (key, %s) VALUES ('%s', %s)",
                normalizeTableName (tableName),
                getFieldsNames (itemClass),
                newKey,
                getFieldsValues (newItem)
        );
        try (Statement statement = connection.createStatement ( )) {
            return statement.executeUpdate (query);
        }

    }

    public Hashtable<String, T> readAllItems(String tableName, Class itemClass) throws SQLException {
        String query = format ("SELECT key, %s FROM %s",
                getFieldsNames (itemClass),
                normalizeTableName (tableName));
        Hashtable<String, T> hashtable = new Hashtable<> ( );
        try (Statement statement = connection.createStatement ( )) {
            ResultSet resultSet = statement.executeQuery (query);
            while (resultSet.next ( )) {
                String key = resultSet.getString (1);

                hashtable.put (key, (T) getPopulatedItem (itemClass, resultSet));
            }

        }

        return hashtable;
    }

    public T readItem(String tableName, String key, Class itemClass) throws SQLException {
        String query = format ("SELECT key, %s FROM %s WHERE key='%s'",
                getFieldsNames (itemClass),
                normalizeTableName (tableName),
                key);
        try (Statement statement = connection.createStatement ( )) {
            ResultSet resultSet = statement.executeQuery (query);

            if (resultSet.next ( )) {
                return (T) getPopulatedItem (itemClass, resultSet);
            }
        }

        return null;
    }

    public int updateItem(String tableName, String key, Object item) throws SQLException {
        Class itemClass = item.getClass ( );
        String query = format ("UPDATE %s SET %s WHERE key='%s'",
                normalizeTableName (tableName),
                Arrays.stream (itemClass.getDeclaredFields ( ))
                        .map (field -> field.getName ( ) + "=" + normalizeFieldValue (field, item))
                        .collect (Collectors.joining (", ")),
                key
        );
        try (Statement statement = connection.createStatement ( )) {
            return statement.executeUpdate (query);
        }
    }

    public int deleteAllItems(String tableName) throws SQLException {
        String query = format ("TRUNCATE TABLE %s",
                normalizeTableName (tableName));
        try (Statement statement = connection.createStatement ( )) {
            return statement.executeUpdate (query);
        }
    }

    public int deleteItem(String tableName, String key) throws SQLException {
        String query = format ("DELETE FROM %s WHERE key='%s'",
                normalizeTableName (tableName),
                key);
        try (Statement statement = connection.createStatement ( )) {
            return statement.executeUpdate (query);
        }
    }

    private static Object getPopulatedItem(Class itemClass, ResultSet resultSet) {
        try {
            Object item = itemClass.newInstance ( );
            Arrays
                    .stream (itemClass.getDeclaredFields ( ))
                    .forEach (field -> {
                        try {
                            field.setAccessible (true);
                            if (field.getType ( ) == Double.class) {
                                field.set (item, resultSet.getDouble (field.getName ( )));
                            } else if (field.getType ( ) == Integer.class) {
                                field.set (item, resultSet.getInt (field.getName ( )));
                            } else if (field.getType ( ) == Boolean.class) {
                                field.set (item, resultSet.getBoolean (field.getName ( )));
                            } else if (field.getType ( ) == ZonedDateTime.class) {
                                field.set (item, resultSet.getTimestamp (field.getName ( )).toLocalDateTime ( ));
                            } else if (field.getType ( ) == Color.class) {
                                field.set (item, new Color (resultSet.getInt (field.getName ( ))));
                            }
                            else if (field.getType ( ) == BookGenre.class) {
                                field.set (item, resultSet.getInt(field.getName ()));
                            }
                            //else if (field.getType() == BookColor.class) {
                            // field.set(item, new BookColor(resultSet.getString(field.getName())));}
                            else if (field.getType ( ) == String.class) {
                                field.set (item, resultSet.getString (field.getName ( )));
                            }
                            field.setAccessible (false);
                        } catch (IllegalAccessException | SQLException e) {
                            e.printStackTrace ( );
                        }
                    });
            return item;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace ( );
        }
        return null;
    }

    private static String normalizeTableName(String tableName) {
        return tableName.replaceAll ("[.]", "_");
    }

    private static String getFieldsNames(Class itemClass) {
        return Arrays
                .stream (itemClass.getDeclaredFields ( ))
                .map (Field::getName)
                .collect (Collectors.joining (", "));
    }

    private static String getFieldsValues(Object item) {
        return Arrays
                .stream (item.getClass ( ).getDeclaredFields ( ))
                .map (field -> normalizeFieldValue (field, item))
                .collect (Collectors.joining (", "));
    }

    private static String normalizeFieldValue(Field field, Object item) {
        String value = "NULL";
        try {
            field.setAccessible (true);
            if (field.getType ( ) == Double.class
                    || field.getType ( ) == Integer.class
                    || field.getType ( ) == Boolean.class) {
                value = field.get (item).toString ( );
            } else if (field.getType ( ) == String.class) {
                value = "'" + field.get (item).toString ( ) + "'";
            } else if (field.getType ( ) == BookGenre.class) {
                value = String.valueOf (getOrdinal ( ));
            } else if (field.getType ( ) == ZonedDateTime.class) {
                ZonedDateTime date = (ZonedDateTime) field.get (item);
                value = "to_timestamp('" + date.format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss") )+ "', 'yyyy-mm-dd hh24:mi:ss')";
            } else if (field.getType ( ) == Color.class) {
                Color color = (Color) field.get (item);
                value = String.valueOf (color.getRGB ( ));
            }
            field.setAccessible (false);
        } catch (IllegalAccessException e) {
            return "NULL";
        } catch (NullPointerException e) {
            return "NULL";
        }

        return value;
    }
}
