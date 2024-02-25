package danekerscode.ru_lang_tg_bot.config;

import danekerscode.ru_lang_tg_bot.tg.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BotInitializer {

    private final TelegramBot telegramBot;

    @Bean
    TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        var api = new TelegramBotsApi(DefaultBotSession.class);
        var botSession = api.registerBot(telegramBot);
        log.info("BotSession: running = {}", botSession.isRunning());
        return api;
    }

}