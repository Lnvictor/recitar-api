package ccb.smonica.recitar_api.controller;

import ccb.smonica.recitar_api.dto.UserAccessDTO;
import ccb.smonica.recitar_api.service.UserAccessService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("access")
@Log4j2
public class AccessManagerController {
    @Autowired
    private UserAccessService service;

    @GetMapping
    public ResponseEntity<List<UserAccessDTO>> access(@RequestParam String username) {
        return ResponseEntity.ok(this.service.getUserAccessDTO(username));
    }

    @PutMapping
    public ResponseEntity<Object> updateAccess(@RequestBody UserAccessDTO userAccessDTO) {
        this.service.setRoleToUser(userAccessDTO.username(), userAccessDTO.role());
        return ResponseEntity.ok(null);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteAccess(@RequestBody UserAccessDTO userAccessDTO) {
        this.service.removeAccessFromUsername(userAccessDTO.username(), userAccessDTO.role());
        return ResponseEntity.ok(null);
    }
}
