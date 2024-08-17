package ccb.smonica.recitar_api.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String jwtSecret;

    public String generateJwtToken(String userName) {
        Algorithm alg = Algorithm.HMAC256(jwtSecret);
        return JWT.create()
                .withIssuer("recitar-api")
                .withSubject(userName)
                .withExpiresAt(
                        LocalDateTime.now()
                                .plusHours(3)
                                .toInstant(ZoneOffset.of("-03:00"))
                )
                .sign(alg);
    }

    public String validateJwtToken(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }

        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        return JWT.require(algorithm)
                .withIssuer("recitar-api")
                .build()
                .verify(token)
                .getSubject();
    }
}
