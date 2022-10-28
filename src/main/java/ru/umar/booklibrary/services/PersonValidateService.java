package ru.umar.booklibrary.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.umar.booklibrary.models.Person;
import ru.umar.booklibrary.repositories.PersonRepository;

import java.util.Optional;

@Service
public class PersonValidateService {
    private final PersonRepository personRepository;

    @Autowired
    public PersonValidateService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }


    public Optional<Person> getPerson(Person person){
        Optional<Person> optionalPerson = personRepository.findByUsername(person.getUsername());
        return optionalPerson;
    }

}
