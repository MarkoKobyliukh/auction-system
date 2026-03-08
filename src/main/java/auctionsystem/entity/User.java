package auctionsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private LocalDateTime createdAt;

    public User(Long id, String username, String email, LocalDateTime createdAt){
        this.id = id;
        this.username = username;
        this.email = email;
        this.createdAt = createdAt;
    }

    public User(){}

}
