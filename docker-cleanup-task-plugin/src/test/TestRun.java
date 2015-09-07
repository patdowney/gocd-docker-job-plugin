import com.spotify.docker.client;

public class TestRun {
	public static void main(String[] args) {

		      DockerClient docker = DefaultDockerClient.fromEnv().build();
		      DockerClient docker = new DefaultDockerClient("unix:///var/run/docker.sock");
		      ContainerConfig config = ContainerConfig.builder()
		      .image(config.getImageName())
		      .build();
		      HostConfig hostConfig = HostConfig.builder().build();
		      String containerName = "demo-name";
		      ContainerCreation creation = docker.createContainer(config, containerName);
		      String id = creation.id();
		      //
		      //                                                                                                          // Inspect container
		      //                                                                                                          // final ContainerInfo info = docker.inspectContainer(id);
		      //                                                                                                          //
		      //                                                                                                                  // Start container
		      docker.startContainer(id, hostConfig);

		      System.out.println("Hello: " + id);
	}
}
