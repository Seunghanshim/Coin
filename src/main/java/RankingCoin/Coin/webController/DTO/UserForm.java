package RankingCoin.Coin.webController.DTO;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class UserForm {
    @NotEmpty(message = "ID를 입력하세요.")
    private String id;
    @NotEmpty(message = "비밀번호를 입력하세요.")
    private String pwd;
    private String pwdCh;
}
