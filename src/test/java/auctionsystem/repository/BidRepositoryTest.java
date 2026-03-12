package auctionsystem.repository;

import auctionsystem.entity.Auction;
import auctionsystem.entity.AuctionStatus;
import auctionsystem.entity.Bid;
import auctionsystem.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BidRepositoryTest {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByAuction_shouldReturnBidsForAuction() {

        // user
        User bidder = new User();
        bidder.setUsername("bidder");
        bidder.setEmail("bidder@test.com");
        bidder = userRepository.save(bidder);

        // auction
        Auction auction = new Auction();
        auction.setTitle("Camera");
        auction.setStartingPrice(100);
        auction.setCurrentPrice(100);
        auction.setAuctionStatus(AuctionStatus.ACTIVE);
        auction.setSeller(bidder);
        auction = auctionRepository.save(auction);

        // bids
        Bid bid1 = new Bid();
        bid1.setAmount(150);
        bid1.setAuction(auction);
        bid1.setBidder(bidder);

        Bid bid2 = new Bid();
        bid2.setAmount(200);
        bid2.setAuction(auction);
        bid2.setBidder(bidder);

        bidRepository.saveAll(List.of(bid1, bid2));

        // execute
        List<Bid> result = bidRepository.findByAuction(auction);

        // verify
        assertEquals(2, result.size());
        assertEquals(150, result.get(0).getAmount());
        assertEquals(200, result.get(1).getAmount());
    }
}