package ccb.smonica.recitar_api.repository;

import ccb.smonica.recitar_api.entities.Roles;
import ccb.smonica.recitar_api.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles, Long> {
    Roles findByRoleName(RoleName roleName);
}
