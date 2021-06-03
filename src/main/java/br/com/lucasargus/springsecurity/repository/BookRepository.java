package br.com.lucasargus.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.lucasargus.springsecurity.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
