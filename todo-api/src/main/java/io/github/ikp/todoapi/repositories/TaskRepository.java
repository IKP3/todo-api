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

@Repository
public interface TaskRepository extends CrudRepository<TaskEntity, Long>,
    PagingAndSortingRepository<TaskEntity, Long> {
  @Query("select t from TaskEntity t where t.id = :taskId and t.user.id = :userId")
  Optional<TaskEntity> findByIdAndUserId( @Param("userId") Long userId,@Param("taskId") Long taskId);

  @Query("select t from TaskEntity t where t.user.id = :userId")
  List<TaskEntity> findByUserId(@Param("userId") Long userId);

  @Query("select t from TaskEntity t where t.user.id = :userId")
  Page<TaskEntity> findByUserId(@Param("userId") Long userId, Pageable pageable);

  Boolean existsByUser_IdAndId(Long userId, Long taskId);
}
