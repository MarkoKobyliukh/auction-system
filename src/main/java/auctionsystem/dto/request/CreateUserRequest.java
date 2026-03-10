package auctionsystem.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
public class CreateUserRequest {

    @NotBlank
    private String username;
    @Email
    @NotBlank
    private String email;

    public CreateUserRequest() {}

    public CreateUserRequest(String username, String email) {
        this.username = username;
        this.email = email;

    }


}
