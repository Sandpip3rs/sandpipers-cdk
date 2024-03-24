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

package io.sandpipers.cdk.example.dynamodb;

import static io.sandpipers.cdk.assertion.CDKStackAssert.assertThat;
import static io.sandpipers.cdk.core.AbstractApp.tagResources;
import static io.sandpipers.cdk.example.dynamodb.Environment.SANDPIPERS_TEST_111111111111_AP_SOUTHEAST_2;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import software.amazon.awscdk.assertions.Template;

public class DynamoDBTest extends TemplateSupport {

  @BeforeAll
  static void initAll() {

    final Application app = new Application();

    final DynamoDBStack dynamoDBStack = new DynamoDBStack(app, SANDPIPERS_TEST_111111111111_AP_SOUTHEAST_2);

    tagResources(dynamoDBStack, SANDPIPERS_TEST_111111111111_AP_SOUTHEAST_2, app.getApplicationName());

    template = Template.fromStack(dynamoDBStack);
  }

  @Test
  void should_have_dynamodb_table() {
    assertThat(template)
        .containsDynamoDBTable("^Table[a-zA-Z0-9]{8}$")
        .hasBillingMode("PAY_PER_REQUEST")
        .hasName("secrets")
        .hasSSESpecification(false)
        .hasKeySchema("id", "HASH")
        .hasKeySchema("createdAt", "RANGE")
        .hasAttributeDefinitions("id", "S")
        .hasAttributeDefinitions("createdAt", "N")
        .hasUpdateReplacePolicy("Retain")
        .hasDeletionPolicy("Retain");
  }
}
