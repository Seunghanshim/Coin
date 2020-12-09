package RankingCoin.Coin.webController.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class PostForm {
    @NotEmpty(message = "제목을 입력하세요.")
    private String title;
    @NotEmpty(message = "내용을 입력하세요.")
    private String text;

    private boolean anonymous = false;
}
