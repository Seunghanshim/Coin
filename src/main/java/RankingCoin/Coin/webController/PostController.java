package RankingCoin.Coin.webController;

import RankingCoin.Coin.domain.Comment;
import RankingCoin.Coin.domain.Post;
import RankingCoin.Coin.domain.User;
import RankingCoin.Coin.service.PostService;
import RankingCoin.Coin.service.UserDTO;
import RankingCoin.Coin.service.UserService;
import RankingCoin.Coin.webController.DTO.CommentForm;
import RankingCoin.Coin.webController.DTO.PostForm;
import RankingCoin.Coin.webController.DTO.PostSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final UserService userService;

    @GetMapping(value = "/post")
    public String getPostList(@ModelAttribute("postSearch") PostSearch postSearch, Model model){
        List<Post> postList;
        if(StringUtils.hasText(postSearch.getText())) {
            if(postSearch.isCheck()){
                postList = postService.findByUsername("%" + postSearch.getText() + "%");

                postList.removeIf(p -> p.isAnonymous() == true);
            }
            else{
                postList = postService.findByTitle("%" + postSearch.getText() + "%");

            }

            postSearch.setText(null);
        }
        else {
            postList = postService.findAll();
        }

        postList.sort(Comparator.reverseOrder());
        model.addAttribute("postList", postList);

        return "post/postList";
    }

    @GetMapping(value = "/post/user")
    public String getPostListByUser(Model model, @AuthenticationPrincipal UserDTO userDTO){
        List<Post> postList = postService.findByUsername(userDTO.getUsername());
        List<Comment> comments = postService.findComByUsername(userDTO.getUsername());


        model.addAttribute("postList", postList);
        model.addAttribute("postSize", postList.size());
        model.addAttribute("comSize", comments.size());

        return "post/userPostList";
    }

    @GetMapping(value = "/post/new")
    public String createPostForm(Model model, @AuthenticationPrincipal UserDTO userDTO){
        model.addAttribute("postForm", new PostForm());
        model.addAttribute("username", userDTO.getUsername());
        return "post/createPost";
    }

    @PostMapping(value = "/post/new")
    public String createPost(@RequestParam("username") String username, PostForm postForm){
        User user = userService.find(username);

        Post post = Post.createPost(user, postForm.getTitle(), postForm.getText(), postForm.isAnonymous());
        postService.AddPost(post);

        return "redirect:/post";
    }

    @GetMapping(value = "/post/{id}/view")
    public String PostView(@PathVariable("id")Long id, @AuthenticationPrincipal UserDTO userDTO,
                           @ModelAttribute("commentForm")CommentForm commentForm, Model model){
        Post post = postService.find(id);
        postService.updateView(post);

        if(StringUtils.hasText(commentForm.getText())) {
            User user = userService.find(userDTO.getUsername());
            Comment comment = Comment.createComment(post, user, commentForm.getText(), commentForm.isAnonymous());
            postService.AddComment(comment);
            commentForm.setText(null);
        }

        List<Comment> comments = postService.findByPost(id);

        model.addAttribute("post", post);
        model.addAttribute("postText", post.getText().replace("\r\n","<br>"));
        model.addAttribute("comments", comments);

        return "post/postView";
    }

    @GetMapping(value = "/post/{id}/remove")
    public String removePost(@PathVariable("id") Long id){
        postService.removePost(id);

        return "redirect:/post";
    }

    @GetMapping(value = "/post/{id}/com/remove")
    public String removeCom(@PathVariable("id") Long id, @RequestParam("comId")Long comId){
        postService.removeComment(comId);

        return "redirect:/post/{id}/view";
    }
}
