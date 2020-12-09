package RankingCoin.Coin.ScheduleTask;

import RankingCoin.Coin.domain.Coin;
import RankingCoin.Coin.domain.Event;
import RankingCoin.Coin.service.CoinService;
import RankingCoin.Coin.service.EventService;
import jdk.nashorn.internal.ir.RuntimeNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import sun.misc.Request;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@RestController
@RequiredArgsConstructor
@Slf4j
public class ScheduleTask {
    private int ALL_coin_cnt = 0;
    private final CoinService coinService;
    private final EventService eventService;

    @Scheduled(cron = "0 5 * * * *") //1시간 마다 실행(*:05)
    public void getCoinPrice() throws InterruptedException {
        final String url = "https://api.upbit.com/v1/candles/minutes/60?market={market}&count=1";
        RestTemplate restTemplate = new RestTemplate();
        JSONArray All_list = new JSONArray();

        List<Coin> coinList = coinService.findCoins();
        for (Coin coin : coinList) {
            ResponseEntity<String> result = restTemplate.getForEntity(url, String.class, coin.getMarket());
            String res = "{\"coin_price\":" + result.getBody() + "}";

            JSONObject jsonObject = new JSONObject(res);
            JSONArray list = jsonObject.getJSONArray("coin_price");

            All_list.put(list.get(0));
            Thread.sleep(500);
        }

        coinService.AddPrice(All_list);
    }

    @Scheduled(cron = "0 10 * * * *") //1시간 마다 실행 (*:10)
    public void getCoinVolume() throws InterruptedException {
        final String url = "https://api.upbit.com/v1/candles/days?market={market}&count=1";
        RestTemplate restTemplate = new RestTemplate();
        JSONArray All_list = new JSONArray();

        List<Coin> coinList = coinService.findCoins();
        for (Coin coin : coinList) {
            ResponseEntity<String> result = restTemplate.getForEntity(url, String.class, coin.getMarket());
            String res = "{\"coin_vol\":" + result.getBody() + "}";

            JSONObject jsonObject = new JSONObject(res);
            JSONArray list = jsonObject.getJSONArray("coin_vol");

            All_list.put(list.get(0));
            Thread.sleep(500);
        }

        coinService.AddVolume(All_list);
    }


    @Scheduled(cron = "0 0 0 * * *") // 매일 0:15:00에 실행
    public void getCoinList(){//using API
        final String url = "https://api.upbit.com/v1/market/all";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);
        String res = "{\"coin_list\":" + result.getBody() + "}";

        JSONObject jsonObject = new JSONObject(res);
        JSONArray list = jsonObject.getJSONArray("coin_list");

        if(ALL_coin_cnt != list.length()) { // 신규 상장 혹은 폐지가 없는 경우 체크
            coinService.addAll(list);
            ALL_coin_cnt = list.length();
        }
    }

    @Scheduled(cron = "0 20 0 * * *") // 매일 0:20:00에 실행
    public void getEventList() throws IOException, ParseException { //not using API
        eventService.removeEvents(); //모든 호재 삭제(취소되거나 지난 호재 삭제 해주기 위함)

        LocalDateTime today = LocalDateTime.now();
        String url = "https://coinmarketcal.com/en/?form[date_range]=" + today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "+-+";
        String url2 = "&form[exchanges][]=upbit&form[categories][]=";
        url += today.plusMonths(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")); //향후 1달

        List<Coin> coinList = coinService.findCoins();
        List<Event> eventList = new ArrayList<>();

        for(int c = 1; c <= 16; c++) {
            if(c == 10 || c == 12) continue;
            log.info(url + url2 + c + "&page=");

            for (int page = 1; ; page++) {
                log.info("Initializing Coin's Event..("+ c + "/ 16)...page " + page);
                Document doc = Jsoup.connect(url + url2 + c + "&page=" + page).get();
                if (!doc.select("div[class=\"alert alert-transparent\"]").isEmpty()) break;

                Elements elem = doc.select("a").select("[href=\"#reminderModal\"]");
                for (Element e : elem) {
                    String name = e.attr("data-coin");
                    String[] data = name.split("\\(");
                    StringTokenizer t = new StringTokenizer(data[1], ")");
                    name = "KRW-" + t.nextToken();

                    for (Coin co: coinList){
                        if(co.getMarket().equals(name)){
                            String date = e.attr("data-date");
                            String what = e.attr("title");
                            Date time = new SimpleDateFormat("dd MMMMM yyyy", Locale.US).parse(date);
                            LocalDateTime when = LocalDateTime.ofInstant(time.toInstant(), ZoneId.systemDefault());

                            eventList.add(Event.createEvent(co, when, what, c));
                            break;
                        }
                    }
                }
            }
        }

        eventService.AddEventAll(eventList);
    }
}
