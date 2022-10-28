package ru.umar.booklibrary.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.umar.booklibrary.models.Book;
import ru.umar.booklibrary.models.Person;
import ru.umar.booklibrary.repositories.PersonRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PersonService {
    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Optional<Person> findOneByEmail(String email) {
        return personRepository.findByEmail(email);
    }

    public List<Book> getBooksByPersonId(int id) {
        Optional<Person> person = personRepository.findById(id);

        if (person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());
            // We're iterating through the books below, so they'll be loaded for sure, but just in case
            // does not interfere with always calling Hibernate.initialize()
            // (in case, for example, if the code changes later and the book iteration is deleted)

            // Checking for expired books
            person.get().getBooks().forEach(book -> {
                long diffInMillies = Math.abs(book.getTakenAt().getTime() - new Date().getTime());
                // 864000000 milliseconds = 10 days
                if (diffInMillies > 864000000)
                    book.setExpired(true); // book expired
            });

            return person.get().getBooks();
        }
        else {
            return Collections.emptyList();
        }
    }

    public Person findOne(int id){
        Optional<Person> foundPerson = personRepository.findById(id);

        return foundPerson.orElse(null);
    }

    @Transactional
    public void save(Person person){
        person.setCreatedAt(new Date());
        personRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson){
        Person person = personRepository.findPersonById(id);
        person.setUsername(updatedPerson.getUsername());
        person.setEmail(updatedPerson.getEmail());
        person.setPhoneNumber(updatedPerson.getPhoneNumber());
        person.setDateOfBirth(updatedPerson.getDateOfBirth());
        person.setRole(updatedPerson.getRole());
    }

    @Transactional
    public void  delete(int id){
        personRepository.deleteById(id);
    }
}
