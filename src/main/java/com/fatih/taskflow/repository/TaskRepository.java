package com.fatih.taskflow.repository;

import com.fatih.taskflow.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import com.fatih.taskflow.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository
        extends JpaRepository<Task, Long> {
long countByProject_Id(Long projectId);

    @Query("select t.project.id, count(t) from Task t where t.project.id in :projectIds group by t.project.id")
    List<Object[]> countByProjectIds(@Param("projectIds") List<Long> projectIds);
 Page<Task> findByStatus(TaskStatus status, Pageable pageable);

    Page<Task> findByProject_Id(Long projectId, Pageable pageable);

    Page<Task> findByStatusAndProject_Id(TaskStatus status, Long projectId, Pageable pageable);
    List<Task> findByProject_Id(Long projectId);

    Page<Task> findByOwner_Id(Long ownerId, Pageable pageable);

    Page<Task> findByOwner_IdAndStatus(Long ownerId, TaskStatus status, Pageable pageable);

    Page<Task> findByOwner_IdAndProject_Id(Long ownerId, Long projectId, Pageable pageable);

    Page<Task> findByOwner_IdAndStatusAndProject_Id(
            Long ownerId, TaskStatus status, Long projectId, Pageable pageable);

    Optional<Task> findByIdAndOwner_Id(Long id, Long ownerId);

    List<Task> findByProject_IdAndOwner_Id(Long projectId, Long ownerId);

    long countByProject_IdAndOwner_Id(Long projectId, Long ownerId);
}
