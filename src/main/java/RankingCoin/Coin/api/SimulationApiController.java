package RankingCoin.Coin.api;

import RankingCoin.Coin.domain.Coin;
import RankingCoin.Coin.domain.Simulation;
import RankingCoin.Coin.domain.User;
import RankingCoin.Coin.domain.Wallet;
import RankingCoin.Coin.service.CoinService;
import RankingCoin.Coin.service.SimulationService;
import RankingCoin.Coin.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class SimulationApiController {
    private final UserService userService;
    private final CoinService coinService;
    private final SimulationService simulationService;

    @PutMapping("/api/sim/{name}/init")
    public void init(@PathVariable("name") String name){
        userService.walletInitial(name);
    }

    @PutMapping("/api/sim/{name}/order")
    public boolean order(@PathVariable("name") String name, @RequestBody @Valid OrderRequest orderRequest){
        if(orderRequest.getNum() <= 0) return false;

        User user = userService.find(name);
        Coin coin = coinService.find(orderRequest.getCoinId());
        List<Wallet> wallets = simulationService.findAllWallet(name);

        if(orderRequest.getOrder() == 1 && user.getKrw() < coin.getKrw() * orderRequest.getNum()){
            return false;
        }
        else if(orderRequest.getOrder() == -1){
            for (Wallet wallet : wallets) {
                if(wallet.getCoin().equals(coin)){
                    if(wallet.getCoinNum() < orderRequest.getNum()){
                        return false;
                    }
                    break;
                }
            }
        }

        simulationService.addSimulation(user, coin, orderRequest.getNum() * orderRequest.getOrder());

        return true;
    }

    @GetMapping("/api/sim/{name}/krw")
    public int getKrw(@PathVariable("name") String name){
        return userService.find(name).getKrw();
    }

    @GetMapping("/api/sim/{name}/log")
    public List<simDTO> getLogs(@PathVariable("name") String name){
        List<Simulation> simulations = simulationService.findAllSimulation(name);

        List<simDTO> collect = simulations.stream()
                .map(m -> new simDTO(m.getWhen(), m.getCoin().getKor(), m.getCoinPrice(), m.getCoinNum()))
                .collect(Collectors.toList());

        return collect;
    }

    @GetMapping("/api/sim/{name}/wallet")
    public List<walletDTO> getWallets(@PathVariable("name") String name){
        List<Wallet> wallets = simulationService.findAllWallet(name);

        List<walletDTO> collect = wallets.stream()
                .map(m -> new walletDTO(m.getCoin().getKor(), m.getCoin().getKrw(), m.getCoinNum()))
                .collect(Collectors.toList());

        return collect;
    }
    
    @Data
    @AllArgsConstructor
    class walletDTO {
        private String coinName;
        private int coinPrice;
        private int num;
    }

    @Data
    @AllArgsConstructor
    class simDTO {
        private LocalDateTime when;
        private String coinName;
        private int coinPrice;
        private int num;
    }

    @Data
    static class OrderRequest {
        private Long coinId;
        private int num;
        private int order;
    }
}
