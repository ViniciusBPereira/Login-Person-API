package group.spring.services.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import group.spring.services.login.model.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long>{
    Users findByUsername(String username);
}
