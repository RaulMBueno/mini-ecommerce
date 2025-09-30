package com.raulmbueno.mini_ecommerce.repositories;

import com.raulmbueno.mini_ecommerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// @Repository: Indica que esta interface é um componente de acesso a dados.
// JpaRepository<User, Long>:
//   - User: É a entidade que este repositório vai gerenciar.
//   - Long: É o tipo da chave primária da entidade User (o 'id').
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}