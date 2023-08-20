package ma.jwttoken.JWTtokens.dto;

import lombok.Data;

@Data
public class AuthResponseDto {
    private String accessToken;
    private String tokenType = "bearer ";

    public AuthResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
