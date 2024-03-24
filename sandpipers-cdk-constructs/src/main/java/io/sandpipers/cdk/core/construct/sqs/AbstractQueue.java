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

package io.sandpipers.cdk.core.construct.sqs;

import static io.sandpipers.cdk.core.util.Utils.kebabToCamel;
import static software.amazon.awscdk.services.sqs.Queue.Builder.create;
import static software.amazon.awscdk.services.sqs.RedrivePermission.DENY_ALL;

import io.sandpipers.cdk.core.construct.BaseConstruct;
import io.sandpipers.cdk.core.construct.sqs.AbstractQueue.AbstractQueueProps;
import io.sadpipers.cdk.type.SafeString;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.services.kms.IKey;
import software.amazon.awscdk.services.sqs.DeadLetterQueue;
import software.amazon.awscdk.services.sqs.IQueue;
import software.amazon.awscdk.services.sqs.Queue;
import software.amazon.awscdk.services.sqs.QueueEncryption;
import software.amazon.awscdk.services.sqs.RedriveAllowPolicy;
import software.constructs.Construct;

@Getter
public class AbstractQueue<T extends AbstractQueueProps> extends Construct implements BaseConstruct {

  protected static final int DEAD_LETTER_QUEUE_MAX_RECEIVE_COUNT = 3;

  private final IQueue queue;
  private final DeadLetterQueue deadLetterQueue;

  public AbstractQueue(@NotNull final Construct scope,
      @NotNull final SafeString id,
      @NotNull final T props) {
    super(scope, id.getValue());

    final String idValue = kebabToCamel(id.getValue());

    deadLetterQueue = props.getRequireDeadLetterQueue() ? createDeadLetterQueue("DeadLetterQueue", props) : null;
    queue = createQueue(idValue, props, deadLetterQueue);
  }

  @NotNull
  private DeadLetterQueue createDeadLetterQueue(final String id, final T props) {
    final Queue queue = getQueueBuilder(id, props)
        .build();

    return DeadLetterQueue.builder()
        .maxReceiveCount(props.getDeadLetterQueueMaxReceiveCount())
        .queue(queue)
        .build();
  }

  public Queue createQueue(final String id, final T props, final DeadLetterQueue deadLetterQueue) {

    return getQueueBuilder(id, props)
        .deadLetterQueue(deadLetterQueue)
        .redriveAllowPolicy(RedriveAllowPolicy.builder().redrivePermission(DENY_ALL).build())
        .build();
  }

  private Queue.Builder getQueueBuilder(final String id, final T props) {
    return create(this, id)
        .fifo(props.getFifo())
        .deduplicationScope(props.getDeduplicationScope())
        .contentBasedDeduplication(props.getContentBasedDeduplication())
        .fifoThroughputLimit(props.getFifoThroughputLimit())
        .maxMessageSizeBytes(props.getMaxMessageSizeBytes())
        .receiveMessageWaitTime(props.getReceiveMessageWaitTime())
        .visibilityTimeout(props.getDeliveryDelay())
        .removalPolicy(props.getRemovalPolicy())
        .enforceSsl(props.getEnforceSSL())
        .encryptionMasterKey(props.getEncryptionMasterKey())
        .encryption(props.getEncryption())
        .dataKeyReuse(props.getDataKeyReuse())
        .deliveryDelay(props.getDeliveryDelay())
        .retentionPeriod(props.getRetentionPeriod());
  }

  @Getter
  @SuperBuilder
  public static class AbstractQueueProps implements software.amazon.awscdk.services.sqs.QueueProps {


    private final Boolean fifo = false;

    /**
     * This value is intentionally {@code null}. The dead-letter queue will be automatically created and assigned to the queue. Refer to
     * {@link AbstractQueue#createDeadLetterQueue(String, AbstractQueueProps)}
     */
    private final DeadLetterQueue deadLetterQueue = null;

    @Default
    @NotNull
    private final Boolean requireDeadLetterQueue = true;

    @Default
    @NotNull
    private final RemovalPolicy removalPolicy = RemovalPolicy.RETAIN;

    @Default
    @NotNull
    private final Boolean enforceSSL = true;

    @Default
    @NotNull
    private final QueueEncryption encryption = QueueEncryption.SQS_MANAGED;

    @Default
    @Nullable
    private final Number deadLetterQueueMaxReceiveCount = DEAD_LETTER_QUEUE_MAX_RECEIVE_COUNT;

    @Default
    @Nullable
    private final Duration deliveryDelay = Duration.seconds(0);

    @Default
    @Nullable
    private final Duration retentionPeriod = Duration.days(14);

    @Default
    @Nullable
    private final Number maxMessageSizeBytes = null;

    @Default
    @Nullable
    private final Duration receiveMessageWaitTime = null;

    @Default
    @Nullable
    private final Duration visibilityTimeout = null;

    @Default
    @Nullable
    private final IKey encryptionMasterKey = null;

    @Default
    @Nullable
    private final Duration dataKeyReuse = null;

    @Default
    @Nullable
    private final RedriveAllowPolicy redriveAllowPolicy = null;

  }
}
