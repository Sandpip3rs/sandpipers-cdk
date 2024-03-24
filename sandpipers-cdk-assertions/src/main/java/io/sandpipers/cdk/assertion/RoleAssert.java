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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;

/**
 * Fluent assertions for <code>AWS::IAM::Role</code>. This should be used if the resource map is extracted from the AWS template. Otherwise, start with
 * {@link CDKStackAssert#containsRole(String)}.
 */
@SuppressWarnings("unchecked")
public class RoleAssert extends AbstractCDKResourcesAssert<RoleAssert, Map<String, Object>> {

  private RoleAssert(final Map<String, Object> actual) {
    super(actual, RoleAssert.class);
  }

  public static RoleAssert assertThat(final Map<String, Object> actual) {
    return new RoleAssert(actual);
  }

  public RoleAssert hasManagedPolicyArn(final String managedPolicyArnString) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final List<Object> managedPolicyArns = (List<Object>) properties.get("ManagedPolicyArns");

    // TODO flatten ManagedPolicyArns and test again string

    Assertions.assertThat(managedPolicyArns)
        .isNotNull()
        .hasSize(1)
        .anySatisfy(s -> {
          Assertions.assertThat(s)
              .isInstanceOf(Map.class)
              .extracting("Fn::Join")
              .satisfies(joinList -> {
                if (joinList instanceof List) {
                  Assertions.assertThat((List<Object>) joinList)
                      .flatExtracting(joinElement -> joinElement instanceof List ? (List<Object>) joinElement : Collections.emptyList())
                      .anySatisfy(joinElement ->
                          Assertions.assertThat(joinElement)
                              .matches(e -> e.toString().matches(managedPolicyArnString)));
                }
              });
        });

    return this;
  }

  public RoleAssert hasAssumeRolePolicyDocument(final Map<String, String> principal,
      final String resource,
      final String effect,
      final String policyDocumentVersion,
      final String action) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Object> policyDocument =
        (Map<String, Object>) properties.get("AssumeRolePolicyDocument");

    return hasPolicy(principal, resource, effect, policyDocumentVersion, action, policyDocument);
  }
}
