package group.spring.services.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import group.spring.services.login.model.Roles;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long>{
    Roles findByName(String name);
}
