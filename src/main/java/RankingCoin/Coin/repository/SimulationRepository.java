package RankingCoin.Coin.repository;

import RankingCoin.Coin.domain.Simulation;
import RankingCoin.Coin.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.xml.crypto.dsig.SignedInfo;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SimulationRepository {

    private final EntityManager em;

    public void save(Simulation simulation){
        em.persist(simulation);
    }

    public void remove(Simulation simulation){
        em.remove(simulation);
    }

    public List<Simulation> findByUser(String name){
        return em.createQuery("select s from Simulation s where s.user.name =: name", Simulation.class)
                .setParameter("name", name)
                .getResultList();
    }
}
