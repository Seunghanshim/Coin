package RankingCoin.Coin.repository;

import RankingCoin.Coin.domain.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepository {
    private final EntityManager em;

    public void save(Comment comment){
        em.persist(comment);
    }

    public Comment find(Long id){
        return em.find(Comment.class, id);
    }

    public List<Comment> findByUsername(String username){
        return em.createQuery("select c from Comment c where c.user.name =: username", Comment.class)
                .setParameter("username", username)
                .getResultList();
    }

    public List<Comment> findByPost(Long postId){
        return em.createQuery("select c from Comment c join fetch c.user where c.post.id =: postId", Comment.class)
                .setParameter("postId", postId)
                .getResultList();
    }

    public void remove(Comment comment){
        em.remove(comment);
    }
}
