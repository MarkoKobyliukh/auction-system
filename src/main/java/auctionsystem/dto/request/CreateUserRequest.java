package auctionsystem.dto.request;

import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
public class CreateUserRequest {

    private String username;
    private String email;

    public CreateUserRequest() {}

    public CreateUserRequest(String username, String email) {
        this.username = username;
        this.email = email;

    }


}
