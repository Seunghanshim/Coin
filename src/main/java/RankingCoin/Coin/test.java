package RankingCoin.Coin;

import RankingCoin.Coin.domain.Coin;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class test {
    public static void main(String[] args) {
        final String url = "https://api.upbit.com/v1/candles/minutes/60?count=1&market={market}";
        RestTemplate restTemplate = new RestTemplate();
        JSONArray All_list = new JSONArray();

        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class, "KRW-BTC");

        String res = "{\"coin_price\":" + result.getBody() + "}";

        System.out.println(res);
//        JSONObject jsonObject = new JSONObject(res);
//        JSONArray list = jsonObject.getJSONArray("coin_price");
//
//        All_list.put(list.get(0));
    }
}
