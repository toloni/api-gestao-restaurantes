package br.com.toloni.gestaorestaurantes.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.toloni.gestaorestaurantes.domain.TipoUsuario;
import br.com.toloni.gestaorestaurantes.domain.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {

  boolean existsByEmail(String email);

  boolean existsByLogin(String login);

  Page<Usuario> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

  Page<Usuario> findByTipo(TipoUsuario tipo, Pageable pageable);

  Optional<Usuario> findByLogin(String login);

  boolean existsByEmailAndIdNot(String email, Long id);

  boolean existsByLoginAndIdNot(String login, Long id);

}
