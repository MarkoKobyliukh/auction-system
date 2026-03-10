package auctionsystem.service;

import auctionsystem.dto.request.CreateAuctionRequest;
import auctionsystem.dto.response.AuctionCloseResponse;
import auctionsystem.dto.response.AuctionResponse;

import java.util.List;

public interface AuctionService {

    AuctionResponse createAuction(CreateAuctionRequest request);

    AuctionResponse getAuctionById(Long id);

    List<AuctionResponse> getAllAuctions();

    List<AuctionResponse> getAuctionsBySeller(Long sellerId);

    AuctionCloseResponse closeAuctionById(Long id);
}
