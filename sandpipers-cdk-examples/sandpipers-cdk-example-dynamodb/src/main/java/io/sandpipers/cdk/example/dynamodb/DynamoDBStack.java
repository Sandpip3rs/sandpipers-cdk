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

import io.sandpipers.cdk.core.AbstractApp;
import io.sandpipers.cdk.core.construct.BaseStack;
import io.sandpipers.cdk.core.construct.dynamodb.TableV2;
import io.sandpipers.cdk.core.construct.dynamodb.TableV2.TableProps;
import io.sadpipers.cdk.type.KebabCaseString;
import io.sadpipers.cdk.type.SafeString;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.Billing;

public class DynamoDBStack extends BaseStack {

  public DynamoDBStack(@NotNull final AbstractApp app, @NotNull final Environment environment) {
    super(app, environment);

    final TableV2.TableProps tableProps = TableProps.builder()
        .billing(Billing.onDemand())
        .partitionKey(Attribute.builder().name("id").type(AttributeType.STRING).build())
        .sortKey(Attribute.builder().name("createdAt").type(AttributeType.NUMBER).build())
        .timeToLiveAttribute("expiresAt")
        .tableName(KebabCaseString.of("Secrets"))
        .build();

    new TableV2(this, SafeString.of("Table"), tableProps);
  }
}
