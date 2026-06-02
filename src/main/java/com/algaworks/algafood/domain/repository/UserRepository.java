package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CustomJpaRepository<User, Long>{
}
