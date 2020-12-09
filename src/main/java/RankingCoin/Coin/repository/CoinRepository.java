package RankingCoin.Coin.repository;

import RankingCoin.Coin.domain.Coin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CoinRepository {
    private final EntityManager em;

    public void save(Coin coin){ em.persist(coin); }

    public void remove(Coin coin){ em.remove(coin); }

    public Coin find(Long id){return em.find(Coin.class, id);}

    public Coin findByMarket(String market){
        return em.createQuery("select c from Coin c where c.market =: market", Coin.class)
                .setParameter("market", market)
                .getSingleResult();
    }

    public List<Coin> findAll(){
        return em.createQuery("select c from Coin c", Coin.class).getResultList();
    }

    public List<Coin> findByName(String name){
        return em.createQuery("select c from Coin c where c.kor like :name",Coin.class)
                .setParameter("name", name)
                .getResultList();
    }
}
