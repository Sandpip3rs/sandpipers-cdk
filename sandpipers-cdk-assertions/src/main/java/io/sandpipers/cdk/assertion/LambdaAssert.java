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
 * Fluent assertions for <code>AWS::Lambda::Function</code>. This should be used if the resource map is extracted from the AWS template. Otherwise, start with
 * {@link CDKStackAssert#containsFunction(String)}.
 */
@SuppressWarnings("unchecked")
public class LambdaAssert extends AbstractCDKResourcesAssert<LambdaAssert, Map<String, Object>> {

  private LambdaAssert(final Map<String, Object> actual) {
    super(actual, LambdaAssert.class);
  }

  public static LambdaAssert assertThat(final Map<String, Object> actual) {
    return new LambdaAssert(actual);
  }

  public LambdaAssert hasFunction(final String expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");

    Assertions.assertThat(properties.get("FunctionName"))
        .isInstanceOf(String.class)
        .matches(expected::equals);

    return this;
  }

  public LambdaAssert hasHandler(final String expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final String handler = (String) properties.get("Handler");

    Assertions.assertThat(handler)
        .isInstanceOf(String.class)
        .isEqualTo(expected);

    return this;
  }

  public LambdaAssert hasCode(final String s3Bucket, final String s3Key) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Object> lambdaCode = (Map<String, Object>) properties.get("Code");

    String actualS3Bucket = ((Map<String, String>) lambdaCode.get("S3Bucket")).get("Fn::Sub");
    String actualS3Key = (String) lambdaCode.get("S3Key");

    Assertions.assertThat(actualS3Bucket)
        .matches(s3Bucket);

    Assertions.assertThat(actualS3Key)
        .matches(s3Key);

    return this;
  }

  public LambdaAssert hasRetryAttempts(final Integer expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Integer retryAttempts = (Integer) properties.get("retryAttempts");

    Assertions.assertThat(retryAttempts)
        .isInstanceOf(Integer.class)
        .isEqualTo(expected);

    return this;
  }

  public LambdaAssert hasMaxAgeEvent(final Integer expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Integer memorySize = (Integer) properties.get("maxAgeEvent");

    Assertions.assertThat(memorySize)
        .isInstanceOf(Integer.class)
        .isEqualTo(expected);

    return this;
  }

  public LambdaAssert hasRole(final String arnRegex) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Object> role = (Map<String, Object>) properties.get("Role");
    final List<String> roleArnFun = (List<String>) role.get("Fn::GetAtt");

    Assertions.assertThat(roleArnFun)
        .isInstanceOf(List.class)
        .anySatisfy(s -> Assertions.assertThat(s)
            .isInstanceOf(String.class)
            .matches(e -> e.matches(arnRegex)));

    return this;
  }

  public LambdaAssert hasDeadLetterTarget(final String targetArn) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Object> role = (Map<String, Object>) properties.get("DeadLetterConfig");
    final Map<String, Object> actualTargetArn = (Map<String, Object>) role.get("TargetArn");

    if(actualTargetArn.containsKey("Fn::GetAtt")) {
      final List<String> list = (List<String>) actualTargetArn.get("Fn::GetAtt");
      Assertions.assertThat(list)
          .isNotEmpty()
          .anySatisfy(e -> Assertions.assertThat(e).matches(targetArn));
    } else {
      Assertions.assertThat(actualTargetArn)
          .isInstanceOf(Map.class)
          .anySatisfy((key, value) -> Assertions.assertThat(key)
              .matches(e -> e.matches("Ref") && String.valueOf(value).matches(targetArn)));
    }

    return this;
  }
}
