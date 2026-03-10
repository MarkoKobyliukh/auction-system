package auctionsystem.service.impl;

import auctionsystem.dto.request.CreateAuctionRequest;
import auctionsystem.dto.response.AuctionCloseResponse;
import auctionsystem.dto.response.AuctionResponse;
import auctionsystem.entity.Auction;
import auctionsystem.entity.AuctionStatus;
import auctionsystem.entity.Bid;
import auctionsystem.entity.User;
import auctionsystem.exception.AuctionIsClosedException;
import auctionsystem.exception.AuctionNotFoundException;
import auctionsystem.repository.AuctionRepository;
import auctionsystem.repository.BidRepository;
import auctionsystem.repository.UserRepository;
import auctionsystem.service.AuctionService;
import org.springframework.stereotype.Service;
import auctionsystem.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final BidRepository bidRepository;

    // dependency injection
    public AuctionServiceImpl(AuctionRepository auctionRepository, UserRepository userRepository, BidRepository bidRepository){
        this.auctionRepository = auctionRepository;
        this.userRepository = userRepository;
        this.bidRepository = bidRepository;
    }

    // This method avoids duplication the same AuctionResponse constructor everywhere and keeps the service cleaner
    private AuctionResponse mapToResponse(Auction auction){
        return new AuctionResponse(
                auction.getId(),
                auction.getTitle(),
                auction.getDescription(),
                auction.getStartingPrice(),
                auction.getCurrentPrice(),
                auction.getStartTime(),
                auction.getEndTime(),
                auction.getAuctionStatus(),
                auction.getSeller().getId(),
                auction.getSeller().getUsername()
        );
    }

    @Override
    public AuctionResponse createAuction(CreateAuctionRequest request){
        Auction auction = new Auction();
        auction.setTitle(request.getTitle());
        auction.setDescription(request.getDescription());
        auction.setStartingPrice(request.getStartingPrice());
        auction.setCurrentPrice(request.getStartingPrice());
        auction.setStartTime(LocalDateTime.now());
        auction.setEndTime(request.getEndTime());
        auction.setAuctionStatus(AuctionStatus.ACTIVE);

        User seller = userRepository.findById(request.getSellerId()).orElseThrow(
                () -> new UserNotFoundException("User not found with id: " + request.getSellerId())
        );
        auction.setSeller(seller);

        Auction savedAuction = auctionRepository.save(auction);
        return mapToResponse(savedAuction);
    }

    @Override
    public AuctionResponse getAuctionById(Long id){
        Auction auction = auctionRepository.findById(id).orElseThrow(
                () -> new AuctionNotFoundException("Auction not found with id: " + id)
        );
        return mapToResponse(auction);
    }

    @Override
    public List<AuctionResponse> getAllAuctions(){
        return auctionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuctionResponse> getAuctionsBySeller(Long sellerId){

        User seller = userRepository.findById(sellerId).orElseThrow(
                () -> new UserNotFoundException("User not found with id: " + sellerId)
        );

        return auctionRepository.findBySeller(seller)
                .stream()
                .map(this::mapToResponse)
                .toList(); // shorter modern version of collect(Collectors.toList())
    }

    @Override
    public AuctionCloseResponse closeAuctionById(Long id) {

        Auction auction = auctionRepository.findById(id).orElseThrow(
                () -> new AuctionNotFoundException("Auction not found with id: " + id)
        );

        if (auction.getAuctionStatus() == AuctionStatus.CLOSED) {
            throw new AuctionIsClosedException("This auction is already closed");
        }

        User winner = null;
        double highestBid = auction.getCurrentPrice();

        List<Bid> bids = bidRepository.findByAuction(auction);

        for (Bid bid : bids) {
            if (bid.getAmount() > highestBid) {
                highestBid = bid.getAmount();
                winner = bid.getBidder();
            }
        }

        auction.setWinner(winner);
        auction.setAuctionStatus(AuctionStatus.CLOSED);
        auctionRepository.save(auction);

        return new AuctionCloseResponse(
                auction.getId(),
                auction.getTitle(),
                highestBid,
                auction.getAuctionStatus(),
                winner != null ? winner.getId() : null,
                winner != null ? winner.getUsername() : null
        );
    }
}
