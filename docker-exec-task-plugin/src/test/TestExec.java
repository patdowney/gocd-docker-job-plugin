import com.spotify.docker.client.*;
import com.spotify.docker.client.messages.*;

import static com.spotify.docker.client.DockerClient.ExecParameter.STDOUT;
import static com.spotify.docker.client.DockerClient.ExecParameter.STDERR;

import com.google.common.collect.*;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class TestExec {
	public static void main(String[] args) throws Exception{

		      DockerClient docker = DefaultDockerClient.fromEnv().build();
//		      DockerClient docker = new DefaultDockerClient("unix:///var/run/docker.sock");
		      //ContainerConfig config = ContainerConfig.builder()
		      //.image("ubuntu:15.04")
		      //.build();
		      //HostConfig hostConfig = HostConfig.builder().build();
		      String containerName = "demo-name";
		      //ContainerCreation creation = docker.createContainer(config, containerName);
		      //String id = creation.id();
		      //
		      //                                                                                                          // Inspect container
		      //                                                                                                          // final ContainerInfo info = docker.inspectContainer(id);
		      //                                                                                                          //
		      //                                                                                                                  // Start container
		      //docker.startContainer(id, hostConfig);

		      //System.out.println("Hello: " + id);

	String execId = docker.execCreate(containerName, new String[]{"find", "/"}, STDOUT, STDERR);

	/*
        final PipedOutputStream stdout = new PipedOutputStream(); //new PipedOutputStream();
        final PipedOutputStream stderr = new PipedOutputStream(); //new PipedOutputStream();
        final PipedInputStream stdout_pipe = new PipedInputStream(stdout);
        final PipedInputStream stderr_pipe = new PipedInputStream(stderr);
*/
	//console.readErrorOf(stderr_pipe);
	//console.readOutputOf(stdout_pipe);


        LogStream log = docker.execStart(execId); //, new String[]{"find", "."});
        //log.attach(stdout, stderr);
        log.attach(System.out, System.err);


        ExecState e = docker.execInspect(execId);

    }

}
