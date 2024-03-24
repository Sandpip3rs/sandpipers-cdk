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

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;

/**
 * Fluent assertions for <code>AWS::ApiGateway::Deployment</code>. This should be used if the resource map is extracted from the AWS template. Otherwise, start
 * with{@link CDKStackAssert#containsApiMethod(String)}
 */
@SuppressWarnings("unchecked")
public class ApiMethodAssert extends AbstractCDKResourcesAssert<ApiMethodAssert, Map<String, Object>> {

  private ApiMethodAssert(final Map<String, Object> actual) {
    super(actual, ApiMethodAssert.class);
  }

  public static ApiMethodAssert assertThat(final Map<String, Object> actual) {
    return new ApiMethodAssert(actual);
  }

  public ApiMethodAssert hasRestApiId(final String expected) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, String> restApiId = (Map<String, String>) properties.get("RestApiId");

    Assertions.assertThat(restApiId)
        .isNotEmpty()
        .extracting("Ref")
        .matches(e -> e.toString().matches(expected));

    return this;
  }

  public ApiMethodAssert hasAuthorizationType(final String expected) {
    final Map<String, String> properties = (Map<String, String>) actual.get("Properties");
    final String authorizationType = properties.get("AuthorizationType");

    Assertions.assertThat(authorizationType)
        .isNotBlank()
        .matches(e -> e.matches(expected));

    return this;
  }

  public ApiMethodAssert hasHttpMethod(final String expected) {

    final Map<String, String> properties = (Map<String, String>) actual.get("Properties");
    final String httpMethod = properties.get("HttpMethod");

    Assertions.assertThat(httpMethod)
        .isNotBlank()
        .matches(e -> e.matches(expected));

    return this;
  }

  public ApiMethodAssert hasIntegration(final String method, final String type, final String uri) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Object> integration = (Map<String, Object>) properties.get("Integration");
    final Map<String, Object> actual = (Map<String, Object>) integration.get("Uri");

    final String actualUrl = flattenSourceArn(actual);

    Assertions.assertThat(integration)
        .isNotEmpty()
        .containsEntry("IntegrationHttpMethod", method)
        .containsEntry("Type", type);

    if (StringUtils.isNotBlank(actualUrl)) {
      Assertions.assertThat(integration)
          .containsKey("Uri");
    }

    return this;
  }

  public ApiMethodAssert hasResourceId(final String expected) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, String> resourceId = (Map<String, String>) properties.get("ResourceId");

    if(resourceId.containsKey("Fn::GetAtt")) {
      Assertions.assertThat(resourceId)
          .isNotEmpty()
          .flatExtracting("Fn::GetAtt")
          .map(e -> (String) e)
          .anySatisfy(e -> Assertions.assertThat(e).matches(expected))
          .anySatisfy(e -> Assertions.assertThat(e).matches("RootResourceId"));
    } else {
      Assertions.assertThat(resourceId)
          .isNotEmpty()
          .extracting("Ref")
          .matches(e -> e.toString().matches(expected));
    }
    return this;
  }
}
