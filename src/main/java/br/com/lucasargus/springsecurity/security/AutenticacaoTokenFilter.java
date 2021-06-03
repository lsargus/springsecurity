package br.com.lucasargus.springsecurity.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.lucasargus.springsecurity.model.User;
import br.com.lucasargus.springsecurity.repository.UserRepository;

public class AutenticacaoTokenFilter extends OncePerRequestFilter {

	private TokenService tokenService;
	private UserRepository userRepository;

	public AutenticacaoTokenFilter(TokenService tokenService, UserRepository userRepository) {
		this.tokenService = tokenService;
		this.userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = recuperaToken(request);

		boolean valido = tokenService.isTokenValido(token);

		if (valido)
			autenticarCliente(token);

		filterChain.doFilter(request, response);

	}

	private String recuperaToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if (token == null || token.isEmpty() || !token.startsWith("Bearer "))
			return null;
		else if (token.startsWith("Bearer ")) {
			token = token.replaceFirst("Bearer ", "");
		}

		return token;
	}

	private void autenticarCliente(String token) {
		Long idUser = tokenService.getIdUsuario(token);
		Optional<User> optUser = userRepository.findById(idUser);

		User user = optUser.isPresent() ? optUser.get() : null;
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null,
				user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

}
