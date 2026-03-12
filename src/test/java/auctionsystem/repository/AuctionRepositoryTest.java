package auctionsystem.repository;

import auctionsystem.entity.Auction;
import auctionsystem.entity.AuctionStatus;
import auctionsystem.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AuctionRepositoryTest {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findBySeller_shouldReturnAuctionsForGivenSeller() {

        // seller
        User seller = new User();
        seller.setUsername("seller1");
        seller.setEmail("seller@test.com");
        seller = userRepository.save(seller);

        // auction 1
        Auction auction1 = new Auction();
        auction1.setTitle("Laptop");
        auction1.setStartingPrice(500);
        auction1.setCurrentPrice(500);
        auction1.setAuctionStatus(AuctionStatus.ACTIVE);
        auction1.setSeller(seller);

        // auction 2
        Auction auction2 = new Auction();
        auction2.setTitle("Phone");
        auction2.setStartingPrice(300);
        auction2.setCurrentPrice(300);
        auction2.setAuctionStatus(AuctionStatus.ACTIVE);
        auction2.setSeller(seller);

        auctionRepository.saveAll(List.of(auction1, auction2));

        // execute
        List<Auction> result = auctionRepository.findBySeller(seller);

        // verify
        assertEquals(2, result.size());
        assertEquals("Laptop", result.get(0).getTitle());
        assertEquals("Phone", result.get(1).getTitle());
    }
}