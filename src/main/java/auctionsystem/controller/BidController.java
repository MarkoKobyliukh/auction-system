package auctionsystem.controller;

import auctionsystem.dto.request.PlaceBidRequest;
import auctionsystem.dto.response.BidResponse;
import auctionsystem.service.BidService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bids")
public class BidController {

    private final BidService bidService;

    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    @PostMapping
    public ResponseEntity<BidResponse> placeBid(@RequestBody PlaceBidRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bidService.placeBid(request));
    }
}
