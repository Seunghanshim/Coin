package RankingCoin.Coin.webController;

import RankingCoin.Coin.domain.Coin;
import RankingCoin.Coin.domain.Event;
import RankingCoin.Coin.domain.UserCoin;
import RankingCoin.Coin.service.CoinService;
import RankingCoin.Coin.service.EventService;
import RankingCoin.Coin.service.UserDTO;
import RankingCoin.Coin.service.UserService;
import RankingCoin.Coin.webController.DTO.CoinDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CoinController {
    private final CoinService coinService;
    private final UserService userService;
    private final EventService eventService;

    @GetMapping("/coin/list")
    public String getCoinList(Model model, @AuthenticationPrincipal UserDTO userDTO){
        List<Coin> coinList = coinService.findCoins();
        coinList.sort(Comparator.reverseOrder());

        model.addAttribute("max", coinList.get(0).getVol());

        List<Coin> favorList = new ArrayList<>();
        if(userDTO != null) {
            List<UserCoin> tmp = userService.findFavorites(userDTO.getUsername());
            for (UserCoin userCoin : tmp) {
                favorList.add(userCoin.getCoin());
            }
            model.addAttribute("favorList",favorList);
        }

        model.addAttribute("coinList", coinList);
        return "coin/coinList";
    }

    @GetMapping("/coin/{id}/view")
    public String getCoinInfo(@PathVariable("id")Long id, @AuthenticationPrincipal UserDTO userDTO, Model model) {
        List<Event> eventList = eventService.findByCoin(id);
        Coin coin = coinService.find(id);

        if(userDTO != null) model.addAttribute("name", userDTO.getUsername());
        model.addAttribute("eventList", eventList);
        model.addAttribute("coin", coin);

        if (userDTO != null && userService.checkFavor(userDTO.getUsername(), coin.getMarket())) {
            model.addAttribute("favor", true);
            return "coin/coinView";
        }

        model.addAttribute("favor", false);
        return "coin/coinView";
    }

}
