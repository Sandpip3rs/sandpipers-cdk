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
 * Fluent assertions for <code>AWS::SNS::Topic</code>. This should be used if the resource map is extracted from the AWS template. Otherwise, start with
 * {@link CDKStackAssert#containsTopic(String)}.
 */
@SuppressWarnings("unchecked")
public class TopicAssert extends AbstractCDKResourcesAssert<TopicAssert, Map<String, Object>> {

  private TopicAssert(final Map<String, Object> actual) {
    super(actual, TopicAssert.class);
  }

  public static TopicAssert assertThat(final Map<String, Object> actual) {
    return new TopicAssert(actual);
  }

  public TopicAssert hasTopicName(final String expected) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");

    final String topicName = (String) properties.get("TopicName");

    Assertions.assertThat(topicName)
        .containsPattern(expected);

    return this;
  }

  public TopicAssert isFifo(final boolean expected) {

    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Boolean isFifo = (Boolean) properties.get("FifoTopic");

    Assertions.assertThat(isFifo)
        .isEqualTo(expected);

    return this;
  }

  public TopicAssert hasContentBasedDeduplicationEnabled(final boolean expected) {

    final Map<String, Boolean> properties = (Map<String, Boolean>) actual.get("Properties");
    final Boolean isFifo = properties.get("ContentBasedDeduplication");

    Assertions.assertThat(isFifo)
        .isEqualTo(expected);

    return this;
  }

  public TopicAssert hasMessageRetentionPeriodInDays(final Number expected) {

    final Map<String, Number> properties = (Map<String, Number>) actual.get("Properties");
    final Map<String, Number> archivePolicy = (Map<String, Number>) properties.get("ArchivePolicy");
    final Number messageRetentionPeriod = archivePolicy.get("MessageRetentionPeriod");

    Assertions.assertThat(messageRetentionPeriod)
        .isEqualTo(expected);

    return this;
  }

  public TopicAssert hasMasterKey(final String expected) {

    final Map<String, Number> properties = (Map<String, Number>) actual.get("Properties");
    final Map<String, Object> kmsMasterKeyId = (Map<String, Object>) properties.get("KmsMasterKeyId");
    final List<String> keyArnFun = (List<String>) kmsMasterKeyId.get("Fn::GetAtt");

    Assertions.assertThat(keyArnFun)
        .isInstanceOf(List.class)
        .anySatisfy(s -> Assertions.assertThat(s)
            .isInstanceOf(String.class)
            .containsPattern(expected));

    return this;
  }
}
