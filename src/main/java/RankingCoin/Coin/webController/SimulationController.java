package RankingCoin.Coin.webController;

import RankingCoin.Coin.domain.Coin;
import RankingCoin.Coin.domain.Simulation;
import RankingCoin.Coin.domain.User;
import RankingCoin.Coin.domain.Wallet;
import RankingCoin.Coin.service.CoinService;
import RankingCoin.Coin.service.SimulationService;
import RankingCoin.Coin.service.UserDTO;
import RankingCoin.Coin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SimulationController {
    private final SimulationService simulationService;
    private final CoinService coinService;
    private final UserService userService;

    @GetMapping("/sim")
    public String getSimulation(@AuthenticationPrincipal UserDTO userDTO, Model model){
        List<Coin> coinList = coinService.findCoins();
        coinList.sort(Comparator.reverseOrder());
        List<Coin> newList = coinList.subList(0, 10);

        User user = userService.find(userDTO.getUsername());
        int sum = user.getKrw();

        List<Simulation> simulationList = simulationService.findAllSimulation(userDTO.getUsername());
        Collections.reverse(simulationList);
        List<Wallet> walletList = simulationService.findAllWallet(userDTO.getUsername());
        walletList.sort(Comparator.reverseOrder());
        for (Wallet wallet : walletList) {
            sum += wallet.getCoin().getKrw() * wallet.getCoinNum();
        }

        model.addAttribute("sum", sum);
        model.addAttribute("coinList", newList);
        model.addAttribute("walletList", walletList);
        model.addAttribute("simulationList", simulationList);
        model.addAttribute("userKrw", user.getKrw());
        model.addAttribute("update", newList.get(0).getUpdateTime());

        return "simulation/simulation";
    }

    @PostMapping("/sim/order/{id}")
    public String getOrder(@PathVariable("id")Long id, @AuthenticationPrincipal UserDTO userDTO,
                           @RequestParam("num") int num , @RequestParam("order") int order){
        if(num <= 0) return "redirect:/sim";

        User user = userService.find(userDTO.getUsername());
        Coin coin = coinService.find(id);
        List<Wallet> wallets = simulationService.findAllWallet(user.getName());

        if(order == 1 && user.getKrw() < coin.getKrw() * num){
            return "redirect:/sim";
        }
        else if(order == -1){
            for (Wallet wallet : wallets) {
                if(wallet.getCoin().equals(coin)){
                    if(wallet.getCoinNum() < num){
                        return "redirect:/sim";
                    }
                    break;
                }
            }
        }

        simulationService.addSimulation(user, coin, num * order);

        return "redirect:/sim";
    }

    @PostMapping("/sim/init")
    public String simInit(@AuthenticationPrincipal UserDTO userDTO){
        userService.walletInitial(userDTO.getUsername());
        return "redirect:/sim";
    }

    @GetMapping("/sim/rank")
    public String getRanking(Model model){
        List<User> userList = userService.findAll();
        userList.sort(Comparator.reverseOrder());

        model.addAttribute("userList", userList);

        return "simulation/ranking";
    }
}
