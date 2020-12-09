package RankingCoin.Coin.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements Comparable<User>{
    @Id @Column(name = "user_id")
    @GeneratedValue
    private Long id;

    private String name;
    private String pwd;
    private int krw;

    public static User createUser(String name, String pwd){
        User user = new User();
        user.name = name;
        user.pwd = pwd;
        user.krw = 50000000;
        return user;
    }

    public void updateKrw(int krw){
        this.krw = krw;
    }

    @Override
    public int compareTo(User o) {
        int targetVol = o.krw;
        if (this.krw == targetVol) return 0;
        else if (this.krw > targetVol) return 1;
        else return -1;
    }
}
