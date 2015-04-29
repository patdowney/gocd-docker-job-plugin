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

package name.patdowney.gocd.plugin.task.docker.run;

import com.thoughtworks.go.plugin.api.task.*;

import com.spotify.docker.client.*;
import com.spotify.docker.client.messages.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DockerRunTaskExecutor {

    public static final String CURLED_FILE = "index.txt";

    public Result execute(Config config, Context context, JobConsoleLogger console) {
        try {
            return runImage(context, config, console);
        } catch (Exception e) {
            return new Result(false, "Failed to run image: " + config.getImageName(), e);
        }
    }

    private Result runImage(Context taskContext, Config taskConfig, JobConsoleLogger console) throws DockerException, DockerCertificateException, InterruptedException {

        DockerClient docker = DefaultDockerClient.fromEnv().build();
	//DockerClient docker = new DefaultDockerClient("unix:///var/run/docker.sock");
	//
	console.printLine("Configuring container with image: " + taskConfig.getImageName());
        ContainerConfig containerConfig = ContainerConfig.builder()
                                 .image(taskConfig.getImageName())
                                 .build();


        HostConfig hostConfig = HostConfig.builder().build();
        ContainerCreation creation = docker.createContainer(containerConfig);
        String id = creation.id();
	console.printLine("Created container with id: " + id);

// Inspect container
// final ContainerInfo info = docker.inspectContainer(id);
//
        // Start container
        docker.startContainer(id, hostConfig);
	console.printLine("Started container with id: " + id);


        return new Result(true, "Image started: " + id + "(" + taskConfig.getImageName() + ")");
    }

/*
    private Result runCommand(Context taskContext, Config taskConfig, JobConsoleLogger console) throws IOException, InterruptedException {
        ProcessBuilder curl = createCurlCommandWithOptions(taskContext, taskConfig);
        console.printLine("Launching command: " + curl.command());
        curl.environment().putAll(taskContext.getEnvironmentVariables());
        console.printEnvironment(curl.environment());

        Process curlProcess = curl.start();
        console.readErrorOf(curlProcess.getErrorStream());
        console.readOutputOf(curlProcess.getInputStream());

        int exitCode = curlProcess.waitFor();
        curlProcess.destroy();

        if (exitCode != 0) {
            return new Result(false, "Failed downloading file. Please check the output");
        }

        return new Result(true, "Downloaded file: " + CURLED_FILE);
    }

    ProcessBuilder createCurlCommandWithOptions(Context taskContext, Config taskConfig) {
        String destinationFilePath = taskContext.getWorkingDir() + "/" + CURLED_FILE;

        List<String> command = new ArrayList<String>();
        command.add("curl");
        command.add(taskConfig.getRequestType());
        if (taskConfig.getSecureConnection().equals("no")) {
            command.add("--insecure");
        }
        if (taskConfig.getAdditionalOptions() != null && !taskConfig.getAdditionalOptions().trim().isEmpty()) {
            String parts[] = taskConfig.getAdditionalOptions().split("\\s+");
            for (String part : parts) {
                command.add(part);
            }
        }
        command.add("-o");
        command.add(destinationFilePath);
        command.add(taskConfig.getUrl());

        return new ProcessBuilder(command);
    }*/
}
