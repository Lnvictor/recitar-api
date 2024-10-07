package ccb.smonica.recitar_api.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import ccb.smonica.recitar_api.dto.OIDCProviderTokenResponseDTO;
import ccb.smonica.recitar_api.dto.PostLoginResponseDTO;
import ccb.smonica.recitar_api.exception.OAuthFlowException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String jwtSecret;

    @Value("${api.security.oauth.microsoft.token_url}")
    private String microsoftTokenUrl;

    @Value("${api.security.oauth.microsoft.redirect_uri}")
    private String redirectUri;

    @Value("${api.security.oauth.microsoft.client_id}")
    private String clientId;

    @Value("${api.security.oauth.microsoft.client_secret}")
    private String clientSecret;

    public String generateJwtToken(String userName) {
        Algorithm alg = Algorithm.HMAC256(jwtSecret);
        return JWT.create()
                .withIssuer("recitar-api")
                .withSubject(userName)
                .withExpiresAt(
                        LocalDateTime.now()
                                .plusHours(3)
                                .toInstant(ZoneOffset.of("-03:00")))
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

    public PostLoginResponseDTO generateAuthCodeByOAuthCode(String code) {
        String username = this.getUsernameFromMicrosoftToken(code);
        String ourToken = this.generateJwtToken(username);

        return new PostLoginResponseDTO(ourToken, 10800);
    }

    public String getUsernameFromMicrosoftToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("code", code);
        params.add("redirect_uri", this.redirectUri);
        params.add("grant_type", "authorization_code");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(this.clientId, this.clientSecret);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(
                params, headers);

        try {
            OIDCProviderTokenResponseDTO responseEntity = restTemplate.postForObject(new URI(microsoftTokenUrl),
                    entity, OIDCProviderTokenResponseDTO.class);

            String accessToken = responseEntity.accessToken();

            return JWT.decode(accessToken).getClaim("email").asString();

        } catch (RestClientException e) {
            e.printStackTrace();
            throw new OAuthFlowException("Something went wrong trying to connect to provider API");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new OAuthFlowException("The URL provided is incorrect");
        }
    }
}
