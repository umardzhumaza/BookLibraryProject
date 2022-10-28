package ru.umar.booklibrary.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.umar.booklibrary.models.Person;
import ru.umar.booklibrary.services.PersonValidateService;

@Component
public class PersonValidator implements Validator {
    private final PersonValidateService personValidateService;

    @Autowired
    public PersonValidator(PersonValidateService personValidateService) {
        this.personValidateService = personValidateService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        try {
            personValidateService.getPerson(person).orElseThrow(Exception::new);
        } catch (Exception e) {
            return;
        }
        errors.rejectValue("username", "", "Person with that name already exists");
    }
}
