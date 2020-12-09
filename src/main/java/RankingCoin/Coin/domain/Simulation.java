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
public class Simulation{

    @Id @GeneratedValue
    private Long id;

    private LocalDateTime when;

    private int coinNum;

    private int coinPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coin_id")
    private Coin coin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Simulation createSimulation(User user, Coin coin, int Num){
        Simulation simulation = new Simulation();

        simulation.user = user;
        simulation.coin = coin;
        simulation.coinNum = Num;
        simulation.coinPrice = coin.getKrw();
        simulation.when = LocalDateTime.now();

        return simulation;
    }
}
