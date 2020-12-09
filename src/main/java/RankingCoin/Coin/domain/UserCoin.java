package RankingCoin.Coin.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoin implements Comparable<UserCoin>{

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coin_id")
    private Coin coin;

    public static UserCoin create(User user, Coin coin){
        UserCoin userCoin = new UserCoin();
        userCoin.user = user;
        userCoin.coin = coin;

        return userCoin;
    }

    @Override
    public int compareTo(UserCoin o) {
        int targetVol = o.getCoin().getVol();
        if (this.coin.getVol() == targetVol) return 0;
        else if (this.coin.getVol() > targetVol) return 1;
        else return -1;
    }
}
