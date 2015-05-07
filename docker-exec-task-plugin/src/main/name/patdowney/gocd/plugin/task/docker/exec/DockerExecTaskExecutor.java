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

package name.patdowney.gocd.plugin.task.docker.exec;

import com.thoughtworks.go.plugin.api.task.*;

import com.spotify.docker.client.*;
import com.spotify.docker.client.messages.*;
import static com.spotify.docker.client.DockerClient.ExecParameter.STDOUT;
import static com.spotify.docker.client.DockerClient.ExecParameter.STDERR;


import com.google.common.collect.*;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DockerExecTaskExecutor {
    public Result execute(Config config, Context context, JobConsoleLogger console) {
        try {
            return runExec(context, config, console);
        } catch (Exception e) {
            return new Result(false, "Failed to run command: " + config.getExecCommand(), e);
        }
    }

    private Result runExec(Context taskContext, Config taskConfig, JobConsoleLogger console) throws DockerException, DockerCertificateException, InterruptedException, IOException {

	final String containerInstance = taskContext.getPipelineDescription();

        DockerClient docker = DefaultDockerClient.fromEnv().build();

	String execId = docker.execCreate(containerInstance,new String[]{"find", "/"}, STDOUT, STDERR);

        final PipedOutputStream stdout = new PipedOutputStream();
        final PipedOutputStream stderr = new PipedOutputStream();
        final PipedInputStream stdout_pipe = new PipedInputStream(stdout);
        final PipedInputStream stderr_pipe = new PipedInputStream(stderr);

	console.readErrorOf(stderr_pipe);
	console.readOutputOf(stdout_pipe);

        LogStream log = docker.execStart(execId); //, new String[]{"find", "."});
        log.attach(stdout, stderr);


        ExecState e = docker.execInspect(execId);

        return new Result(true, "Command Execute: " + taskConfig.getExecCommand());
    }

}
