/*
 * Copyright 2019 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.igor.gcb;

import com.google.api.services.cloudbuild.v1.CloudBuild;
import com.google.api.services.cloudbuild.v1.model.Build;
import com.google.api.services.cloudbuild.v1.model.Operation;
import com.netflix.spinnaker.kork.web.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * Generates authenticated requests to the Google Cloud Build API for a single configured account, delegating to
 * GoogleCloudBuildExecutor to execute these requests.
 */
@RequiredArgsConstructor
public class GoogleCloudBuildAccount {
  private final String projectId;
  private final CloudBuild cloudBuild;
  private final GoogleCloudBuildExecutor executor;
  private final GoogleCloudBuildCache googleCloudBuildCache;

  @SuppressWarnings("unchecked")
  public Map<String, Object> createBuild(Build build) {
    Operation operation = executor.execute(() -> cloudBuild.projects().builds().create(projectId, build));
    return (Map<String, Object>) operation.getMetadata().get("build");
  }

  public void updateBuild(String buildId, String status, String serializedBuild) {
    googleCloudBuildCache.updateBuild(buildId, status, serializedBuild);
  }

  public String getBuild(String buildId) {
    return googleCloudBuildCache.getBuild(buildId);
  }
}
