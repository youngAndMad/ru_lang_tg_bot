package danekerscode.ru_lang_tg_bot.tg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static danekerscode.ru_lang_tg_bot.tg.TgBotConstants.GREETING;

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
            if (text.equals("/start")) {
                greeting(update.getMessage());
            }
        }
    }

    private void greeting(Message message) {
        String chatId = String.valueOf(message.getChatId());
        var sendMessage = new SendMessage(
                chatId,
                GREETING.formatted(message.getFrom().getUserName())
        );

        var replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setKeyboard(List.of(
                new KeyboardRow(List.of(new KeyboardButton("Погнали!"))),
                new KeyboardRow(List.of(new KeyboardButton("Не хочу")))
        ));

        replyKeyboardMarkup.setResizeKeyboard(true);

        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        try {
            executeAsync(sendMessage).thenRun(() -> log.info("Greeting message sent: chat.id = {}", chatId));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

}
