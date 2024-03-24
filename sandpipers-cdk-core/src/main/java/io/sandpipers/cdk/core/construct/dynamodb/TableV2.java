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

package io.sandpipers.cdk.core.construct.dynamodb;

import io.sandpipers.cdk.core.construct.BaseConstruct;
import io.sadpipers.cdk.type.KebabCaseString;
import io.sadpipers.cdk.type.SafeString;
import java.util.List;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.Billing;
import software.amazon.awscdk.services.dynamodb.GlobalSecondaryIndexPropsV2;
import software.amazon.awscdk.services.dynamodb.LocalSecondaryIndexProps;
import software.amazon.awscdk.services.dynamodb.TableEncryptionV2;
import software.amazon.awscdk.services.dynamodb.TablePropsV2;
import software.constructs.Construct;

/**
 * L3 Construct representing AWS::DynamoDB::GlobalTable
 * <p> Example usage can be found in <a
 * href="https://github.com/muhamadto/sandpipers-cdk/blob/main/sandpipers-cdk-examples/sandpipers-cdk-example-dynamodb/src/main/java/com/sandpipers/cdk/example/dynamodb/DynamoDBStack.java">sandpipers-cdk-example-dynamodb</a></p>
 */
@Getter
public class TableV2 extends Construct implements BaseConstruct {

  private software.amazon.awscdk.services.dynamodb.TableV2 table;

  public TableV2(
      @NotNull final Construct scope,
      @NotNull final SafeString id,
      @NotNull final TableProps props) {
    super(scope, id.getValue());

    this.table = software.amazon.awscdk.services.dynamodb.TableV2.Builder.create(this, id.getValue())
        .billing(props.getBilling())
        .tableName(props.getTableName())
        .encryption(props.getEncryption())
        .partitionKey(props.getPartitionKey())
        .removalPolicy(props.getRemovalPolicy())
        .sortKey(props.getSortKey())
        .deletionProtection(props.getDeletionProtection())
        .localSecondaryIndexes(props.getLocalSecondaryIndexes())
        .globalSecondaryIndexes(props.getGlobalSecondaryIndexes())
        .kinesisStream(props.getKinesisStream())
        .pointInTimeRecovery(props.getPointInTimeRecovery())
        .dynamoStream(props.getDynamoStream())
        .replicas(props.getReplicas())
        .contributorInsights(props.getContributorInsights())
        .build();
  }

  @Getter
  @SuperBuilder
  public static class TableProps implements TablePropsV2 {

   //TODO make it kebab case type
    @NotNull
    private final KebabCaseString tableName;

    @Default
    private Billing billing = Billing.onDemand();

    @Default
    private RemovalPolicy removalPolicy = RemovalPolicy.RETAIN;

    @Default
    private TableEncryptionV2 encryption = TableEncryptionV2.dynamoOwnedKey();

    @NotNull
    private Attribute partitionKey;

    @Singular
    private List<GlobalSecondaryIndexPropsV2> globalSecondaryIndexes;

    @Singular
    private List<LocalSecondaryIndexProps> localSecondaryIndexes;

    @Nullable
    private Attribute sortKey;

    @Nullable
    private String timeToLiveAttribute;

    @Nullable
    private Boolean contributorInsights;

    public String getTableName() {
      return tableName.getValue();
    }
  }
}
