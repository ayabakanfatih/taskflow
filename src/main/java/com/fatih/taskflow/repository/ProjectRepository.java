package com.fatih.taskflow.repository;

import com.fatih.taskflow.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository
        extends JpaRepository<Project, Long> {
}
