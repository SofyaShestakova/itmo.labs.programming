package ru.shestakova.model;

import java.util.ListResourceBundle;

public class SyntaxBundle_tu extends ListResourceBundle {
    private String[][] contents = {
            {"Язык","Dil"},
            {"Русский","Rus"},
            {"Турецкий","Türk"},
            {"Литовский","Litvanyalı"},
            {"Испанский","İspanyolca"},
            {"клиент","müşteri"},
            {"Лабораторная","laboratuvar"},
            {"Название","isim"},
            {"Автор","yazar"},
            {"Информация о новом элементе","yeni ürün bilgisi"},
            {"Красненький","küçük kırmızı"},
            {"Зеленый","yeşil"},
            {"Синий","mavi"},
            {"Желтый","sarı"},
            {"Розовый","pembe"},
            {"Магический", "sihirli"},
            {"Загадочный","esrarengiz"},
            {"Научная фантастика","bilim kurgu"},
            {"Путешествие","yolculuk"},
            {"Хоррор","korku"},
            {"Генерировать значение:","değer üretmek:"},
            {"Генерировать","üretmek"},
            {"Обновить","güncellemek"},
            {"Начать анимацию","animasyonu başlat"},
            {"Остановить анимацию","animasyonu durdur"}

    };

    public String[][] getContents(){
        return contents;
    }
}
