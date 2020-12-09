package RankingCoin.Coin.service;

import RankingCoin.Coin.domain.*;
import RankingCoin.Coin.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final CoinRepository coinRepository;
    private final UserCoinRepository userCoinRepository;
    private final WalletRepository walletRepository;
    private final SimulationRepository simulationRepository;

    @Transactional
    public boolean join(User user){
        if(userRepository.find(user.getName()).isEmpty()) {
            userRepository.save(user);
            return true;
        }
        else{
            return false;
        }
    }

    @Transactional(readOnly = true)
    public User find(String name){
        return userRepository.find(name).get(0);
    }

    @Transactional
    public boolean addFavor(String name, String market){
        List<UserCoin> userCoins = userCoinRepository.find(name, market);
        if(userCoins.isEmpty()) {
            User user = userRepository.find(name).get(0);
            Coin coin = coinRepository.findByMarket(market);
            userCoinRepository.save(UserCoin.create(user, coin));
            return true;
        }
        else {
            userCoinRepository.remove(userCoins.get(0));
            return false;
        }
    }

    @Transactional(readOnly = true)
    public List<UserCoin> findFavorites(String name){
        return userCoinRepository.findByUser(name);
    }

    @Transactional(readOnly = true)
    public boolean checkFavor(String name, String market){
        if(userCoinRepository.find(name, market).isEmpty()){
            return false;
        }
        else return true;
    }

    @Transactional
    public void walletInitial(String name){
        User findUser = userRepository.find(name).get(0);
        List<Wallet> wallets = walletRepository.findByUser(name);
        for (Wallet wallet : wallets) {
            walletRepository.remove(wallet);
        }

        List<Simulation> simulations = simulationRepository.findByUser(name);
        for (Simulation simulation : simulations) {
            simulationRepository.remove(simulation);
        }

        findUser.updateKrw(50000000);
    }

    @Transactional
    public List<User> findAll(){
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> users = userRepository.find(username);
        if(userRepository.find(username).isEmpty()){
            throw new UsernameNotFoundException(username);
        }

        User user = users.get(0);
        UserDTO userDTO = new UserDTO(user.getName(), user.getPwd());

        return userDTO;
    }
}
