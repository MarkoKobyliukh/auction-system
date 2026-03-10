package auctionsystem.exception;

public class AuctionIsClosedException extends RuntimeException {
    public AuctionIsClosedException(String message) {
        super(message);
    }
}
