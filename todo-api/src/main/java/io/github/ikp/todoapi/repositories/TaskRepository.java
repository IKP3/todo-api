package io.github.ikp.todoapi.repositories;

import io.github.ikp.todoapi.domain.entities.TaskEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends CrudRepository<TaskEntity, Long>,
    PagingAndSortingRepository<TaskEntity, Long> {

}
