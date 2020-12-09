package RankingCoin.Coin.repository;

import RankingCoin.Coin.domain.User;
import RankingCoin.Coin.domain.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class WalletRepository {
    private final EntityManager em;

    public void save(Wallet wallet){
        em.persist(wallet);
    }

    public void remove(Wallet wallet){
        em.remove(wallet);
    }

    public List<Wallet> findByUser(String name){
        return em.createQuery("select w from Wallet w where w.user.name =: name", Wallet.class)
                .setParameter("name", name).getResultList();
    }
}
