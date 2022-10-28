package ru.umar.booklibrary.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.umar.booklibrary.models.Person;
import ru.umar.booklibrary.services.BookService;
import ru.umar.booklibrary.services.PersonService;
import ru.umar.booklibrary.util.PersonValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class PersonController {

    private final PersonService personService;
    private final PersonValidator personValidator;

    private final BookService bookService;

    public PersonController(PersonService personService, PersonValidator personValidator, BookService bookService) {
        this.personService = personService;
        this.personValidator = personValidator;
        this.bookService = bookService;
    }

    @GetMapping()
    public String index(Model model, @ModelAttribute Person person){
        //Получим всех людей из DAO и отобразим людей в представление
        model.addAttribute("person", personService.findAll());
        return "adminPeople/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model){
        //Получим одного еловека по его айди из DAO и передадим на отображение на представление
        model.addAttribute("person", personService.findOne(id));
        model.addAttribute("book", personService.getBooksByPersonId(id));
        return "adminPeople/show";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id){
        model.addAttribute("person", personService.findOne(id));
        return "adminPeople/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, @PathVariable("id") int id){

        if(bindingResult.hasErrors())
            return "adminPeople/edit";

        personService.update(id, person);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        personService.delete(id);
        return "redirect:/admin";
    }
}
