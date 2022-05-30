package group.spring.services.person.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import group.spring.services.person.action.PersonService;
import group.spring.services.person.model.Person;

@RequestMapping(value = "/api/v1/person-service")
@RestController
public class PersonController {
    
    @Autowired
    private PersonService personService;

    @PostMapping("/")
    public ResponseEntity<Person> savePerson(@RequestBody Person person){
        return ResponseEntity.ok(personService.savePerson(person));
    }

    @GetMapping("/")
    public ResponseEntity<List<Person>> getPeople(){
        return ResponseEntity.ok(personService.getPeople());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPerson(@PathVariable Long id){
        return ResponseEntity.ok(personService.getPerson(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removePerson(@PathVariable Long id){
        personService.removePerson(id);
        return ResponseEntity.ok().build();
    }

}
