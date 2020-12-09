package RankingCoin.Coin.webController.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CoinDTO {
    private Long id;
    private String market;
    private String eng;
    private String kor;
    private int vol;
    private int krw;
    private LocalDateTime updateTime;
    private int op;
    private boolean hasEvent;
    private int att;
}
