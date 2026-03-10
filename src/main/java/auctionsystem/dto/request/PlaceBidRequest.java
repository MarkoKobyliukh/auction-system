package auctionsystem.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceBidRequest {

    private Long auctionId;
    private Long bidderId;
    private double amount;

    public PlaceBidRequest() {}

    public PlaceBidRequest(Long auctionId, Long bidderId, double amount) {
        this.auctionId = auctionId;
        this.bidderId = bidderId;
        this.amount = amount;
    }
}
