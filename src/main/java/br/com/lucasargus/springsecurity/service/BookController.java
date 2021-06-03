package br.com.lucasargus.springsecurity.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.lucasargus.springsecurity.model.Book;
import br.com.lucasargus.springsecurity.repository.BookRepository;

@RestController
@Validated
public class BookController {

    @Autowired
    private BookRepository repository;

    // Find
    @GetMapping("/books")
    @PreAuthorize("hasAuthority('admin')")
    public List<Book> findAll() {
      
    	return repository.findAll();
    }


    // Save or update
    @PutMapping("/books/{id}")
    @PreAuthorize("hasAuthority('admin') or hasAuthority('usuario')")
    public Book saveOrUpdate(@RequestBody Book newBook, @PathVariable Long id) {

        return repository.findById(id)
                .map(x -> {
                    x.setName(newBook.getName());
                    x.setAuthor(newBook.getAuthor());
                    x.setPrice(newBook.getPrice());
                    return repository.save(x);
                })
                .orElseGet(() -> {
                    newBook.setId(id);
                    return repository.save(newBook);
                });
    }

    @DeleteMapping("/books/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public void deleteBook(@PathVariable Long id) {
        repository.deleteById(id);
    }

}
