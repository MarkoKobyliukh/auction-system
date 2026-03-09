package auctionsystem.dto.response;

import auctionsystem.entity.AuctionStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class AuctionResponse {

    private Long id;
    private String title;
    private String description;
    private double startingPrice;
    private double currentPrice;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private AuctionStatus auctionStatus;
    private Long sellerId;
    private String sellerUsername;

    public AuctionResponse() {
    }

    public AuctionResponse(Long id, String title, String description, double startingPrice, double currentPrice,
                           LocalDateTime startTime, LocalDateTime endTime, AuctionStatus auctionStatus,
                           Long sellerId, String sellerUsername) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startingPrice = startingPrice;
        this.currentPrice = currentPrice;
        this.startTime = startTime;
        this.endTime = endTime;
        this.auctionStatus = auctionStatus;
        this.sellerId = sellerId;
        this.sellerUsername = sellerUsername;
    }
}
