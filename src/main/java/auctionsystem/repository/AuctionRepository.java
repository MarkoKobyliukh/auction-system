package auctionsystem.repository;

import auctionsystem.entity.Auction;
import auctionsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

    public List<Auction> findBySeller(User seller);
}
