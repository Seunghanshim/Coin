package RankingCoin.Coin.service;

import RankingCoin.Coin.domain.Coin;
import RankingCoin.Coin.domain.UserCoin;
import RankingCoin.Coin.repository.*;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CoinService {
    private final CoinRepository coinRepository;
    private final UserCoinRepository userCoinRepository;
    private final EventRepository eventRepository;
    private final SimulationRepository simulationRepository;
    private final WalletRepository walletRepository;

    @Transactional
    public void addAll(JSONArray list){
        List<Coin> coinList = coinRepository.findAll();
        Map<String, Boolean> listCheck = new HashMap<String,Boolean>();

        for(Coin c:coinList){ listCheck.put(c.getMarket(), false);}

        for(int i = 0; i < list.length(); i++) {
            JSONObject obj = list.getJSONObject(i);
            String market = obj.getString("market");
            StringTokenizer t = new StringTokenizer(market,"-");

            if(t.nextToken().equals("KRW")) {
                if (!listCheck.containsKey(market)) {
                    Coin coin = Coin.CreatCoin(market, obj.getString("english_name"), obj.getString("korean_name"));
                    coinRepository.save(coin);
                }
                else{
                    listCheck.put(market, true);
                }
            }
        }

        for(String market: listCheck.keySet()){
            if(!listCheck.get(market)){ //Coin삭제 시 user의 Wallet과 Simulation에서 어떻게 삭제할 건지 수정 필요
                userCoinRepository.removeByCoin(market);
                eventRepository.removeByCoin(market);
                Coin findCoin = coinRepository.findByMarket(market);
                coinRepository.remove(findCoin);
            }
        }
    }

    @Transactional
    public void AddPrice(JSONArray list){
        for(int j = 0; j < list.length(); j++){
            JSONObject obj = list.getJSONObject(j);
            String market = obj.getString("market");
            LocalDateTime dateTime = LocalDateTime
                    .parse(obj.getString("candle_date_time_kst"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

            Coin coin = coinRepository.findByMarket(market);

            coin.updatePrice(Math.round(obj.getFloat("trade_price")), dateTime);
        }
    }

    @Transactional
    public void AddVolume(JSONArray list){
        for(int j = 0; j < list.length(); j++){
            JSONObject obj = list.getJSONObject(j);
            String market = obj.getString("market");

            Coin coin = coinRepository.findByMarket(market);

            long volume = (long) obj.getFloat("candle_acc_trade_volume") * (long)obj.getFloat("trade_price");
            coin.updateVolume((int)(volume / 1000000));
        }
    }

//    @Transactional
//    public void update(String market, int updateScore){
//        coinRepository.findByMarket(market).get(0).updateOP(updateScore);
//    }

    @Transactional(readOnly = true)
    public List<Coin> findCoins(){
        return coinRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Coin find(Long id){
        return coinRepository.find(id);
    }
}
