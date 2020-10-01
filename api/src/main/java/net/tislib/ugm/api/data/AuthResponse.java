package net.tislib.ugm.api.data;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class AuthResponse {
    private String token;

    private UserDetails user;
}
