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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import software.amazon.awscdk.services.sqs.RedriveAllowPolicy;

/**
 * Fluent assertions for <code>AWS::SQS::Queue</code>. This should be used if the resource map is extracted from the AWS template. Otherwise, start with
 * {@link CDKStackAssert#containsQueue(String)}.
 */
@SuppressWarnings("unchecked")
public class QueueAssert extends AbstractCDKResourcesAssert<QueueAssert, Map<String, Object>> {

  private QueueAssert(final Map<String, Object> actual) {
    super(actual, QueueAssert.class);
  }

  public static QueueAssert assertThat(final Map<String, Object> actual) {
    return new QueueAssert(actual);
  }

  public QueueAssert hasDeadLetterQueue(final String expected) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, List<Object>> redrivePolicy = (Map<String, List<Object>>) properties.get("RedrivePolicy");
    final Map<String, Object> deadLetterTargetArn = (Map<String, Object>) redrivePolicy.get("deadLetterTargetArn");
    final List<String> roleArnFun = (List<String>) deadLetterTargetArn.get("Fn::GetAtt");

    Assertions.assertThat(roleArnFun)
        .isInstanceOf(List.class)
        .anySatisfy(s -> Assertions.assertThat(s)
            .isInstanceOf(String.class)
            .matches(e -> e.matches(expected)));

    return this;
  }

  public QueueAssert isFifo(final boolean expected) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Boolean isFifo = (Boolean) properties.get("FifoQueue");

    Assertions.assertThat(isFifo)
        .isEqualTo(expected);

    return this;
  }

  public QueueAssert hasRedriveAllowPolicy(final RedriveAllowPolicy expected) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Object> redriveAllowPolicy = (Map<String, Object>) properties.get("RedriveAllowPolicy");

    Assertions.assertThat(redriveAllowPolicy)
        .isEqualTo(new ObjectMapper().convertValue(expected, Map.class));

    return this;
  }

  public QueueAssert hasContentBasedDeduplicationEnabled(final boolean expected) {

    final Map<String, Boolean> properties = (Map<String, Boolean>) actual.get("Properties");
    final Boolean isFifo = properties.get("ContentBasedDeduplication");

    Assertions.assertThat(isFifo)
        .isEqualTo(expected);

    return this;
  }

  public QueueAssert hasDeduplicationScope(final String expected) {

    final Map<String, String> properties = (Map<String, String>) actual.get("Properties");
    final String deduplicationScope = properties.get("DeduplicationScope");

    Assertions.assertThat(deduplicationScope)
        .isEqualTo(expected);

    return this;
  }

  public QueueAssert hasFifoThroughputLimit(final String expected) {

    final Map<String, String> properties = (Map<String, String>) actual.get("Properties");
    final String fifoThroughputLimit = properties.get("FifoThroughputLimit");

    Assertions.assertThat(fifoThroughputLimit)
        .isEqualTo(expected);

    return this;
  }

  public QueueAssert hasMaxRetrialCount(final Integer deletionPolicy) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Integer> redrivePolicy = (Map<String, Integer>) properties.get("RedrivePolicy");
    final Integer maxReceiveCount = redrivePolicy.get("maxReceiveCount");

    Assertions.assertThat(maxReceiveCount)
        .isEqualTo(deletionPolicy);

    return this;
  }
}
