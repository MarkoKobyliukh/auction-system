package auctionsystem.service.impl;

import auctionsystem.dto.request.PlaceBidRequest;
import auctionsystem.dto.response.BidResponse;
import auctionsystem.entity.Auction;
import auctionsystem.entity.AuctionStatus;
import auctionsystem.entity.Bid;
import auctionsystem.entity.User;
import auctionsystem.exception.AuctionNotFoundException;
import auctionsystem.exception.BidValidationException;
import auctionsystem.exception.UnauthorizedAuctionActionException;
import auctionsystem.exception.UserNotFoundException;
import auctionsystem.repository.AuctionRepository;
import auctionsystem.repository.BidRepository;
import auctionsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BidServiceImplTest {

    private UserRepository userRepository;
    private AuctionRepository auctionRepository;
    private BidRepository bidRepository;

    private BidServiceImpl bidService;

    private Long auctionId;
    private Long bidderId;

    private User bidder;
    private User seller;
    private Auction auction;

    @BeforeEach
    void setup() {
        userRepository = Mockito.mock(UserRepository.class);
        auctionRepository = Mockito.mock(AuctionRepository.class);
        bidRepository = Mockito.mock(BidRepository.class);

        bidService = new BidServiceImpl(userRepository, auctionRepository, bidRepository);

        auctionId = 1L;
        bidderId = 99L;

        bidder = new User();
        bidder.setId(bidderId);
        bidder.setUsername("Marko");
        bidder.setEmail("marko@marko");

        seller = new User();
        seller.setId(1L);
        seller.setUsername("Seller");
        seller.setEmail("seller@seller");

        auction = new Auction();
        auction.setId(auctionId);
        auction.setAuctionStatus(AuctionStatus.ACTIVE);
        auction.setCurrentPrice(500);
        auction.setSeller(seller);
    }

    private PlaceBidRequest createRequest(double amount) {
        return new PlaceBidRequest(auctionId, bidderId, amount);
    }

    @Test
    void placeBid_shouldThrowException_whenUserDoesNotExist() {
        PlaceBidRequest request = createRequest(700);

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));
        when(userRepository.findById(bidderId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> bidService.placeBid(request));
    }

    @Test
    void placeBid_shouldThrowException_whenAuctionDoesNotExist() {
        PlaceBidRequest request = createRequest(700);

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.empty());

        assertThrows(AuctionNotFoundException.class, () -> bidService.placeBid(request));
    }

    @Test
    void placeBid_shouldThrowExceptionAndNotSaveBidOrAuction_whenAuctionIsClosed() {
        auction.setAuctionStatus(AuctionStatus.CLOSED);
        PlaceBidRequest request = createRequest(700);

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));
        when(userRepository.findById(bidderId)).thenReturn(Optional.of(bidder));

        assertThrows(BidValidationException.class, () -> bidService.placeBid(request));

        verify(bidRepository, never()).save(any(Bid.class));
        verify(auctionRepository, never()).save(any(Auction.class));
    }

    @Test
    void placeBid_shouldThrowExceptionAndNotSaveBidOrAuction_whenBidderIsSeller() {
        auction.setSeller(bidder);
        PlaceBidRequest request = createRequest(700);

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));
        when(userRepository.findById(bidderId)).thenReturn(Optional.of(bidder));

        assertThrows(UnauthorizedAuctionActionException.class, () -> bidService.placeBid(request));

        verify(bidRepository, never()).save(any(Bid.class));
        verify(auctionRepository, never()).save(any(Auction.class));
    }

    @Test
    void placeBid_shouldThrowException_whenBidAmountIsWrong() {
        PlaceBidRequest request = createRequest(500);

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));
        when(userRepository.findById(bidderId)).thenReturn(Optional.of(bidder));

        assertThrows(BidValidationException.class, () -> bidService.placeBid(request));
    }

    @Test
    void placeBid_shouldNotThrowException_whenBidIsSuccessful() {
        PlaceBidRequest request = createRequest(700);

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));
        when(userRepository.findById(bidderId)).thenReturn(Optional.of(bidder));
        when(bidRepository.save(any(Bid.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BidResponse response = bidService.placeBid(request);

        verify(bidRepository, times(1)).save(any(Bid.class));
        verify(auctionRepository, times(1)).save(auction);

        assertEquals(700, auction.getCurrentPrice());
        assertNotNull(response);
        assertEquals(700, response.getAmount());
        assertEquals(bidderId, response.getBidderId());
    }
}