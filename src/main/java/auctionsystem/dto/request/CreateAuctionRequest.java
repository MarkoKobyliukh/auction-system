package auctionsystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateAuctionRequest {

    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @Positive
    private double startingPrice;
    @NotNull
    private LocalDateTime endTime;
    @NotNull
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
