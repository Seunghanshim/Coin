package RankingCoin.Coin.api;

import RankingCoin.Coin.domain.Coin;
import RankingCoin.Coin.domain.User;
import RankingCoin.Coin.domain.UserCoin;
import RankingCoin.Coin.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/api/user")
    public CreateUserResponse saveUser(@RequestBody @Valid CreateUserRequest request){
        return new CreateUserResponse(userService.join(User.createUser(request.getName(), passwordEncoder.encode(request.getPwd()))));
    }

    @PutMapping("/api/user/{name}/favor")
    public boolean UpdateUserFavor(@PathVariable("name") String name, @RequestBody @Valid UpdateUserFavor request){
        return userService.addFavor(name, request.getMarket());
    }

    @GetMapping("/api/user/{name}/favor")
    public List<CoinDto> UsersCoins(@PathVariable("name") String name){
        List<UserCoin> favorites = userService.findFavorites(name);

        List<CoinDto> collect = favorites.stream()
                .map(m -> new CoinDto(m.getCoin().getId(), m.getCoin().getMarket(), m.getCoin().getKor(), m.getCoin().getKrw(), m.getCoin().getVol()))
                .collect(Collectors.toList());

        return collect;
    }

    @Data
    static class CreateUserRequest {
        private String name;
        private String pwd;
    }

    @Data
    @AllArgsConstructor
    class CreateUserResponse {
        private boolean check;
    }

    @Data
    static class UpdateUserFavor {
        private String market;
    }

    @Data
    @AllArgsConstructor
    class CoinDto {
        private Long id;
        private String market;
        private String kor;
        private int krw;
        private int vol;
    }
}
