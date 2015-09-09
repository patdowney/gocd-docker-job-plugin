/*************************
 * GO-LICENSE-START*********************************
 * Copyright 2015 ThoughtWorks, Inc.
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

package name.patdowney.gocd.plugin.common;

import java.nio.file.Paths;
import java.util.Map;

public class Context {
    private final Map environmentVariables;
    private final String workingDir;

    public Context(Map context) {
        environmentVariables = (Map) context.get("environmentVariables");
        workingDir = (String) context.get("workingDirectory");
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getEnvironmentVariables() {
        return environmentVariables;
    }

    public String getWorkingDir() {
        return workingDir;
    }

    public String getAbsoluteWorkingDir() {
        return Paths.get("").toAbsolutePath().resolve(workingDir).toString();
    }

    public String getPipelineDescription() {
        return String.format("%s_%s_%s_%s_%s",
                environmentVariables.get("GO_PIPELINE_NAME").toString(),
                environmentVariables.get("GO_PIPELINE_COUNTER").toString(),
                environmentVariables.get("GO_STAGE_NAME").toString(),
                environmentVariables.get("GO_STAGE_COUNTER").toString(),
                environmentVariables.get("GO_JOB_NAME").toString());
    }

}
