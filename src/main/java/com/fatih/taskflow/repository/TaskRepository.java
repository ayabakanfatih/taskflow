package com.fatih.taskflow.repository;

import com.fatih.taskflow.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import com.fatih.taskflow.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface TaskRepository
        extends JpaRepository<Task, Long> {
 Page<Task> findByStatus(TaskStatus status, Pageable pageable);

    Page<Task> findByProject_Id(Long projectId, Pageable pageable);

    Page<Task> findByStatusAndProject_Id(TaskStatus status, Long projectId, Pageable pageable);
    List<Task> findByProject_Id(Long projectId);
}
