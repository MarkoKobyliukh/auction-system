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
import auctionsystem.service.BidService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class BidServiceImpl implements BidService {

    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;

    // dependency injection
    public BidServiceImpl(UserRepository userRepository, AuctionRepository auctionRepository, BidRepository bidRepository){
        this.userRepository = userRepository;
        this.auctionRepository = auctionRepository;
        this.bidRepository = bidRepository;
    }

    @Override
    public BidResponse placeBid(PlaceBidRequest request){
        // checks
        Auction auction = auctionRepository.findById(request.getAuctionId()).orElseThrow(
                () -> new AuctionNotFoundException("Auction not found with id: " + request.getAuctionId())
        );

        User bidder = userRepository.findById(request.getBidderId()).orElseThrow(
                () -> new UserNotFoundException("User not found with id: " + request.getBidderId())
        );

        if (request.getAmount() <= 0)
            { throw new BidValidationException("The amount can't be negative or 0");}

        if (request.getAmount() <= auction.getCurrentPrice())
            { throw new BidValidationException("The amount can't be equal to or lower than current bid");}

        if (auction.getAuctionStatus().equals(AuctionStatus.CLOSED))
            { throw new BidValidationException("The auction is closed!");}

        if (Objects.equals(auction.getSeller().getId(), request.getBidderId()))
            { throw new UnauthorizedAuctionActionException("Seller can't place bids on their own auction");}

        // create Bid
        Bid bid = new Bid();
        bid.setAmount(request.getAmount());
        bid.setBidTime(LocalDateTime.now());
        bid.setAuction(auction);
        bid.setBidder(bidder);

        // save Bid
        Bid savedBid = bidRepository.save(bid);

        // update auction's CurrentPrice
        auction.setCurrentPrice(request.getAmount());

        // save updated auction
        auctionRepository.save(auction);

        return new BidResponse(
                savedBid.getId(),
                savedBid.getAmount(),
                savedBid.getBidTime(),
                savedBid.getAuction().getId(),
                savedBid.getBidder().getId(),
                savedBid.getBidder().getUsername()
        );
    }
}
