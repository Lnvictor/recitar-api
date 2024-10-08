package ccb.smonica.recitar_api.controller;

import ccb.smonica.recitar_api.dto.OIDCLoginBodyDTO;
import ccb.smonica.recitar_api.dto.PostLoginRequestBodyDTO;
import ccb.smonica.recitar_api.dto.PostLoginResponseDTO;
import ccb.smonica.recitar_api.repository.UserRepository;
import ccb.smonica.recitar_api.service.TokenService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("login")
@Log4j2
public class LoginController {
    private UserRepository repository;
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<PostLoginResponseDTO> doLogin(@RequestBody PostLoginRequestBodyDTO body) {
        String username = body.username();

        log.info("Trying to log with the username: {}", username);
        UserDetails user = repository.findByUsername(username);

        if (!(new BCryptPasswordEncoder().matches(body.password(), user.getPassword()))) {
            log.debug("Something went wrong comparing passwords of user: {}", username);
            return new ResponseEntity<>(HttpStatusCode.valueOf(403));
        }

        try {
            String token = tokenService.generateJwtToken(username);
            log.debug("Username: {} | Token: {}", username, token);
            return ResponseEntity.ok(new PostLoginResponseDTO(token, 10800));
        } catch (Exception e) {
            log.error("Something went wrong doing authentication process of user: {}", username);
            e.printStackTrace();
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/microsoft")
    public ResponseEntity<PostLoginResponseDTO> loginOIDCWithGoogle(@RequestBody OIDCLoginBodyDTO body){
        return ResponseEntity.ok(tokenService.generateAuthCodeByOAuthCode(body.code()));
    }
}
