package RankingCoin.Coin.service;

import RankingCoin.Coin.domain.Coin;
import RankingCoin.Coin.domain.Simulation;
import RankingCoin.Coin.domain.User;
import RankingCoin.Coin.domain.Wallet;
import RankingCoin.Coin.repository.CoinRepository;
import RankingCoin.Coin.repository.SimulationRepository;
import RankingCoin.Coin.repository.UserRepository;
import RankingCoin.Coin.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SimulationService {

    private final SimulationRepository simulationRepository;
    private final WalletRepository walletRepository;

    // user가 가진 krw(원화) 이상의 buy 주문은 안들어온다 가정
    // user가 가진 coin 이상의 sell 주문은 안들어온다 가정
    // Num이 0인 경우는 없고, 0 미만은 sell 0 초과는 buy로 가정
    @Transactional
    public void addSimulation(User user, Coin coin, int Num){
        simulationRepository.save(Simulation.createSimulation(user, coin, Num));

        user.updateKrw(user.getKrw() - (coin.getKrw() * Num));

        List<Wallet> wallets = walletRepository.findByUser(user.getName());
        for (Wallet wallet : wallets) {
            if(wallet.getCoin().equals(coin)){
                int newNum = wallet.getCoinNum() + Num;
                if(newNum == 0) {
                    walletRepository.remove(wallet);
                }
                else{
                    wallet.updateCoinNum(newNum);
                }
                return;
            }
        }

        walletRepository.save(Wallet.createWallet(user, coin, Num));
    }

    @Transactional(readOnly = true)
    public List<Simulation> findAllSimulation(String name){
        return simulationRepository.findByUser(name);
    }

    @Transactional(readOnly = true)
    public List<Wallet> findAllWallet(String name){
        return walletRepository.findByUser(name);
    }
}
