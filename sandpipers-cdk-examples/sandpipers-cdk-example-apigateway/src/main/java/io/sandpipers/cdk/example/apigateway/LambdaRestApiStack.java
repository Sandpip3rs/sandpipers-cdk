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

package io.sandpipers.cdk.example.apigateway;

import io.sandpipers.cdk.core.AbstractApp;
import io.sandpipers.cdk.core.construct.BaseStack;
import io.sandpipers.cdk.core.construct.lambda.CustomRuntime2023Function;
import io.sandpipers.cdk.core.construct.lambda.CustomRuntime2023Function.CustomRuntime2023FunctionProps;
import io.sadpipers.cdk.type.SafeString;
import java.io.IOException;
import java.nio.file.Path;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.services.apigateway.LambdaRestApiProps;
import software.amazon.awscdk.services.apigateway.StageOptions;
import software.amazon.awscdk.services.lambda.Code;

public class LambdaRestApiStack extends BaseStack {

  public LambdaRestApiStack(@NotNull final AbstractApp app, @NotNull final Environment environment) {
    super(app, environment);

    try {
      final String testLambdaCodePath = TestLambdaUtils.getTestLambdaCodePath(Path.of(System.getProperty("java.io.tmpdir")))
          .toFile().getPath();

      final CustomRuntime2023FunctionProps functionProps = CustomRuntime2023FunctionProps.builder()
          .description("Test Function for CDK")
          .handler("org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest")
          .code(Code.fromAsset(testLambdaCodePath))
          .build();

      final CustomRuntime2023Function<CustomRuntime2023FunctionProps> function =
          new CustomRuntime2023Function<>(this, SafeString.of("Function"), functionProps);

      // To test {@link LambdaPermissionAssert}
      final LambdaRestApiProps lambdaRestApiProps = LambdaRestApiProps.builder()
          .handler(function.getFunction())
          .deployOptions(StageOptions.builder().stageName("Test").build())
          .build();

      new io.sandpipers.cdk.core.construct.apigateway.LambdaRestApi(this, SafeString.of("LambdaRestApi"), lambdaRestApiProps);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
