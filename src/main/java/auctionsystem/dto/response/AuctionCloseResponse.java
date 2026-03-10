package auctionsystem.dto.response;

import auctionsystem.entity.AuctionStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuctionCloseResponse {

    private Long auctionId;
    private String title;
    private double finalPrice;
    private AuctionStatus auctionStatus;
    private Long winnerId;
    private String winnerUsername;

    public AuctionCloseResponse() {}

    public AuctionCloseResponse(Long auctionId, String title, double finalPrice,
                                AuctionStatus auctionStatus, Long winnerId, String winnerUsername){
        this.auctionId = auctionId;
        this.title = title;
        this.finalPrice = finalPrice;
        this.auctionStatus = auctionStatus;
        this.winnerId = winnerId;
        this.winnerUsername = winnerUsername;
    }
}
