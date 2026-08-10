package com.fatih.taskflow.service;

import com.fatih.taskflow.dto.CreateProjectRequest;
import com.fatih.taskflow.exception.ProjectNotFoundException;
import com.fatih.taskflow.model.Project;
import com.fatih.taskflow.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Transactional
    public Project createProject(CreateProjectRequest request) {

        Project project = new Project(request.getName());

        return projectRepository.save(project);
    }

    @Transactional(readOnly = true)
    public List<Project> getProjects() {
        return projectRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Project getProjectById(Long id) {

        return projectRepository
                .findById(id)
                .orElseThrow(
                        () -> new ProjectNotFoundException(id)
                );
    }
}
