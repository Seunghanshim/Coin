package RankingCoin.Coin.api;

import RankingCoin.Coin.domain.Comment;
import RankingCoin.Coin.domain.Post;
import RankingCoin.Coin.domain.User;
import RankingCoin.Coin.service.PostService;
import RankingCoin.Coin.service.UserService;
import RankingCoin.Coin.webController.DTO.PostForm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Lob;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PostApiController {
    private final PostService postService;
    private final UserService userService;

    @GetMapping("/api/post")
    public List<PostDTO> getPostList(){
        List<Post> postList = postService.findAll();

        List<PostDTO> collect = postList.stream()
                .map(m -> m.isAnonymous() ?
                        new PostDTO(m.getId(), m.getTitle(), "익명", m.getText(), m.getDate(), m.getView()) :
                        new PostDTO(m.getId(), m.getTitle(), m.getUser().getName(), m.getText(), m.getDate(), m.getView()))
                .collect(Collectors.toList());

        return collect;
    }

    @GetMapping("/api/{name}/post")
    public List<PostDTO> getUserPostList(@PathVariable("name") String name){
        List<Post> byUsername = postService.findByUsername(name);

        List<PostDTO> collect = byUsername.stream()
                .map(m -> m.isAnonymous() ?
                        new PostDTO(m.getId(), m.getTitle(), "익명", m.getText(), m.getDate(), m.getView()) :
                        new PostDTO(m.getId(), m.getTitle(), m.getUser().getName(), m.getText(), m.getDate(), m.getView()))
                .collect(Collectors.toList());

        return collect;
    }

    @GetMapping("/api/post/{id}")
    public PostDTO getPost(@PathVariable("id")Long id){
        Post post = postService.find(id);
        return new PostDTO(post.getId(), post.getTitle(),
                post.isAnonymous()? "익명" : post.getUser().getName()
                ,post.getText(), post.getDate(), post.getView());
    }

    @GetMapping("/api/post/{id}/com")
    public List<ComDTO> getComments(@PathVariable("id") Long id){
        List<Comment> byPost = postService.findByPost(id);
        List<ComDTO> collect = byPost.stream()
                .map(m -> m.isAnonymous() ? new ComDTO(m.getId(), "익명", m.getText(), m.getDate()) :
                        new ComDTO(m.getId(), m.getUser().getName(), m.getText(), m.getDate()))
                .collect(Collectors.toList());

        return collect;
    }

    @PostMapping("/api/{name}/post")
    public void createPost(@PathVariable("name")String name, @RequestBody @Valid CreatePostRequest createPostRequest){
        User user = userService.find(name);
        Post post = Post.createPost(user, createPostRequest.getTitle(), createPostRequest.getText(), createPostRequest.isAnonymous());
        postService.AddPost(post);
    }

    @PostMapping("/api/post/{id}/{name}/com")
    public void createComment(@PathVariable("id")Long id, @PathVariable("name")String username,
                              @RequestBody @Valid CreateComRequest createComRequest){
        Post post = postService.find(id);
        User user = userService.find(username);
        Comment comment = Comment.createComment(post, user, createComRequest.getText(), createComRequest.isAnonymous());
        postService.AddComment(comment);
    }

    @DeleteMapping("/api/post/{id}/remove")
    public void removePost(@PathVariable("id")Long id){
        postService.removePost(id);
    }

    @DeleteMapping("/api/post/{id}/com/remove")
    public void removeComment(@PathVariable("id")Long id){
        postService.removeComment(id);
    }

    @Data
    @AllArgsConstructor
    class ComDTO {
        private Long id;
        private String username;
        private String text;
        private LocalDateTime date;
    }

    @Data
    @AllArgsConstructor
    class PostDTO {
        private Long id;
        private String title;
        private String username;
        private String text;
        private LocalDateTime date;
        private int view;
    }

    @Data
    @AllArgsConstructor
    class UserPostResponse {
        private int postSize;
        private int comSize;
        private List<PostDTO> postList;
    }

    @Data
    static class CreatePostRequest {
        private String title;
        private String text;
        private boolean anonymous;
    }

    @Data
    static class CreateComRequest {
        private String text;
        private boolean anonymous;
    }
}
