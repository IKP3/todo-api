package io.github.ikp.todoapi.repositories;

import io.github.ikp.todoapi.domain.entities.TaskEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TaskRepository extends CrudRepository<TaskEntity, Long>,
    PagingAndSortingRepository<TaskEntity, Long> {

  Optional<TaskEntity> findByUser_IdAndId( @Param("userId") Long userId,@Param("taskId") Long taskId);

  List<TaskEntity> findAllByUser_Id(@Param("userId") Long userId);

  Page<TaskEntity> findAllByUser_Id(@Param("userId") Long userId, Pageable pageable);

  Boolean existsByUser_IdAndId(Long userId, Long taskId);

  @Transactional
  void deleteByUser_IdAndId(Long userId, Long taskId);
}
