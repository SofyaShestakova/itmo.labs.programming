package ru.shestakova.model;

import java.util.ListResourceBundle;

public class SyntaxBundle_sp extends ListResourceBundle {
    private String[][] contents = {
            {"Язык","idioma"},
            {"Русский","Ruso"},
            {"Турецкий","Turco"},
            {"Литовский","Lituano"},
            {"Испанский","español"},
            {"клиент", "cliente"},
            {"Лабораторная №8 (клиент)", "laboratorio №8 (cliente)"},
            {"Название","el nombre"},
            {"Автор","el autor"},
            {"Информация_о_новом_элементе","Nueva información del artículo"},
            {"Красненький","Poco rojo"},
            {"Зеленый","Verde"},
            {"Синий","Azul"},
            {"Желтый","Amarillo"},
            {"Розовый","Rosa"},
            {"Магический","mágico"},
            {"Загадочный","misterioso"},
            {"Научная фантастика","ciencia ficcion"},
            {"Путешествие","el viaje"},
            {"Хоррор","horror"},
            {"Генерировать значение:","generar valor:"},
            {"Генерировать","generar"},
            {"Обновить","para actualizar"},
            {"Начать анимацию","comienza la animación"},
            {"Остановить анимацию","detener la animación"},
            {"Цвет", "colorear"},
            {"Жанр", "género"},
            {"Дата создания:","fecha de creación"}

    };

    public String[][] getContents(){
        return contents;
    }
}
