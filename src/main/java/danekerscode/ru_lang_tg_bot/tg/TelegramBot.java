package danekerscode.ru_lang_tg_bot.tg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;

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

    @Override
    public void onUpdateReceived(Update update) {
        var chatId = update.getMessage().getChatId();
        if (update.hasMessage() && update.getMessage().hasText()) {
            var text = update.getMessage().getText();
            log.info("Received message: chat.id = {}, text = {}", chatId, text);
            switch (text) {
                case START -> greeting(update.getMessage());
                case LETS_STUDY -> askForOratorSkillLevel(update.getMessage());
                case NO_THANKS -> noThanks(update.getMessage());
                case ADVANCED_ORATOR_SKILL_LEVEL,
                        BEGIN_ORATOR_SKILL_LEVEL,
                        MIDDLE_ORATOR_SKILL_LEVEL -> processOratorSkillLevelAnswer(update.getMessage());
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
        var skillLevel = message.getText();

        var sendMessage = new SendMessage(chatId, "Ты выбрал уровень: " + skillLevel + " . Теперь давай это проверим! Сейчас ты пройдешь тест для этого");
        processMessageSending(sendMessage);
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
}
