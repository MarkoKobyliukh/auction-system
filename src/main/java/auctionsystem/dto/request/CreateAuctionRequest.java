package auctionsystem.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateAuctionRequest {

    private String title;
    private String description;
    private double startingPrice;
    private LocalDateTime endTime;
    private Long sellerId;

    public CreateAuctionRequest() {}

    public CreateAuctionRequest(String title, String description, double startingPrice,
                             LocalDateTime endTime, Long sellerId){
        this.title = title;
        this.description = description;
        this.startingPrice = startingPrice;
        this.endTime = endTime;
        this.sellerId = sellerId;
    }

}
