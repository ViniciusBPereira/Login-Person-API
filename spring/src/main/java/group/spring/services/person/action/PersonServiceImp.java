package group.spring.services.person.action;

import java.net.URI;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import group.spring.services.person.controller.PersonController;
import group.spring.services.person.model.Location;
import group.spring.services.person.model.Person;
import group.spring.services.person.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class PersonServiceImp implements PersonService{

    @Autowired
    private PersonRepository personRepository;

    private Person getLocation(Person person){
        RestTemplate request = new RestTemplate();
        ResponseEntity<Location> locale = request.getForEntity(URI.create("https://viacep.com.br/ws/" + person.getCep() + "/json/"), Location.class);
        person.setLocation(locale.getBody());
        
        return person;
    }

    @Override
    public Person savePerson(Person person) {
        log.info("Save Person");
        return personRepository.save(getLocation(person));
    }

    @Override
    public Person getPerson(Long id) {
        log.info("Get Person");
        Link link = WebMvcLinkBuilder.linkTo(PersonController.class).slash("").withSelfRel();
        Person person = personRepository.findById(id).get();
        person.add(link);
        return person;
    }

    @Override
    public List<Person> getPeople() {
        log.info("Get all People");
        List<Person> list = personRepository.findAll();
        for (Person person : list) {
            Link link = WebMvcLinkBuilder.linkTo(PersonController.class).slash(person.getId()).withSelfRel();
            person.add(link);
        }
        return list;
    }

    @Override
    public void removePerson(Long id) {
        log.info("Remove Person");
        personRepository.deleteById(id);
        
    }

}
