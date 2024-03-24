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
 * Fluent assertions for <code>AWS::ApiGateway::DomainName</code>. This should be used if the resource map is extracted from the AWS template. Otherwise, start
 * with{@link CDKStackAssert#containsApiDomainName(String)}
 */
@SuppressWarnings("unchecked")
public class ApiDomainNameAssert extends AbstractCDKResourcesAssert<ApiDomainNameAssert, Map<String, Object>> {

  private ApiDomainNameAssert(final Map<String, Object> actual) {
    super(actual, ApiDomainNameAssert.class);
  }

  public static ApiDomainNameAssert assertThat(final Map<String, Object> actual) {
    return new ApiDomainNameAssert(actual);
  }

  public ApiDomainNameAssert hasDomainName(final String expected) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");

    final String domainName = (String) properties.get("DomainName");

    Assertions.assertThat(domainName)
        .isNotEmpty()
        .isEqualTo(expected);

    return this;
  }

  public ApiDomainNameAssert hasEndpointConfiguration(final String expected) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Object> endpointConfiguration = (Map<String, Object>) properties.get("EndpointConfiguration");
    final List<String> types = (List<String>) endpointConfiguration.get("Types");

    Assertions.assertThat(types)
        .isNotEmpty()
        .contains(expected);

    return this;
  }

  public ApiDomainNameAssert hasRegionalCertificateArn(final String expected) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, String> regionalCertificateArn = (Map<String, String>) properties.get("RegionalCertificateArn");

    Assertions.assertThat(regionalCertificateArn)
        .isNotEmpty()
        .extracting("Ref")
        .matches(e -> e.toString().matches(expected));

    return this;
  }

}
