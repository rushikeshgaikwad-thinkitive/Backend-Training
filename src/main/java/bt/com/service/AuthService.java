package bt.com.service;


import bt.com.dto.module.Login;
import bt.com.dto.projection.LoginResponse;

public interface AuthService {

    LoginResponse login(Login request);
}