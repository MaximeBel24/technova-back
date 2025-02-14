package fr.doranco.ecom_backend.repositories;

import fr.doranco.ecom_backend.enums.RoleName;
import fr.doranco.ecom_backend.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRoleName(RoleName roleName);
}

