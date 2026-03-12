package auctionsystem.controller;

import auctionsystem.dto.response.AuctionCloseResponse;
import auctionsystem.dto.response.AuctionResponse;
import auctionsystem.entity.AuctionStatus;
import auctionsystem.service.AuctionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import auctionsystem.exception.GlobalExceptionHandler;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuctionController.class)
@Import(GlobalExceptionHandler.class)
class AuctionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuctionService auctionService;

    @Test
    @DisplayName("POST /api/auctions should return 201 and auction JSON")
    void createAuction_shouldReturnCreated() throws Exception {
        AuctionResponse response = new AuctionResponse(
                1L,
                "Laptop",
                "Gaming laptop",
                500.0,
                500.0,
                LocalDateTime.of(2026, 3, 11, 12, 0),
                LocalDateTime.of(2026, 3, 20, 12, 0),
                AuctionStatus.ACTIVE,
                10L,
                "sellerUser"
        );

        when(auctionService.createAuction(org.mockito.ArgumentMatchers.any()))
                .thenReturn(response);

        String requestBody = """
                {
                  "title": "Laptop",
                  "description": "Gaming laptop",
                  "startingPrice": 500,
                  "endTime": "2026-03-20T12:00:00",
                  "sellerId": 10
                }
                """;

        mockMvc.perform(post("/api/auctions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.id").value(1))
                        .andExpect(jsonPath("$.title").value("Laptop"))
                        .andExpect(jsonPath("$.auctionStatus").value("ACTIVE"))
                        .andExpect(jsonPath("$.sellerId").value(10))
                        .andExpect(jsonPath("$.sellerUsername").value("sellerUser"));
    }

    @Test
    @DisplayName("GET /api/auctions/{id} should return 200 and auction JSON")
    void getAuctionById_shouldReturnOk() throws Exception {
        AuctionResponse response = new AuctionResponse(
                1L,
                "Phone",
                "Flagship phone",
                1000.0,
                1200.0,
                LocalDateTime.of(2026, 3, 11, 10, 0),
                LocalDateTime.of(2026, 3, 25, 10, 0),
                AuctionStatus.ACTIVE,
                20L,
                "ownerUser"
        );

        when(auctionService.getAuctionById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/auctions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Phone"))
                .andExpect(jsonPath("$.currentPrice").value(1200.0))
                .andExpect(jsonPath("$.sellerUsername").value("ownerUser"));
    }

    @Test
    @DisplayName("PATCH /api/auctions/{id}/close should return 200 and close result JSON")
    void closeAuction_shouldReturnOk() throws Exception {
        AuctionCloseResponse response = new AuctionCloseResponse(
                1L,
                "Camera",
                1500.0,
                AuctionStatus.CLOSED,
                99L,
                "winnerUser"
        );

        when(auctionService.closeAuctionById(1L)).thenReturn(response);

        mockMvc.perform(patch("/api/auctions/1/close"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.auctionId").value(1))
                .andExpect(jsonPath("$.title").value("Camera"))
                .andExpect(jsonPath("$.finalPrice").value(1500.0))
                .andExpect(jsonPath("$.auctionStatus").value("CLOSED"))
                .andExpect(jsonPath("$.winnerId").value(99))
                .andExpect(jsonPath("$.winnerUsername").value("winnerUser"));
    }

    @Test
    @DisplayName("POST /api/auctions should return 400 when validation fails")
    void createAuction_shouldReturnBadRequest_whenRequestIsInvalid() throws Exception {
        String invalidRequestBody = """
                {
                  "title": "",
                  "description": "",
                  "startingPrice": -5,
                  "endTime": null,
                  "sellerId": null
                }
                """;

        mockMvc.perform(post("/api/auctions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestBody))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.status").value(400))
                        .andExpect(jsonPath("$.error").value("Bad Request"))
                        .andExpect(jsonPath("$.path").value("/api/auctions"));
    }
}