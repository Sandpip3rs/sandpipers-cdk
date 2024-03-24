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

package io.sandpipers.cdk.core.construct.lambda;

import io.sadpipers.cdk.type.SafeString;
import io.sandpipers.cdk.core.construct.BaseConstruct;
import io.sandpipers.cdk.core.construct.lambda.AbstractCustomRuntimeFunction.AbstractCustomRuntimeFunctionProps;
import io.sandpipers.cdk.core.construct.sns.Topic;
import io.sandpipers.cdk.core.construct.sns.Topic.TopicProps;
import io.sandpipers.cdk.core.construct.sqs.Queue;
import io.sandpipers.cdk.core.construct.sqs.Queue.QueueProps;
import java.util.List;
import java.util.Map;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.BooleanUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.codeguruprofiler.IProfilingGroup;
import software.amazon.awscdk.services.ec2.ISecurityGroup;
import software.amazon.awscdk.services.ec2.IVpc;
import software.amazon.awscdk.services.ec2.SubnetSelection;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.kms.IKey;
import software.amazon.awscdk.services.lambda.AdotInstrumentationConfig;
import software.amazon.awscdk.services.lambda.Architecture;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.FileSystem;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.FunctionProps;
import software.amazon.awscdk.services.lambda.ICodeSigningConfig;
import software.amazon.awscdk.services.lambda.IEventSource;
import software.amazon.awscdk.services.lambda.ILayerVersion;
import software.amazon.awscdk.services.lambda.LambdaInsightsVersion;
import software.amazon.awscdk.services.lambda.LogRetentionRetryOptions;
import software.amazon.awscdk.services.lambda.LoggingFormat;
import software.amazon.awscdk.services.lambda.ParamsAndSecretsLayerVersion;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.RuntimeManagementMode;
import software.amazon.awscdk.services.lambda.SnapStartConf;
import software.amazon.awscdk.services.lambda.Tracing;
import software.amazon.awscdk.services.lambda.VersionOptions;
import software.amazon.awscdk.services.logs.ILogGroup;
import software.amazon.awscdk.services.logs.RetentionDays;
import software.constructs.Construct;

/**
 * A lambda function with runtime provided.al2023 (custom runtime al2023). Creating function with any other runtime (e.g. passed in the
 * {@link FunctionProps} or via any other means will be ignored).
 */

@Getter
public class AbstractCustomRuntimeFunction<T extends AbstractCustomRuntimeFunctionProps> extends Construct implements BaseConstruct {

  private static final int FUNCTION_DEFAULT_TIMEOUT_IN_SECONDS = 10;
  private static final int FUNCTION_DEFAULT_MEMORY_SIZE = 512;
  private static final int FUNCTION_DEFAULT_RETRY_ATTEMPTS = 2;
  private static final int FUNCTION_DEFAULT_MAX_EVENT_AGE = 60;

  private final Function function;

  /**
   * @param scope This parameter is required.
   * @param id    This parameter is required.
   * @param props This parameter is required.
   */
  public AbstractCustomRuntimeFunction(@NotNull final Construct scope,
      @NotNull final SafeString id,
      @NotNull final T props) {
    super(scope, id.getValue());

    final Function.Builder builder = Function.Builder.create(this, id.getValue())
        .runtime(props.getRuntime())
        .description(props.getDescription())
        .code(props.getCode())
        .handler(props.getHandler())
        .timeout(props.getTimeout())
        .memorySize(props.getMemorySize())
        .retryAttempts(props.getRetryAttempts())
        .maxEventAge(props.getMaxEventAge())
        .retryAttempts(props.getRetryAttempts())
        .role(props.getRole())
        .vpc(props.getVpc())
        .initialPolicy(props.getInitialPolicies())
        .environment(props.getEnvironment());

    if (BooleanUtils.isTrue(props.getDeadLetterTopicEnabled())) {
      final TopicProps topicProps = TopicProps.builder().build();

      final Topic<TopicProps> deadLetterTopic = new Topic<>(this, SafeString.of("DeadLetterTopic"), topicProps);

      builder.deadLetterTopic(deadLetterTopic.getTopic());
    }

    if (BooleanUtils.isTrue(props.getDeadLetterQueueEnabled())) {
      final QueueProps queueProps = QueueProps.builder()
          .requireDeadLetterQueue(false)
          .deadLetterQueueMaxReceiveCount(12)
          .build();

      final Queue<QueueProps> queue = new Queue<>(this, SafeString.of("DeadLetterQueue"), queueProps);
      builder.deadLetterQueue(queue.getQueue());
    }

    function = builder
        .build();

  }

