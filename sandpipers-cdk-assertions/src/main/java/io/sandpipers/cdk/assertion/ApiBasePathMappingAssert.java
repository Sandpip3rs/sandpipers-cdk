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
import org.assertj.core.api.Assertions;

/**
 * Fluent assertions for <code>AWS::ApiGateway::BasePathMapping</code>. This should be used if the resource map is extracted from the AWS template. Otherwise, start with
 * {@link CDKStackAssert#containsApiPathMapping(String)}.
 */
@SuppressWarnings("unchecked")
public class ApiBasePathMappingAssert extends AbstractCDKResourcesAssert<ApiBasePathMappingAssert, Map<String, Object>> {

  private ApiBasePathMappingAssert(final Map<String, Object> actual) {
    super(actual, ApiBasePathMappingAssert.class);
  }

  public static ApiBasePathMappingAssert assertThat(final Map<String, Object> actual) {
    return new ApiBasePathMappingAssert(actual);
  }

  public ApiBasePathMappingAssert hasDomainName(final String expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, String> domainName = (Map<String, String>) properties.get("DomainName");

    Assertions.assertThat(domainName)
        .isNotEmpty()
        .extracting("Ref")
        .matches(e -> e.toString().matches(expected));

    return this;
  }

  public ApiBasePathMappingAssert hasRestApiId(final String expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, String> restApiId = (Map<String, String>) properties.get("RestApiId");

    Assertions.assertThat(restApiId)
        .isNotEmpty()
        .extracting("Ref")
        .matches(e -> e.toString().matches(expected));

    return this;
  }

  public ApiBasePathMappingAssert hasStage(final String expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, String> stage = (Map<String, String>) properties.get("Stage");

    Assertions.assertThat(stage)
        .isNotEmpty()
        .extracting("Ref")
        .matches(e -> e.toString().matches(expected));

    return this;
  }
}
