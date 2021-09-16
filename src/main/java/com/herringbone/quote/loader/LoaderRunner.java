package com.herringbone.quote.loader;

import com.herringbone.quote.loader.domain.Ticker;
import com.herringbone.quote.loader.repository.TickerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
@Slf4j
public class LoaderRunner implements CommandLineRunner {
    @Value("${yahoo.index.symbol}")
    private String yahooIndexAlias;

    @Value("${yahoo.index.full.symbol}")
    private String yahooIndexFullSymbol;

    private final TickerRepository tickerRepository;

    public LoaderRunner(TickerRepository tickerRepository) {
        this.tickerRepository = tickerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            Ticker exampleTicker = Ticker.builder().symbol(yahooIndexFullSymbol).alias(yahooIndexAlias).build();
            Ticker ticker = tickerRepository.findAll(Example.of(exampleTicker)).get(0);
//            Ticker ticker = tickerRepository.findBySymbolOrAlias(yahooIndexFullSymbol, yahooIndexAlias).orElseThrow(() -> new Exception("unable to find ticker"));
            log.info("ticker {} {} exists", ticker.getSymbol(), ticker.getAlias());
        } catch (Exception e) {
            log.info("Ticker with symbol {} and alias {} does not exist saving", yahooIndexFullSymbol, yahooIndexAlias);
            ZonedDateTime zonedDateTime = ZonedDateTime.of(1950, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
            Ticker ticker = Ticker.builder().symbol(yahooIndexFullSymbol).alias(yahooIndexAlias).exchange("index").startdate(zonedDateTime)
                    .lastUpdate(ZonedDateTime.now())
                    .build();
            tickerRepository.save(ticker);
        }
    }
}
