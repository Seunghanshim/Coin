package RankingCoin.Coin.service;

import RankingCoin.Coin.domain.Comment;
import RankingCoin.Coin.domain.Post;
import RankingCoin.Coin.repository.CommentRepository;
import RankingCoin.Coin.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void AddPost(Post post){
        postRepository.save(post);
    }

    @Transactional
    public void updateView(Post post){
        post.updateView();
    }

    @Transactional
    public void removePost(Long id){
        Post post = postRepository.find(id);
        List<Comment> byPost = commentRepository.findByPost(id);
        for (Comment comment : byPost) {
            commentRepository.remove(comment);
        }
        postRepository.remove(post);
    }

    public Post find(Long id){
        return postRepository.find(id);
    }

    public List<Post> findAll(){
        return postRepository.findAll();
    }

    public List<Post> findByUsername(String username){
        return postRepository.findByUsername(username);
    }

    public List<Post> findByTitle(String title){
        return postRepository.findByTitle(title);
    }

    @Transactional
    public void AddComment(Comment comment){
        commentRepository.save(comment);
    }

    @Transactional
    public void removeComment(Long commentId){
        Comment comment = commentRepository.find(commentId);
        commentRepository.remove(comment);
    }

    public List<Comment> findByPost(Long postId){
        return commentRepository.findByPost(postId);
    }

    public List<Comment> findComByUsername(String username){
        return commentRepository.findByUsername(username);
    }
}
