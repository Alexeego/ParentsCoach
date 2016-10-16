package com.example.alexey.parentscoach.utils;

/**
 * Created by Alexey on 16.10.2016.
 */
public class WordUtil {
    public static void wordEdit(StringBuilder sb, int count, String word, String many, String one, String second) // Функция для написания правильного окончания в слове в зависимости от их числа
    {
        sb.append(count).append(" ").append(word);
        if(count % 10 == 0 || count % 10 >4 || count % 100 > 10 && count % 100 < 20){
            sb.append(many);
        } else if(count % 10 == 1) {
            sb.append(one);
        } else {
            sb.append(second);
        }
    }
}
