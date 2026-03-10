package auctionsystem.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceBidRequest {

    @NotNull
    private Long auctionId;
    @NotNull
    private Long bidderId;
    @Positive
    private double amount;

    public PlaceBidRequest() {}

    public PlaceBidRequest(Long auctionId, Long bidderId, double amount) {
        this.auctionId = auctionId;
        this.bidderId = bidderId;
        this.amount = amount;
    }
}
