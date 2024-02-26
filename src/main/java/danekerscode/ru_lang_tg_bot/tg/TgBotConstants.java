package danekerscode.ru_lang_tg_bot.tg;

import lombok.experimental.UtilityClass;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public static final String START_QUIZ = "Начать тест";

    public static final Map<Long, Question> QUIZ_QUESTIONS = new LinkedHashMap<>() {{
        put(1L, new Question(
                "1. Как вы оцениваете свою уверенность во время публичных выступлений?",
                List.of("а) Обсолютно уверенно и комфортно",
                        "б) Иногда нервничаю, но обычно справляюсь с этим.",
                        "в) Я всегда волнуюсь и испытываю стресс."))
        );

        put(2L, new Question(
                "2. Как вы подготавливаетесь к публичному выступлению?",
                List.of("а) Тщательно планирую и готовлюсь заранее.",
                        "б) Провожу некоторую подготовку.",
                        "в) Обычно не готовлюсь заранее."))
        );

        put(3L, new Question(
                "3. Как вы реагируете на вопросы из зала во время выступления?",
                List.of("а) Уверенно и готово отвечаю на любые вопросы.",
                        "б) Иногда затрудняюсь с ответом.",
                        "в) Чувствую себя неуютно."))
        );


        put(4L, new Question(
                "4. Как вы оцениваете свои навыки организации мыслей во время выступления?",
                List.of("а) Могу четко структурировать свои мысли и передавать их аудитории.",
                        "б) Иногда теряю нить рассказа.",
                        "в) Часто теряюсь в рассказе."))
        );

        put(5L, new Question(
                "5. Как вы используете невербальные средства коммуникации во время выступления?",
                List.of("а) Активно использую жесты, мимику и интонацию",
                        "б) Использую некоторые невербальные средства.",
                        "в) Не обращаю внимания."))
        );

        put(6L, new Question(
                "6. Как вы оцениваете свою способность к адаптации к аудитории во время выступления?",
                List.of("а) Легко адаптируюсь к разным типам аудитории",
                        "б) Иногда затрудняюсь с адаптацией,.",
                        "в) Имею трудности с адаптацией к аудитории."))
        );

        put(7L, new Question(
                "7. Как вы оцениваете свою способность завершать выступление?",
                List.of("а) Могу завершить выступление с ярким и запоминающимся заключением.",
                        "б) Иногда затрудняюсь с завершением.",
                        "в) Часто теряю нить рассказа.")));

    }};

    public static final String HOW_TO_IMPROVE_ORATOR_SKILL = "Как улучшить ораторские навыки";
    public static final String HOW_TO_IMPROVE_PUBLIC_SPEECH = "Как улучшить публичные выступления";

    public static final List<String> HOW_TO_IMPROVE_ORATOR_SKILL_ADVICES = List
            .of(
                    "Стараться больше читать вслух с выражением",
                    "Увеличивать словарный запас",
                    "Очистить речь от слов-паразитов",
                    "Работать над дикцией, интонацией, тембром и громкостью голоса",
                    "Слушать выступления хороших ораторов",
                    "Много тренироваться",
                    "Анализировать свои ошибки и исправлять их"
            );

    public static final List<String> HOW_TO_IMPROVE_PUBLIC_SPEECH_ADVICES = List.of(
            "Используй разные варианты подачи информации",
            "Используй визуальные материалы",
            "Аудитория — твой лучший помощник. Уважай и люби её.",
            "Всегда готовь сценарий выступления"
    );
}

