package com.fatih.taskflow.service;

import com.fatih.taskflow.dto.CreateProjectRequest;
import com.fatih.taskflow.exception.ProjectNotFoundException;
import com.fatih.taskflow.model.Project;
import com.fatih.taskflow.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fatih.taskflow.dto.ProjectResponse;
import com.fatih.taskflow.repository.TaskRepository;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public ProjectService(
            ProjectRepository projectRepository,
            TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
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
@Transactional(readOnly = true)
    public List<ProjectResponse> getProjectResponses() {
        List<Project> projects = projectRepository.findAll();

        List<Long> projectIds = projects.stream()
                .map(Project::getId)
                .toList();

        Map<Long, Long> countsByProjectId = new HashMap<>();
        if (!projectIds.isEmpty()) {
            for (Object[] row : taskRepository.countByProjectIds(projectIds)) {
                countsByProjectId.put((Long) row[0], (Long) row[1]);
            }
        }

        return projects.stream()
                .map(project -> new ProjectResponse(
                        project.getId(),
                        project.getName(),
                        countsByProjectId.getOrDefault(project.getId(), 0L)
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProjectResponseById(Long id) {
        Project project = getProjectById(id);

        return new ProjectResponse(
                project.getId(),
                project.getName(),
                taskRepository.countByProject_Id(project.getId())
        );
    }

}
