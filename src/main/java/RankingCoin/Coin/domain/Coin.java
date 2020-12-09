package RankingCoin.Coin.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coin implements Comparable<Coin>{
    @Id @Column(name = "coin_id")
    @GeneratedValue
    private Long id;

    private String market;
    private String eng;
    private String kor;
    private int vol;
    private int krw;
    private LocalDateTime updateTime;
    private int op;
    private boolean hasEvent;

    public static Coin CreatCoin(String market, String eng, String kor){
        Coin coin = new Coin();
        coin.market = market;
        coin.eng = eng;
        coin.kor = kor;

        return coin;
    }

    public void updatePrice(int krw, LocalDateTime updateTime){
        this.krw = krw;
        this.updateTime = updateTime;
    }

    public void updateVolume(int vol){
        this.vol = vol;
    }

    public void updateOP(int op){
        this.op = op;
    }

    public void updateHasEvent(boolean hasEvent){
        this.hasEvent = hasEvent;
    }

    @Override
    public int compareTo(Coin coin){
        int targetVol = coin.getVol();
        if(this.vol == targetVol) return 0;
        else if(this.vol > targetVol) return 1;
        else return -1;
    }
}
