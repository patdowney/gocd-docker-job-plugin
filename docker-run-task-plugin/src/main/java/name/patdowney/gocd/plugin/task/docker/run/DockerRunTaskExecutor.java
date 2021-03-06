/*************************
 * GO-LICENSE-START*********************************
 * Copyright 2014 ThoughtWorks, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ************************GO-LICENSE-END
 ***********************************/

package name.patdowney.gocd.plugin.task.docker.run;

import com.google.common.collect.ImmutableList;
import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerCertificateException;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.HostConfig;
import com.thoughtworks.go.plugin.api.task.JobConsoleLogger;
import name.patdowney.gocd.plugin.common.Context;
import name.patdowney.gocd.plugin.common.Result;

public class DockerRunTaskExecutor {
    public Result execute(Config config, Context context, JobConsoleLogger console) {
        try {
            return runImage(context, config, console);
        } catch (Exception e) {
            return new Result(false, "Failed to run image: " + config.getImageName(), e);
        }
    }

    private Result runImage(Context taskContext, Config taskConfig, JobConsoleLogger console) throws DockerException, DockerCertificateException, InterruptedException {

        console.printEnvironment(taskContext.getEnvironmentVariables());

        final String containerBuildDir = "/build";


        DockerClient docker = DefaultDockerClient.fromEnv().build();
        //DockerClient docker = new DefaultDockerClient("unix:///var/run/docker.sock");

        final ImmutableList.Builder<String> binds = ImmutableList.builder();
        binds.add(String.format("%s:%s", taskContext.getAbsoluteWorkingDir(), containerBuildDir));

        // Start container
        HostConfig hostConfig = HostConfig.builder()
                .binds(binds.build())
                .build();

        //console.printLine("Configuring container with image: " + taskConfig.getImageName());
        ContainerConfig containerConfig = ContainerConfig.builder()
                .hostConfig(hostConfig)
                .image(taskConfig.getImageName())
                .workingDir(containerBuildDir)
                .cmd("sleep", "30000")
                .build();

        //console.printLine("Creating container with name: " + taskContext.getPipelineDescription());
        ContainerCreation creation = docker.createContainer(containerConfig, taskContext.getPipelineDescription());
        String id = creation.id();
        //console.printLine("Created container with id: " + id);

        // Inspect container
        // final ContainerInfo info = docker.inspectContainer(id);
        //

        docker.startContainer(id);
        //console.printLine("Started container with id: " + id);

        return new Result(true, "Image started: " + id + "(" + taskConfig.getImageName() + ")");
    }

}
