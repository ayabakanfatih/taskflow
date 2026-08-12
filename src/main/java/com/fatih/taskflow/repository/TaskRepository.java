package com.fatih.taskflow.repository;

import com.fatih.taskflow.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import com.fatih.taskflow.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository
        extends JpaRepository<Task, Long> {
long countByProject_Id(Long projectId);

    @Query("select t.project.id, count(t) from Task t where t.project.id in :projectIds group by t.project.id")
    List<Object[]> countByProjectIds(@Param("projectIds") List<Long> projectIds);
 Page<Task> findByStatus(TaskStatus status, Pageable pageable);

    Page<Task> findByProject_Id(Long projectId, Pageable pageable);

    Page<Task> findByStatusAndProject_Id(TaskStatus status, Long projectId, Pageable pageable);
    List<Task> findByProject_Id(Long projectId);
}
