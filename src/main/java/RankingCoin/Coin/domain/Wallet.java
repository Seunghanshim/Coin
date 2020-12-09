package RankingCoin.Coin.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wallet implements Comparable<Wallet>{

    @Id @GeneratedValue
    private Long id;

    private int coinNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coin_id")
    private Coin coin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Wallet createWallet(User user, Coin coin, int Num){
        Wallet wallet = new Wallet();
        wallet.user = user;
        wallet.coin = coin;
        wallet.coinNum = Num;

        return wallet;
    }

    public void updateCoinNum(int coinNum){
        this.coinNum = coinNum;
    }

    @Override
    public int compareTo(Wallet o) {
        int targetKrw = o.getCoin().getKrw();
        if (this.getCoin().getKrw() == targetKrw) return 0;
        else if (this.getCoin().getKrw() > targetKrw) return 1;
        else return -1;
    }
}
