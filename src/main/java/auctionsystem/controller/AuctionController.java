package auctionsystem.controller;

import auctionsystem.dto.request.CreateAuctionRequest;
import auctionsystem.dto.response.AuctionResponse;
import auctionsystem.service.AuctionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auctions")
public class AuctionController {

    private final AuctionService auctionService;


    // dependency injection
    public AuctionController(AuctionService auctionService){
        this.auctionService = auctionService;
    }

    @PostMapping
    public ResponseEntity<AuctionResponse> createAuction(@RequestBody CreateAuctionRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(auctionService.createAuction(request));
    }

    @GetMapping
    public ResponseEntity<List<AuctionResponse>> getAllAuctions(){
        return ResponseEntity.ok(auctionService.getAllAuctions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuctionResponse> getAuctionById(@PathVariable Long id){
        return ResponseEntity.ok(auctionService.getAuctionById(id));
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<AuctionResponse>> getAuctionsBySeller(@PathVariable Long sellerId){
        return ResponseEntity.ok(auctionService.getAuctionsBySeller(sellerId));
    }

}
