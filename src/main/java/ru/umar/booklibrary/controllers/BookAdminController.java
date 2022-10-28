package ru.umar.booklibrary.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.umar.booklibrary.models.Book;
import ru.umar.booklibrary.models.Person;
import ru.umar.booklibrary.services.BookService;
import ru.umar.booklibrary.services.PersonService;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/book")
public class BookAdminController {

    private final BookService bookService;

    private final PersonService personService;

    @Autowired
    public BookAdminController(BookService bookService, PersonService personService) {
        this.bookService = bookService;
        this.personService = personService;
    }

    @GetMapping()
    public String showAllBookAdmin(Model model, @RequestParam(value = "page", required = false) Integer page,
                                   @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                                   @RequestParam(value = "sort_by_year", required = false) boolean sortByYear) {

        if (page == null || booksPerPage == null)
            model.addAttribute("book", bookService.findAll(sortByYear)); // выдача всех книг
        else
            model.addAttribute("book", bookService.findWithPagination(page, booksPerPage, sortByYear));
        return "adminBook/booksForAdmin";
    }



    @GetMapping("/{id}")
    public String showBookAdmin(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person){
        model.addAttribute("book", bookService.findOne(id));

        Person bookOwner = bookService.findBooksByPersonId(id);

        if (bookOwner != null)
            model.addAttribute("owner", bookOwner);
        else
            model.addAttribute("people", personService.findAll());

        return "adminBook/showForAdmin";
    }



    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id){
        model.addAttribute("book", bookService.findOne(id));
        return "adminBook/edit";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book){
        return "adminBook/new";
    }

    @PostMapping("/new")
    public String saveBook(@ModelAttribute("book") @Valid Book book,
                           BindingResult bindingResult){

        if(bindingResult.hasErrors())
            return "adminBook/new";

        bookService.save(book);
        return "redirect:/admin/book";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult, @PathVariable("id") int id){

        if(bindingResult.hasErrors())
            return "adminBook/edit";

        bookService.update(id, book);
        return "redirect:/admin/book";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        bookService.delete(id);
        return "redirect:/admin/book";
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson) {
        bookService.assign(id, selectedPerson);
        return "redirect:/admin/book/" + id;
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        bookService.release(id);
        return "redirect:/admin/book/" + id;
    }

    @GetMapping("/search")
    public String searchAdminPage() {
        return "adminBook/searchForAdmin";
    }

    @PostMapping("/search")
    public String makeAdminSearch(Model model, @RequestParam("query") String query) {
        model.addAttribute("books", bookService.searchByTitle(query));
        return "adminBook/searchForAdmin";
    }
}
