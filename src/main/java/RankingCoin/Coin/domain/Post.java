package RankingCoin.Coin.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Clob;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post implements Comparable<Post>{
    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    @Lob
    private String text;

    private LocalDateTime date;
    private int view;
    private boolean anonymous;

    public static Post createPost(User user, String title, String text, boolean anonymous) {
        Post post = new Post();
        post.user = user;
        post.title = title;
        post.text = text;
        post.anonymous = anonymous;
        post.view = 0;
        post.date = LocalDateTime.now();

        return post;
    }

    public void updateView(){
        this.view += 1;
    }

    @Override
    public int compareTo(Post o) {
        LocalDateTime targetDate = o.getDate();
        if(this.date.isEqual(targetDate)) return 0;
        else if(this.date.isAfter(targetDate)) return 1;
        else return -1;
    }
}
