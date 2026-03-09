package auctionsystem.service;

import auctionsystem.dto.request.CreateUserRequest;
import auctionsystem.dto.response.UserResponse;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

}
