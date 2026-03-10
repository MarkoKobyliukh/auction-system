package auctionsystem.exception;

public class UnauthorizedAuctionActionException extends RuntimeException {
    public UnauthorizedAuctionActionException(String message) {
        super(message);
    }
}
