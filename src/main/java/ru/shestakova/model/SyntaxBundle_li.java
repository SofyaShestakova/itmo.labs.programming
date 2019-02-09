package ru.shestakova.model;

import java.util.ListResourceBundle;

public class SyntaxBundle_li extends ListResourceBundle {
    private String[][] contents = {
            {"Язык","Kalba"},
            {"Русский","Rusų"},
            {"Турецкий","Turkų"},
            {"Литовский","Lietuvių"},
            {"Испанский","ispanų"},
            {"клиент","klientas"},
            {"Лабораторная","laboratorija"},
            {"Название","pavadinimą"},
            {"Автор","autorius"},
            {"Информация о новом элементе","nauja elemento informacija"},
            {"Красненький","mažai raudona"},
            {"Зеленый","žalia"},
            {"Синий","mėlyna"},
            {"Желтый","geltona"},
            {"Розовый","rožinė"},
            {"Магический", "stebuklinga"},
            {"Загадочный","paslaptingas"},
            {"Научная фантастика","mokslinė fantastika"},
            {"Путешествие","Kelionės"},
            {"Хоррор","siaubas"},
            {"Генерировать значение:","generuoti vertę:"},
            {"Генерировать","generuoti"},
            {"Обновить","atnaujinti"},
            {"Начать анимацию","pradėti animaciją"},
            {"Остановить анимацию","sustabdyti animaciją"}

    };

    public String[][] getContents(){
        return contents;
    }

}
