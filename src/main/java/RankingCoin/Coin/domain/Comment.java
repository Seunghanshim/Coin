package RankingCoin.Coin.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    private String text;
    private LocalDateTime date;
    private boolean anonymous;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Comment createComment(Post post, User user, String text, boolean anonymous){
        Comment comment = new Comment();
        comment.post =post;
        comment.user = user;
        comment.text = text;
        comment.anonymous = anonymous;
        comment.date = LocalDateTime.now();

        return comment;
    }
}
