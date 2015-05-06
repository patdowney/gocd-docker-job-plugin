/*************************GO-LICENSE-START*********************************
* Copyright 2014 ThoughtWorks, Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*************************GO-LICENSE-END***********************************/

package name.patdowney.gocd.plugin.task.docker.cleanup;

import com.thoughtworks.go.plugin.api.task.*;

import com.spotify.docker.client.*;
import com.spotify.docker.client.messages.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DockerCleanupTaskExecutor {
    public Result execute(Config config, Context context, JobConsoleLogger console) { // throws Exception {
        try {
            return runImage(context, config, console);
        } catch (Exception e) {
            return new Result(false, "Failed to cleanup instance: " + context.getPipelineDescription(), e);
        }
    }

    private Result runImage(Context taskContext, Config taskConfig, JobConsoleLogger console) throws DockerException, DockerCertificateException, InterruptedException {
	String id = taskContext.getPipelineDescription();

        DockerClient docker = DefaultDockerClient.fromEnv().build();

        // Kill container
        docker.killContainer(id);

        // Remove container
        docker.removeContainer(id);

        return new Result(true, "Image killed and removed: " + id );
    }

}