  @Getter
  @SuperBuilder
  public static class AbstractCustomRuntimeFunctionProps implements FunctionProps {

    private final Runtime runtime = null;

    @NotNull("may not be null")
    private String description;

    @NotNull("may not be null")
    private Code code;

    @NotNull("may not be null")
    private String handler;

    @Default
    private final Duration timeout = Duration.seconds(FUNCTION_DEFAULT_TIMEOUT_IN_SECONDS);

    @Default
    @Range(from = 128, to = 3008)
    private final Number memorySize = FUNCTION_DEFAULT_MEMORY_SIZE;

    @Default
    @Range(from = 0, to = 2)
    private final Integer retryAttempts = FUNCTION_DEFAULT_RETRY_ATTEMPTS;

    @Default
    @Range(from = 60, to = 21600)
    private Number maxEventAge = FUNCTION_DEFAULT_MAX_EVENT_AGE;

    @Nullable
    @Default
    private IRole role = null;

    @Nullable
    @Default
    private IVpc vpc = null;

    @Nullable
    @Default
    private Map<String, String> environment = null;

    @Default
    @NotNull
    private Boolean deadLetterTopicEnabled = false;

    @Default
    @NotNull
    private Boolean deadLetterQueueEnabled = false;

    @Default
    @NotNull
    private Boolean failureDestinationRequired = false;

    @Default
    @NotNull
    private Boolean successDestinationRequired = false;

    @Nullable
    @Singular
    private List<PolicyStatement> initialPolicies;

    @Nullable
    private AdotInstrumentationConfig adotInstrumentation;

    @Nullable
    private Boolean allowAllOutbound;

    @Nullable
    private Boolean allowPublicSubnet;

    @Nullable
    private String applicationLogLevel;

    @Nullable
    private Architecture architecture;

    @Nullable
    private ICodeSigningConfig codeSigningConfig;

    @Nullable
    private VersionOptions currentVersionOptions;

    @Nullable
    private IKey environmentEncryption;

    @Nullable
    private software.amazon.awscdk.Size ephemeralStorageSize;

    @Nullable
    private List<IEventSource> events;

    @Nullable
    private FileSystem filesystem;

    @Nullable
    private LambdaInsightsVersion insightsVersion;

    @Nullable
    private java.util.List<ILayerVersion> layers;

    @Nullable
    private String logFormat;

    @Nullable
    private LoggingFormat loggingFormat;

    @Nullable
    private ILogGroup logGroup;

    @Nullable
    private RetentionDays logRetention;

    @Nullable
    private LogRetentionRetryOptions logRetentionRetryOptions;

    @Nullable
    private IRole logRetentionRole;

    @Nullable
    private ParamsAndSecretsLayerVersion paramsAndSecrets;

    @Nullable
    private Boolean profiling;

    @Nullable
    private IProfilingGroup profilingGroup;

    @Nullable
    private java.lang.Number reservedConcurrentExecutions;

    @Nullable
    private RuntimeManagementMode runtimeManagementMode;

    @Nullable
    private List<ISecurityGroup> securityGroups;

    @Nullable
    private SnapStartConf snapStart;

    @Nullable
    private String systemLogLevel;

    @Nullable
    private Tracing tracing;

    @Nullable
    private SubnetSelection vpcSubnets;

    @NotNull
    public String getHandler() {
      return handler;
    }

    @NotNull
    public String getDescription() {
      return description;
    }

    @NotNull
    public Duration getMaxEventAge() {
      return Duration.seconds(maxEventAge.intValue());
    }
  }
}