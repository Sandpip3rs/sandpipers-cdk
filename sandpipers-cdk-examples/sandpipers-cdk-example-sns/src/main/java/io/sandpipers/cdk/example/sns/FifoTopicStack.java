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

package io.sandpipers.cdk.example.sns;

import io.sandpipers.cdk.core.AbstractApp;
import io.sandpipers.cdk.core.construct.BaseStack;
import io.sandpipers.cdk.core.construct.sns.FifoTopic;
import io.sandpipers.cdk.core.construct.sns.FifoTopic.FifoTopicProps;
import io.sandpipers.cdk.core.construct.sqs.Queue;
import io.sandpipers.cdk.core.construct.sqs.Queue.QueueProps;
import io.sadpipers.cdk.type.SafeString;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.services.kms.IKey;
import software.amazon.awscdk.services.kms.Key;
import software.amazon.awscdk.services.sns.Subscription;
import software.amazon.awscdk.services.sns.SubscriptionProtocol;

public class FifoTopicStack extends BaseStack {

  public FifoTopicStack(@NotNull AbstractApp app, @NotNull Environment environment) {
    super(app, environment);

    final IKey key = Key.Builder.create(this, "Key")
        .description("Key for the topic")
        .build();

    final FifoTopicProps fifoTopicProps = FifoTopicProps.builder()
        .contentBasedDeduplication(true)
        .messageRetentionPeriodInDays(30)
        .masterKey(key)
        .build();

    final FifoTopic<FifoTopicProps> fifoTopic = new FifoTopic<>(this, SafeString.of("Topic"), fifoTopicProps);

    final QueueProps deadLetterQueueProps = QueueProps.builder().requireDeadLetterQueue(false).build();
    final Queue<QueueProps> deadLetterQueue = new Queue<>(this, SafeString.of("DeadLetterQueue"), deadLetterQueueProps);

    Subscription.Builder.create(this, "Subscription")
        .topic(fifoTopic.getTopic())
        .deadLetterQueue(deadLetterQueue.getQueue())
        .protocol(SubscriptionProtocol.SQS)
        .endpoint("http://example.com")
        .build();
  }
}
