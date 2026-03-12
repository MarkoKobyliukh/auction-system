package auctionsystem.service.impl;

import auctionsystem.dto.response.AuctionCloseResponse;
import auctionsystem.entity.Auction;
import auctionsystem.entity.AuctionStatus;
import auctionsystem.entity.Bid;
import auctionsystem.entity.User;
import auctionsystem.exception.AuctionIsClosedException;
import auctionsystem.repository.AuctionRepository;
import auctionsystem.repository.BidRepository;
import auctionsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuctionServiceImplTest {

    private AuctionRepository auctionRepository;
    private UserRepository userRepository;
    private BidRepository bidRepository;

    private AuctionServiceImpl auctionService;

    private Long auctionId;
    private Long sellerId;
    private Long bidderId1;
    private Long bidderId2;

    private User seller;
    private User bidder1;
    private User bidder2;
    private Auction auction;

    @BeforeEach
    void setup() {
        auctionRepository = Mockito.mock(AuctionRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        bidRepository = Mockito.mock(BidRepository.class);

        auctionService = new AuctionServiceImpl(auctionRepository, userRepository, bidRepository);

        auctionId = 1L;
        sellerId = 10L;
        bidderId1 = 20L;
        bidderId2 = 30L;

        seller = new User();
        seller.setId(sellerId);
        seller.setUsername("Seller");
        seller.setEmail("seller@test.com");

        bidder1 = new User();
        bidder1.setId(bidderId1);
        bidder1.setUsername("BidderOne");
        bidder1.setEmail("bidder1@test.com");

        bidder2 = new User();
        bidder2.setId(bidderId2);
        bidder2.setUsername("BidderTwo");
        bidder2.setEmail("bidder2@test.com");

        auction = new Auction();
        auction.setId(auctionId);
        auction.setTitle("Laptop");
        auction.setAuctionStatus(AuctionStatus.ACTIVE);
        auction.setStartingPrice(500);
        auction.setCurrentPrice(700);
        auction.setSeller(seller);
    }

    private Bid createBid(double amount, User bidder, Auction auction) {
        Bid bid = new Bid();
        bid.setAmount(amount);
        bid.setBidder(bidder);
        bid.setAuction(auction);
        return bid;
    }

    @Test
    void closeAuction_shouldSucceed_whenAuctionHasBids() {
        Bid bid1 = createBid(800, bidder1, auction);
        Bid bid2 = createBid(900, bidder2, auction);

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));
        when(bidRepository.findByAuction(auction)).thenReturn(List.of(bid1, bid2));

        AuctionCloseResponse response = auctionService.closeAuctionById(auctionId);

        verify(auctionRepository, times(1)).save(auction);

        assertNotNull(response);
        assertEquals(auctionId, response.getAuctionId());
        assertEquals(900, response.getFinalPrice());
        assertEquals(bidder2.getId(), response.getWinnerId());
        assertEquals(bidder2.getUsername(), response.getWinnerUsername());

        assertEquals(AuctionStatus.CLOSED, auction.getAuctionStatus());
        assertEquals(bidder2, auction.getWinner());
    }

    @Test
    void closeAuction_shouldSucceed_whenAuctionHasNoBids() {
        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));
        when(bidRepository.findByAuction(auction)).thenReturn(List.of());

        AuctionCloseResponse response = auctionService.closeAuctionById(auctionId);

        verify(auctionRepository, times(1)).save(auction);

        assertNotNull(response);
        assertEquals(auctionId, response.getAuctionId());
        assertEquals(auction.getCurrentPrice(), response.getFinalPrice());
        assertNull(response.getWinnerId());
        assertNull(response.getWinnerUsername());

        assertEquals(AuctionStatus.CLOSED, auction.getAuctionStatus());
        assertNull(auction.getWinner());
    }

    @Test
    void closeAuction_shouldThrowException_whenAuctionIsAlreadyClosed() {
        auction.setAuctionStatus(AuctionStatus.CLOSED);

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));

        assertThrows(AuctionIsClosedException.class, () -> auctionService.closeAuctionById(auctionId));

        verify(auctionRepository, never()).save(any(Auction.class));
    }

    @Test
    void closeAuction_shouldSelectCorrectWinner_whenMultipleBidsExist() {
        Bid bid1 = createBid(750, bidder1, auction);
        Bid bid2 = createBid(950, bidder2, auction);
        Bid bid3 = createBid(850, bidder1, auction);

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));
        when(bidRepository.findByAuction(auction)).thenReturn(List.of(bid1, bid2, bid3));

        AuctionCloseResponse response = auctionService.closeAuctionById(auctionId);

        assertEquals(bidder2.getId(), response.getWinnerId());
        assertEquals(bidder2.getUsername(), response.getWinnerUsername());
        assertEquals(950, response.getFinalPrice());
        assertEquals(bidder2, auction.getWinner());
    }

    @Test
    void closeAuction_shouldSetStatusToClosed_whenClosingAuctionSuccessfully() {
        Bid bid = createBid(800, bidder1, auction);

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));
        when(bidRepository.findByAuction(auction)).thenReturn(List.of(bid));

        auctionService.closeAuctionById(auctionId);

        assertEquals(AuctionStatus.CLOSED, auction.getAuctionStatus());
        verify(auctionRepository, times(1)).save(auction);
    }

    @Test
    void closeAuction_shouldNotSaveAuction_whenAuctionIsAlreadyClosed() {
        auction.setAuctionStatus(AuctionStatus.CLOSED);

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));

        assertThrows(AuctionIsClosedException.class, () -> auctionService.closeAuctionById(auctionId));

        verify(auctionRepository, never()).save(any(Auction.class));
    }
}