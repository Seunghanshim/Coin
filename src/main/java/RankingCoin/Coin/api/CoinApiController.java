package RankingCoin.Coin.api;

import RankingCoin.Coin.domain.Category;
import RankingCoin.Coin.domain.Coin;
import RankingCoin.Coin.domain.Event;
import RankingCoin.Coin.repository.CoinRepository;
import RankingCoin.Coin.service.CoinService;
import RankingCoin.Coin.service.EventService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CoinApiController {
    private final CoinService coinService;
    private final EventService eventService;

    @GetMapping("/api/coin")
    public List<CoinDto> Coins(){
        List<Coin> findCoins = coinService.findCoins();

        List<CoinDto> collect = findCoins.stream()
                .map(m -> new CoinDto(m.getId(), m.getMarket(), m.getKor(), m.getKrw(), m.getVol()))
                .collect(Collectors.toList());

        return collect;
    }

    @GetMapping("/api/coin/{id}/event")
    public List<EventDto> CoinsEvents(@PathVariable("id") Long id){
        List<Event> list = eventService.findByCoin(id);

        List<EventDto> collect = list.stream()
                .map(m -> new EventDto(m.getWhat(), m.getCategory(), m.getWhen()))
                .collect(Collectors.toList());

        return collect;
    }

    @Data
    @AllArgsConstructor
    class CoinDto {
        private Long id;
        private String market;
        private String kor;
        private int krw;
        private int vol;
    }

    @Data
    @AllArgsConstructor
    class EventDto {
        private String event;
        private Category category;
        private LocalDateTime whenDate;
    }
}
