package auctionsystem.service;

import auctionsystem.dto.request.PlaceBidRequest;
import auctionsystem.dto.response.BidResponse;

public interface BidService {

    BidResponse placeBid(PlaceBidRequest request);
}
