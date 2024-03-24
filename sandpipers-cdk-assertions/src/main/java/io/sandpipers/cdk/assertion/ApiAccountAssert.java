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
 * Fluent assertions for <code>AWS::ApiGateway::Account</code>. This should be used if the resource map is extracted from the AWS template. Otherwise, start with
 * {@link CDKStackAssert#containsApiAccount(String)}.
 */
@SuppressWarnings("unchecked")
public class ApiAccountAssert extends
    AbstractCDKResourcesAssert<ApiAccountAssert, Map<String, Object>> {

  private ApiAccountAssert(final Map<String, Object> actual) {
    super(actual, ApiAccountAssert.class);
  }

  public static ApiAccountAssert assertThat(final Map<String, Object> actual) {
    return new ApiAccountAssert(actual);
  }

  public ApiAccountAssert hasCloudWatchRole(final String expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, List<String>> cloudWatchRoleArn = (Map<String, List<String>>) properties.get(
        "CloudWatchRoleArn");

    Assertions.assertThat(cloudWatchRoleArn)
        .isNotEmpty()
        .flatExtracting("Fn::GetAtt")
        .isNotEmpty()
        .map(e -> (String) e)
        .anySatisfy(arn -> Assertions.assertThat(arn).matches(e -> e.matches(expected)));

    return this;
  }
}
