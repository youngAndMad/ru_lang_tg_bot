package danekerscode.ru_lang_tg_bot.tg;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TgBotConstants {

    public static final String GREETING = """ 
            📝 Привет %s!
            Я бот для улучшения ораторского и публичного выступления.
            Если хочешь начать обучения нажми 'Погнали'!
            """;
    public static final String NO_THANKS_TEXT = "Жаль, что ты не хочешь улучшить свои навыки. Но если передумаешь, я всегда тут!";

    public static final String START = "/start";

    public static final String LETS_STUDY = "Погнали!";
    public static final String NO_THANKS = "Не хочу";

    public static final String ORATOR_SKILL_LEVEL = "На каком уровне ты сейчас?";
    public static final String BEGIN_ORATOR_SKILL_LEVEL = "Новичок";
    public static final String MIDDLE_ORATOR_SKILL_LEVEL = "Средний";
    public static final String ADVANCED_ORATOR_SKILL_LEVEL = "Продвинутый";
}
