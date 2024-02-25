package danekerscode.ru_lang_tg_bot.tg;

import java.util.List;

public record Question (
        String value,
        List<String> answers
){
}
