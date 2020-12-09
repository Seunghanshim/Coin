package RankingCoin.Coin.repository;

import RankingCoin.Coin.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {
    private final EntityManager em;

    public void save(Post post) {
        em.persist(post);
    }

    public Post find(Long id) {
        return em.find(Post.class, id);
    }

    public void remove(Post post) {
        em.remove(post);
    }

    public List<Post> findAll() {
        return em.createQuery("select p from Post p join fetch p.user", Post.class).getResultList();
    }

    public List<Post> findByUsername(String username) {
        return em.createQuery("select p from Post p join fetch p.user where p.user.name like : username", Post.class)
                .setParameter("username", username)
                .getResultList();
    }

    public List<Post> findByTitle(String title) {
        return em.createQuery("select p from Post p join fetch p.user where p.title like :title", Post.class)
                .setParameter("title", title)
                .getResultList();
    }
}
