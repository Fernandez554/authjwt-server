package com.nttbank.microservices.authjwtserver.repository;

import com.nttbank.microservices.authjwtserver.model.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IUserRepo extends CrudRepository<UserEntity, String>{
    Optional<UserEntity> findByUsername(String username);
}
