package auctionsystem.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BidResponse {

    private Long id;
    private double amount;
    private LocalDateTime bidTime;
    private Long auctionId;
    private Long bidderId;
    private String bidderUsername;

    public BidResponse() {}

    public BidResponse(Long id, double amount, LocalDateTime bidTime, Long auctionId, Long bidderId, String bidderUsername){
        this.id = id;
        this.amount = amount;
        this.bidTime = bidTime;
        this.auctionId = auctionId;
        this.bidderId = bidderId;
        this.bidderUsername = bidderUsername;
    }
}
