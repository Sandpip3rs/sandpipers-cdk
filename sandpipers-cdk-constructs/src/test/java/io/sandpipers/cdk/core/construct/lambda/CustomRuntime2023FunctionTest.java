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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static software.amazon.awscdk.services.lambda.Architecture.ARM_64;
import static software.amazon.awscdk.services.lambda.CodeSigningConfig.fromCodeSigningConfigArn;
import static software.amazon.awscdk.services.lambda.Runtime.PROVIDED_AL2023;

import io.sandpipers.cdk.core.construct.lambda.AbstractCustomRuntimeFunction.AbstractCustomRuntimeFunctionProps;
import io.sandpipers.cdk.core.construct.lambda.CustomRuntime2023Function.CustomRuntime2023FunctionProps;
import io.sandpipers.cdk.core.construct.lambda.CustomRuntime2023Function.CustomRuntime2023FunctionProps.CustomRuntime2023FunctionPropsBuilder;
import io.sadpipers.cdk.type.SafeString;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Size;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.codeguruprofiler.ProfilingGroup;
import software.amazon.awscdk.services.ec2.SecurityGroup;
import software.amazon.awscdk.services.ec2.SubnetSelection;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.kms.Key;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Tracing;
import software.amazon.awscdk.services.lambda.VersionOptions;
import software.amazon.awscdk.services.lambda.eventsources.ApiEventSource;
import software.amazon.awscdk.services.logs.RetentionDays;

@Disabled
class CustomRuntime2023FunctionTest {

  @TempDir
  private static Path TEMP_DIR;
  private Stack stack;
  private SafeString id;
  private Path lambdaCodePath;
  private CustomRuntime2023FunctionPropsBuilder functionPropsBuilder;

  @BeforeEach
  void setUp() throws IOException {
    lambdaCodePath = TestLambdaUtils.getTestLambdaCodePath(TEMP_DIR);

    stack = new Stack(new App(), "test-stack");
    id = SafeString.of("test-function");

    functionPropsBuilder = CustomRuntime2023FunctionProps.builder()
        .maxEventAge(514)
        .failureDestinationRequired(true)
        .successDestinationRequired(true)
        .retryAttempts(2)
        .allowPublicSubnet(false)
        .architecture(ARM_64)
        .codeSigningConfig(fromCodeSigningConfigArn(stack, "test-code-signing-config", "arn:aws:lambda:us-east-1:***:code-signing-config:***"))
        .currentVersionOptions(VersionOptions.builder().build())
        .deadLetterQueueEnabled(false)
        .deadLetterTopicEnabled(false)
        .description("test function")
        .environment(Map.of("Account", "***"))
        .environmentEncryption(Key.fromKeyArn(stack, "test-key", "arn:aws:kms:us-east-1:***:key/***"))
        .ephemeralStorageSize(Size.gibibytes(1))
        .events(List.of(new ApiEventSource("POST", "/test")))
        .filesystem(null)
        .initialPolicy(PolicyStatement.Builder.create().build())
        .insightsVersion(null)
        .layers(Collections.emptyList())
        .logRetention(RetentionDays.FIVE_DAYS)
        .logRetentionRole(Role.fromRoleArn(stack, "test-log-role", "arn:aws:iam::***:role/test-log-role"))
        .memorySize(512)
        .profiling(false)
        .profilingGroup(ProfilingGroup.fromProfilingGroupName(stack, "test-profiling-group", "test-profiling-group"))
        .reservedConcurrentExecutions(2)
        .role(Role.fromRoleArn(stack, "test-role", "arn:aws:iam::***:role/test-role"))
        .securityGroups(List.of(SecurityGroup.fromSecurityGroupId(stack, "test-security-group", "sg-***")))
        .tracing(Tracing.ACTIVE)
        .vpc(Vpc.Builder.create(stack, "test-vpc").build())
        .vpcSubnets(SubnetSelection.builder().build())
        .code(Code.fromAsset(lambdaCodePath.toString()))
        .handler("io.sandpipers.springnativeawslambda.infra.lambda.CustomRuntime2Function::handleRequest");
  }

  @Test
  void should_create_and_return_function() {
    final CustomRuntime2023FunctionProps functionProps = functionPropsBuilder.build();
    final Function actual = new CustomRuntime2023Function<>(stack, id, functionProps).getFunction();

    assertThat(actual)
        .isNotNull();

    assertThat(actual.getRuntime())
        .isEqualTo(PROVIDED_AL2023);
  }

  @Test
  void should_create_and_return_function_with_non_default_timeout() {
    final Duration timeout = Duration.seconds(3);
    final CustomRuntime2023FunctionProps functionProps = (CustomRuntime2023FunctionProps) functionPropsBuilder
        .timeout(timeout)
        .build();

    final Function actual = new CustomRuntime2023Function<>(stack, id, functionProps).getFunction();

    assertThat(actual)
        .isNotNull();

    assertThat(actual.getTimeout()
        .toSeconds())
        .isEqualTo(3);
  }

  @Test
  @Disabled
  void should_throw_exception_when_function_handler_is_null() {

    assertThatThrownBy(() -> functionPropsBuilder.handler(null).build())
        .isNotNull()
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("may not be null");
  }

  @Test
  void should_throw_exception_when_function_code_is_null() {

    assertThatThrownBy(() -> functionPropsBuilder.code(null).build())
        .isNotNull()
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("may not be null");
  }

  @Test
  void should_throw_exception_when_function_description_is_null() {
    assertThatThrownBy(() -> functionPropsBuilder.description(null).build())
        .isNotNull()
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("may not be null");
  }

  @Test
  @Disabled
  void should_throw_exception_when_function_memory_size_is_less_than_128() {

    assertThatThrownBy(() -> functionPropsBuilder.memorySize(120).build())
        .isNotNull()
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("'memorySize' must be between 128 and 3008 (inclusive)");
  }

  @Test
  @Disabled
  void should_throw_exception_when_function_memory_size_is_larger_than_3008() {
    assertThatThrownBy(() -> functionPropsBuilder.memorySize(3500).build())
        .isNotNull()
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("'memorySize' must be between 128 and 3008 (inclusive)");
  }

  @Test
  @Disabled
  void should_throw_exception_when_function_memory_size_is_less_than_zero() {
    assertThatThrownBy(() -> functionPropsBuilder.retryAttempts(-1).build())
        .isNotNull()
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("'retryAttempts' must be between 128 and 3008 (inclusive)");
  }

  @Test
  @Disabled
  void should_throw_exception_when_function_memory_size_is_larger_than_two() {
    assertThatThrownBy(() -> functionPropsBuilder.retryAttempts(3).build())
        .isNotNull()
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("'retryAttempts' must be between 128 and 3008 (inclusive)");
  }

  @Test
  @Disabled
  void should_throw_exception_when_max_event_age_is_less_than_60() {
    assertThatThrownBy(() -> functionPropsBuilder.maxEventAge(59).build())
        .isNotNull()
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("'retryAttempts' must be between 60 and 21600 (inclusive)");
  }

  @Test
  @Disabled
  void should_throw_exception_when_max_event_age_is_less_than_21600() {

    final AbstractCustomRuntimeFunctionProps props = functionPropsBuilder.maxEventAge(21601).build();

    assertThatThrownBy(() -> new CustomRuntime2023Function<>(stack, id, (CustomRuntime2023FunctionProps)props))
        .isNotNull()
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("`maximumEventAge` must represent a `Duration` that is between 60 and 21600 seconds");
  }
}