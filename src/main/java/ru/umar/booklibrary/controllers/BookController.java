package ru.umar.booklibrary.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.umar.booklibrary.models.Person;
import ru.umar.booklibrary.services.BookService;
import ru.umar.booklibrary.services.PersonService;


@Controller
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;


    private final PersonService personService;

    public BookController(BookService bookService, PersonService personService) {
        this.bookService = bookService;
        this.personService = personService;
    }

    @GetMapping()
    public String showAllBook(Model model, @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(value = "sort_by_year", required = false) boolean sortByYear) {

        if (page == null || booksPerPage == null)
            model.addAttribute("book", bookService.findAll(sortByYear)); // выдача всех книг
        else
            model.addAttribute("book", bookService.findWithPagination(page, booksPerPage, sortByYear));

        return "book/book";
    }

    @GetMapping("/{id}")
    public String showOneBook(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person){
        model.addAttribute("book", bookService.findOne(id));

        Person bookOwner = bookService.findBooksByPersonId(id);

        if (bookOwner != null)
            model.addAttribute("owner", bookOwner);
        else
            model.addAttribute("people", personService.findAll());

        return "book/show";
    }

    @GetMapping("/search")
    public String searchPage() {
        return "book/search";
    }


    @PostMapping("/search")
    public String makeSearch(Model model, @RequestParam("query") String query) {
        model.addAttribute("books", bookService.searchByTitle(query));
        return "book/search";
    }

}
