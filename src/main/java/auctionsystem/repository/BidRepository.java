package auctionsystem.repository;

import auctionsystem.entity.Auction;
import auctionsystem.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {

    public List<Bid> findByAuction(Auction auction);
}
