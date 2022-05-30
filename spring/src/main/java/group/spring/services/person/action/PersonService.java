package group.spring.services.person.action;

import java.util.List;

import group.spring.services.person.model.Person;

public interface PersonService {
    public Person savePerson(Person person);
    public Person getPerson(Long id);
    public List<Person> getPeople();
    public void removePerson(Long id);
}
