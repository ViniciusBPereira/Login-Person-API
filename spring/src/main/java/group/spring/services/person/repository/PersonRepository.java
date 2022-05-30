package group.spring.services.person.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import group.spring.services.person.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{
    
}
