package RankingCoin.Coin.repository;

import RankingCoin.Coin.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public void save(User user){ em.persist(user); }

    public List<User> findAll(){
        return em.createQuery("select u from User u",User.class)
                .getResultList();
    }

    public List<User> find(String name){
        return em.createQuery("select u from User u where u.name =: name", User.class)
                .setParameter("name", name)
                .getResultList();
    }
}
