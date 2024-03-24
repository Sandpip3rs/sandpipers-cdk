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
 * Fluent assertions for <code>AWS::DynamoDB::GlobalTable</code>. This should be used if the resource map is extracted from the AWS template. Otherwise, start with
 * {@link CDKStackAssert#containsDynamoDBTable(String)}.
 */
@SuppressWarnings("unchecked")
public class DynamoDBGlobalTableAssert extends
    AbstractCDKResourcesAssert<DynamoDBGlobalTableAssert, Map<String, Object>> {

  private DynamoDBGlobalTableAssert(final Map<String, Object> actual) {
    super(actual, DynamoDBGlobalTableAssert.class);
  }

  public static DynamoDBGlobalTableAssert assertThat(final Map<String, Object> actual) {
    return new DynamoDBGlobalTableAssert(actual);
  }

  public DynamoDBGlobalTableAssert hasBillingMode(final String expected) {

    final String billingMode = ((Map<String, String>) actual.get("Properties")).get("BillingMode");

    Assertions.assertThat(billingMode)
        .isNotNull()
        .isInstanceOf(String.class)
        .matches(bm -> bm.matches(expected));

    return this;
  }

  public DynamoDBGlobalTableAssert hasName(final String expected) {

    final String billingMode = ((Map<String, String>) actual.get("Properties")).get("TableName");

    Assertions.assertThat(billingMode)
        .isNotNull()
        .isInstanceOf(String.class)
        .matches(bm -> bm.matches(expected));

    return this;
  }

  public DynamoDBGlobalTableAssert hasSSESpecification(final Boolean expected) {

    final Map<String, Object> properties = ((Map<String, Object>) actual.get("Properties"));
    final Map<String, Boolean> sseSpecification = ((Map<String, Boolean>) properties.get("SSESpecification"));
    final Boolean sseEnabled = sseSpecification.get("SSEEnabled");

    Assertions.assertThat(sseEnabled)
        .isEqualTo(expected);

    return this;
  }

  public DynamoDBGlobalTableAssert hasKeySchema(final String name, final String type) {

    final Map<String, Object> properties = ((Map<String, Object>) actual.get("Properties"));
    final List<Map<String, String>> keySchema = ((List<Map<String, String>>) properties.get("KeySchema"));

    Assertions.assertThat(keySchema)
        .isInstanceOf(List.class)
        .anySatisfy(item -> {
          Assertions.assertThat(item.get("AttributeName"))
              .isNotNull()
              .isInstanceOf(String.class)
              .isEqualTo(name);

          Assertions.assertThat(item.get("KeyType"))
              .isNotNull()
              .isInstanceOf(String.class)
              .isEqualTo(type);
        });
    return this;
  }

  public DynamoDBGlobalTableAssert hasAttributeDefinitions(final String name, final String type) {

    final Map<String, Object> properties = ((Map<String, Object>) actual.get("Properties"));
    final List<Map<String, String>> attributeDefinitions = ((List<Map<String, String>>) properties.get("AttributeDefinitions"));

    Assertions.assertThat(attributeDefinitions)
        .isInstanceOf(List.class)
        .anySatisfy(item -> {
          Assertions.assertThat(item.get("AttributeName"))
              .isNotNull()
              .isInstanceOf(String.class)
              .isEqualTo(name);

          Assertions.assertThat(item.get("AttributeType"))
              .isNotNull()
              .isInstanceOf(String.class)
              .isEqualTo(type);
        });
    return this;
  }
}
