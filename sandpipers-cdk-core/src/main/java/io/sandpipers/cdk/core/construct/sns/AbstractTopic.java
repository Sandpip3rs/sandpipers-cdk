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

package io.sandpipers.cdk.core.construct.sns;

import static software.amazon.awscdk.services.sns.Topic.Builder.create;

import io.sandpipers.cdk.core.construct.BaseConstruct;
import io.sandpipers.cdk.core.construct.sns.AbstractTopic.AbstractTopicProps;
import io.sadpipers.cdk.type.SafeString;
import java.util.List;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.amazon.awscdk.services.kms.IKey;
import software.amazon.awscdk.services.sns.ITopic;
import software.amazon.awscdk.services.sns.LoggingConfig;
import software.constructs.Construct;

@Getter
public class AbstractTopic<T extends AbstractTopicProps> extends Construct implements BaseConstruct {

  private final ITopic topic;

  public AbstractTopic(@NotNull final Construct scope,
      @NotNull final SafeString id,
      @NotNull final T props) {
    super(scope, id.getValue());

    topic = create(this, id.getValue())
        .fifo(props.getFifo())
        .contentBasedDeduplication(props.getContentBasedDeduplication())
        .masterKey(props.getMasterKey())
        .loggingConfigs(props.getLoggingConfigs())
        .messageRetentionPeriodInDays(props.getMessageRetentionPeriodInDays())
        .build();
  }

  @Getter
  @SuperBuilder
  public static class AbstractTopicProps implements software.amazon.awscdk.services.sns.TopicProps {

    protected static final software.amazon.awscdk.services.sns.TopicProps TOPIC_PROPS =
        software.amazon.awscdk.services.sns.TopicProps.builder().build();

    private final Boolean fifo = false;

    @Nullable
    @Default
    private final IKey masterKey = TOPIC_PROPS.getMasterKey();

    @Nullable
    @Default
    private final List<LoggingConfig> loggingConfigs = TOPIC_PROPS.getLoggingConfigs();

    @Nullable
    @Default
    private final Number messageRetentionPeriodInDays = TOPIC_PROPS.getMessageRetentionPeriodInDays();
  }
}
