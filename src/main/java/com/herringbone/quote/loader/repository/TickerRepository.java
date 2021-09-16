package com.herringbone.quote.loader.repository;

import com.herringbone.quote.loader.domain.Ticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TickerRepository extends JpaRepository<Ticker, Long> {

    Optional<Ticker> findBySymbolOrAlias(@Param("symbol") String symbol,
                                         @Param("alias") String alias);
}
