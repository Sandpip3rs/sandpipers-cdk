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
 * Fluent assertions for <code>AWS::CertificateManager::Certificate</code>. This should be used if the resource map is extracted from the AWS template. Otherwise, start
 * with{@link CDKStackAssert#containsCertificate(String)}
 */
@SuppressWarnings("unchecked")
public class CertificateAssert extends AbstractCDKResourcesAssert<CertificateAssert, Map<String, Object>> {

  private CertificateAssert(final Map<String, Object> actual) {
    super(actual, CertificateAssert.class);
  }

  public static CertificateAssert assertThat(final Map<String, Object> actual) {
    return new CertificateAssert(actual);
  }

  public CertificateAssert hasDomainName(final String expected) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");

    final String domainName = (String) properties.get("DomainName");

    Assertions.assertThat(domainName)
        .isNotEmpty()
        .isEqualTo(expected);

    return this;
  }

  public CertificateAssert hasDomainValidationOptions(final String domainName, final String validationDomain) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final List< Object> domainValidationOptions = (List< Object>) properties.get("DomainValidationOptions");

    Assertions.assertThat(domainValidationOptions)
        .isNotNull()
        .isNotEmpty()
        .anySatisfy(dvo -> {
          final Map<String, Object> domainValidationOption = (Map<String, Object>) dvo;
          final String domainNameValue = (String) domainValidationOption.get("DomainName");
          final String validationDomainValue = (String) domainValidationOption.get("ValidationDomain");

          Assertions.assertThat(domainNameValue)
              .isNotEmpty()
              .isEqualTo(domainName);

          Assertions.assertThat(validationDomainValue)
              .isNotEmpty()
              .isEqualTo(validationDomain);
        });

    return this;
  }

  public CertificateAssert hasValidationMethod(final String expected) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final String validationMethod = (String) properties.get("ValidationMethod");

    Assertions.assertThat(validationMethod)
        .isNotEmpty()
        .isEqualTo(expected);

    return this;
  }
}
