/*
 *  Licensed to Muhammad Hamadto
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   See the NOTICE file distributed with this work for additional information regarding copyright ownership.
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package io.sandpipers.cdk.assertion;

import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;

/**
 * Fluent assertions for <code>AWS::ApiGateway::Resource</code>. This should be used if the resource map is extracted from the AWS template. Otherwise, start with
 * {@link CDKStackAssert#containsApiResource(String)}.
 */
@SuppressWarnings("unchecked")
public class ApiResourceAssert extends
    AbstractCDKResourcesAssert<ApiResourceAssert, Map<String, Object>> {

  private ApiResourceAssert(final Map<String, Object> actual) {
    super(actual, ApiResourceAssert.class);
  }

  public static ApiResourceAssert assertThat(final Map<String, Object> actual) {
    return new ApiResourceAssert(actual);
  }

  public ApiResourceAssert hasRestApiId(final String expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, String> restApiId = (Map<String, String>) properties.get("RestApiId");

    Assertions.assertThat(restApiId)
        .isNotEmpty()
        .extracting("Ref")
        .matches(e -> e.toString().matches(expected));

    return this;
  }

  public ApiResourceAssert hasPath(final String expected) {
    final Map<String, String> properties = (Map<String, String>) actual.get("Properties");
    final String pathPart = properties.get("PathPart");

    Assertions.assertThat(pathPart)
        .isNotBlank()
        .matches(sn -> sn.equals(expected));

    return this;
  }

  public ApiResourceAssert hasParentId(final String expected) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, List<String>> parentId = (Map<String, List<String>>) properties.get("ParentId");

    if (parentId.containsKey("Fn::GetAtt")) {
      Assertions.assertThat(parentId)
          .isNotEmpty()
          .flatExtracting("Fn::GetAtt")
          .map(e -> (String) e)
          .anySatisfy(e -> Assertions.assertThat(e).matches(expected))
          .anySatisfy(e -> Assertions.assertThat(e).matches("RootResourceId"));
    } else {
      Assertions.assertThat(parentId)
          .isNotEmpty()
          .extracting("Ref")
          .matches(e -> e.toString().matches(expected));
    }
    return this;
  }
}
