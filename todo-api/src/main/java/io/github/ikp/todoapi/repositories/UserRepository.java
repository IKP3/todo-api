package io.github.ikp.todoapi.repositories;

import io.github.ikp.todoapi.domain.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long>,
    PagingAndSortingRepository<UserEntity, Long> {
}
