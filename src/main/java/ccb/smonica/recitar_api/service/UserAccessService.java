package ccb.smonica.recitar_api.service;

import ccb.smonica.recitar_api.dto.UserAccessDTO;
import ccb.smonica.recitar_api.entities.Roles;
import ccb.smonica.recitar_api.entities.User;
import ccb.smonica.recitar_api.enums.RoleName;
import ccb.smonica.recitar_api.exception.RoleNotFoundException;
import ccb.smonica.recitar_api.exception.UserNotFoundException;
import ccb.smonica.recitar_api.repository.RolesRepository;
import ccb.smonica.recitar_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAccessService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RolesRepository rolesRepository;

    public List<UserAccessDTO> getUserAccessDTO(String username) {
        UserDetails details = this.userRepository.findByUsername(username);
        List<Roles> roles = ((User) details).getRoles();
        return roles.stream().map(
                role -> new UserAccessDTO(role.getRoleName(), username)
        ).toList();
    }

    public void setRoleToUser(String username, RoleName role) {
        UserDetails details = this.userRepository.findByUsername(username);
        Roles r = this.rolesRepository.findByRoleName(role);

        doChecks(details, r);
        User user = (User) details;
        user.addRole(r);
        this.userRepository.save(user);
    }

    public void removeAccessFromUsername(String username, RoleName role) {
        UserDetails details = this.userRepository.findByUsername(username);
        Roles r = this.rolesRepository.findByRoleName(role);
        doChecks(details, r);

        User user = (User) details;
        user.removeRole(r);
        this.userRepository.save(user);
    }

    private void doChecks(UserDetails details, Roles role) {
        if (details == null) {
            throw new UserNotFoundException();
        }

        if (role == null) {
            throw new RoleNotFoundException();
        }
    }
}
