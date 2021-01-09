package io.storyclip.web.Repository;

import io.storyclip.web.Entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, String> {

    Integer countProjectByProjectId(String projectId);

    List<Project> findAllByUserId(Integer userId);

    Project findProjectByProjectIdAndUserId(String projectId, Integer userId);
}
