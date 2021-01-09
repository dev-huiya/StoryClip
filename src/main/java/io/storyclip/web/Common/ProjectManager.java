package io.storyclip.web.Common;

import io.storyclip.web.Repository.ProjectRepository;
import org.springframework.stereotype.Component;

import java.beans.ConstructorProperties;

@Component
public class ProjectManager {

    // Autowired 대신 추천되는 의존성 주입 방식
    private static ProjectRepository ProjectRepo;

    @ConstructorProperties({"ProjectRepository"})
    public ProjectManager(ProjectRepository ProjectRepo) {
        this.ProjectRepo = ProjectRepo;
    }

    /**
     * project key 생성하는 메소드.
     *
     * @return 유니크한 project id
     */
    public static String newKey() {
        String projectId = "";
        do {
            projectId = Common.createRandom(16);
        } while (ProjectRepo.countProjectByProjectId(projectId) != 0);

        return projectId;
    }

}
