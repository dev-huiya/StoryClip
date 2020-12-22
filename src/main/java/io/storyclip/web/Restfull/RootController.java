package io.storyclip.web.Restfull;

import io.storyclip.web.Entity.Result;
import io.storyclip.web.Type.Type;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping(value="/")
public class RootController {

    @Value("${storyClip.version}")
    private String projectVersion;

    @RequestMapping(value={"/", "/status"})
    public Result index() {
        Result result = new Result();
        result.setSuccess(true);
        result.setMessage(Type.OK);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("isServerRun", true);
        hashMap.put("version", projectVersion);
        result.setResult(hashMap);
        return result;
    }
}
