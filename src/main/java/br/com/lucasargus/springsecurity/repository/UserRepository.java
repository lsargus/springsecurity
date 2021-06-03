package br.com.lucasargus.springsecurity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.lucasargus.springsecurity.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	
	public Optional<User> findByLogin(String login);
}
