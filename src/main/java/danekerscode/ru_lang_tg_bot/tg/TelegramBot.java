package danekerscode.ru_lang_tg_bot.tg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static danekerscode.ru_lang_tg_bot.tg.TgBotConstants.*;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String username;

    @Value("${telegram.bot.token}")
    private String token;

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    private final ConcurrentHashMap<Long, Long> userQuizState = new ConcurrentHashMap<>();

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasCallbackQuery()) {
            var message = update.getCallbackQuery().getMessage();
            var chatId = message.getChatId();
            var text = update.getCallbackQuery().getMessage().getText();

            boolean isAnswer = QUIZ_QUESTIONS.values().stream()
                    .map(Question::value)
                    .anyMatch(answer -> answer.equals(text));

            if (isAnswer) {
                processQuiz(message);
            }

        } else if (update.hasMessage() && update.getMessage().hasText()) {
            var message = update.getMessage();
            var text = message.getText();

            boolean isAnswer = QUIZ_QUESTIONS.values().stream()
                    .flatMap(question -> question.answers().stream())
                    .anyMatch(answer -> answer.equals(text));

            if (isAnswer) {
                processQuiz(message);
            }

            switch (text) {
                case START -> greeting(message);
                case LETS_STUDY -> askForOratorSkillLevel(message);
                case NO_THANKS -> noThanks(message);
                case ADVANCED_ORATOR_SKILL_LEVEL,
                        BEGIN_ORATOR_SKILL_LEVEL,
                        MIDDLE_ORATOR_SKILL_LEVEL -> processOratorSkillLevelAnswer(message);
                case START_QUIZ -> processQuiz(message);
                case HOW_TO_IMPROVE_ORATOR_SKILL -> {
                    var sendMessage = new SendMessage(String.valueOf(message.getChatId()), "Ты выбрал улучшение ораторских навыков");
                    processMessageSending(sendMessage);
                }
            }
        }
    }

    private void greeting(Message message) {
        String chatId = String.valueOf(message.getChatId());
        var sendMessage = new SendMessage(chatId, GREETING.formatted(message.getFrom().getUserName()));

        var replyMarkup = createReplyKeyboardMarkup(LETS_STUDY, NO_THANKS);
        sendMessage.setReplyMarkup(replyMarkup);

        processMessageSending(sendMessage);
        log.info("Greeting message sent: chat.id = {}", chatId);
    }

    private void askForOratorSkillLevel(Message message) {
        String chatId = String.valueOf(message.getChatId());
        var sendMessage = new SendMessage(chatId, ORATOR_SKILL_LEVEL);
        var replyMarkup = createReplyKeyboardMarkup(BEGIN_ORATOR_SKILL_LEVEL, MIDDLE_ORATOR_SKILL_LEVEL, ADVANCED_ORATOR_SKILL_LEVEL);
        sendMessage.setReplyMarkup(replyMarkup);

        processMessageSending(sendMessage);
        log.info("Asking for orator skill level: chat.id = {}", chatId);
    }

    private void noThanks(Message message) {
        String chatId = String.valueOf(message.getChatId());
        var sendMessage = new SendMessage(chatId, NO_THANKS_TEXT);
        processMessageSending(sendMessage);
        log.info("No thanks message sent: chat.id = {}", chatId);
    }

    private void processOratorSkillLevelAnswer(Message message) {
        var chatId = String.valueOf(message.getChatId());
        var sendMessage = new SendMessage(chatId, "Ты выбрал уровень: " + message.getText() + " . Теперь давай это проверим! Сейчас ты пройдешь тест для этого. ");
        var replyMarkup = createReplyKeyboardMarkup(START_QUIZ);
        sendMessage.setReplyMarkup(replyMarkup);
        processMessageSending(sendMessage);
    }

    private void processQuiz(Message message) {
        var chatId = String.valueOf(message.getChatId());

        var currentUserState = userQuizState.getOrDefault(message.getChatId(), 1L);

        if (currentUserState != 1) {
            var sendMessage = new SendMessage(chatId, "Ответ принят");
            processMessageSending(sendMessage);
        }

        if (currentUserState == 8){
            var sendMessage = new SendMessage(chatId, "Тест завершен. Твой уровень примерно как ты предполагал");
            processMessageSending(sendMessage);
            sendMessage.setText("Если ты хочешь получить советы выберу по какому поводу ты хочешь получить советы");
            var replyMarkup = createReplyKeyboardMarkup(HOW_TO_IMPROVE_ORATOR_SKILL, HOW_TO_IMPROVE_PUBLIC_SPEECH);
            sendMessage.setReplyMarkup(replyMarkup);
            processMessageSending(sendMessage);
            userQuizState.remove(message.getChatId());
        }else {

            var question = QUIZ_QUESTIONS.get(currentUserState);
            var sendMessage = new SendMessage(chatId, question.value());

            var replyMarkup = createInlineKeyboardMarkup(question.answers().toArray(new String[0]));
            sendMessage.setReplyMarkup(replyMarkup);
            processMessageSending(sendMessage);

            userQuizState.put(message.getChatId(), currentUserState + 1);
        }
    }

    private void processMessageSending(BotApiMethod<Message> message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private ReplyKeyboardMarkup createReplyKeyboardMarkup(String... buttons) {
        var replyMarkup = new ReplyKeyboardMarkup();

        replyMarkup.setResizeKeyboard(true);
        replyMarkup.setOneTimeKeyboard(true);
        replyMarkup.setSelective(true);


        replyMarkup.setKeyboard(List.of(new KeyboardRow
                (List.of(
                        Arrays.stream(buttons).map(
                                KeyboardButton::new
                        ).toArray(KeyboardButton[]::new)
                )))
        );

        return replyMarkup;
    }

    private InlineKeyboardMarkup createInlineKeyboardMarkup(
            String... buttons
    ) {
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();

        var keyboard = Arrays.stream(buttons)
                .map(btn -> {
                    var inlineKeyboardButton = new InlineKeyboardButton();
                    inlineKeyboardButton.setText(System.lineSeparator().concat(btn));
                    inlineKeyboardButton.setCallbackData("btn_answer_to_question");
                    return inlineKeyboardButton;
                })
                .map(Collections::singletonList)
                .collect(Collectors.toList());

        inlineKeyboardMarkup.setKeyboard(keyboard);

        return inlineKeyboardMarkup;
    }

}
