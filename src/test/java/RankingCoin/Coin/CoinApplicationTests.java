package RankingCoin.Coin;

import RankingCoin.Coin.domain.Coin;
import RankingCoin.Coin.domain.Post;
import RankingCoin.Coin.repository.CoinRepository;
import RankingCoin.Coin.repository.EventRepository;
import RankingCoin.Coin.repository.UserCoinRepository;
import RankingCoin.Coin.service.PostService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CoinApplicationTests {
	@Autowired
	CoinRepository coinRepository;
	@Autowired
	EventRepository eventRepository;
	@Autowired
	UserCoinRepository userCoinRepository;

	@Test
	@Transactional
	@Rollback(false)
	public void contextLoads() {
		Coin coin = coinRepository.findByMarket("KRW-ARK");
		eventRepository.removeByCoin(coin.getMarket());
		userCoinRepository.removeByCoin(coin.getMarket());
		coinRepository.remove(coin);
	}
}
