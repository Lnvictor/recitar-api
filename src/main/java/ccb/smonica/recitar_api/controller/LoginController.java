package ccb.smonica.recitar_api.controller;

import ccb.smonica.recitar_api.dto.PostLoginRequestBodyDTO;
import ccb.smonica.recitar_api.dto.PostLoginResponseDTO;
import ccb.smonica.recitar_api.entities.User;
import ccb.smonica.recitar_api.repository.UserRepository;
import ccb.smonica.recitar_api.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
public class LoginController {
    @Autowired
    private UserRepository repository;
    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<PostLoginResponseDTO> doLogin(@RequestBody PostLoginRequestBodyDTO body) {
        String username = body.username();
        UserDetails user = repository.findByUsername(username);

        if (!(new BCryptPasswordEncoder().matches(body.password(), user.getPassword()))) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(403));
        }

        try {
            String token = tokenService.generateJwtToken(username);
            return ResponseEntity.ok(new PostLoginResponseDTO(token));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.badRequest().build();
    }
}
