package RankingCoin.Coin.repository;

import RankingCoin.Coin.domain.Coin;
import RankingCoin.Coin.domain.User;
import RankingCoin.Coin.domain.UserCoin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserCoinRepository {
    private final EntityManager em;

    public void save(UserCoin userCoin){
        em.persist(userCoin);
    }

    public List<UserCoin> find(String name, String market){
        return em.createQuery("select uc from UserCoin uc where uc.user.name = :name and uc.coin.market = :market", UserCoin.class)
                .setParameter("name", name)
                .setParameter("market", market)
                .getResultList();
    }

    public List<UserCoin> findByUser(String name){
        return em.createQuery("select uc from UserCoin uc join fetch uc.coin where uc.user.name = :name", UserCoin.class)
                .setParameter("name", name)
                .getResultList();
    }

    public void removeByCoin(String market){
        List<UserCoin> userCoinList = em.createQuery("select uc from UserCoin uc where uc.coin.market = :market", UserCoin.class)
                .setParameter("market", market)
                .getResultList();

        for (UserCoin userCoin : userCoinList) {
            em.remove(userCoin);
        }
    }

    public void remove(UserCoin userCoin){
        em.remove(userCoin);
    }
}
