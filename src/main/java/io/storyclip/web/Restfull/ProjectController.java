package io.storyclip.web.Restfull;

import io.storyclip.web.Common.Common;
import io.storyclip.web.Common.JWTManager;
import io.storyclip.web.Common.ProjectManager;
import io.storyclip.web.Entity.Project;
import io.storyclip.web.Entity.Result;
import io.storyclip.web.Exception.ParamRequiredException;
import io.storyclip.web.Repository.ProjectRepository;
import io.storyclip.web.Repository.TokenRepository;
import io.storyclip.web.Repository.UserRepository;
import io.storyclip.web.Type.Http;
import org.springframework.web.bind.annotation.*;

import java.beans.ConstructorProperties;
import java.util.HashMap;

@RestController
@RequestMapping(value="/")
public class ProjectController {

    // Autowired 대신 추천되는 의존성 주입 방식
    private static ProjectRepository ProjectRepo;

    @ConstructorProperties({"ProjectRepository"})
    public ProjectController(ProjectRepository ProjectRepo) {
        this.ProjectRepo = ProjectRepo;
    }

    @PostMapping("/project/new")
    public Result newProject(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody HashMap<String, Object> param
    ) throws Exception {
        Result result = new Result();
        Project project = new Project();

        String title = (String) param.get("title");
        String description = (String) param.get("description");

        if(title == null) {
            throw new ParamRequiredException(null);
        }

        HashMap<String, Object> info = JWTManager.read(token);

        project.setProjectId(ProjectManager.newKey());
        project.setUserId((Integer) info.get("id"));
        project.setTitle(title);
        project.setDescription(description);

        result.setSuccess(true);
        result.setMessage(Http.OK);
        result.setResult(ProjectRepo.save(project));

        return result;
    }

    @GetMapping(value={"/projects", "/project/list"})
    public Result getProjectList(@RequestHeader(value = "Authorization") String token) throws Exception {
        Result result = new Result();
        HashMap<String, Object> info = JWTManager.read(token);

        result.setSuccess(true);
        result.setMessage(Http.OK);
        result.setResult(ProjectRepo.findAllByUserId((Integer) info.get("id")));

        return result;
    }

    @GetMapping(value="/PJ{projectId}")
    public Result getProjectInfo(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable String projectId
    ) throws Exception {
        Result result = new Result();
        HashMap<String, Object> info = JWTManager.read(token);

        result.setSuccess(true);
        result.setMessage(Http.OK);
        result.setResult(ProjectRepo.findProjectByProjectIdAndUserId(projectId, (Integer) info.get("id")));

        return result;
    }
}
