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

import io.sandpipers.cdk.core.construct.sns.Topic.TopicProps;
import io.sadpipers.cdk.type.SafeString;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import software.constructs.Construct;

/**
 * L3 Construct representing AWS::SNS::Topic (not FIFO)
 *<p> Example usage can be found in <a href="https://github.com/muhamadto/sandpipers-cdk/blob/main/sandpipers-cdk-examples/sandpipers-cdk-example-sns/src/main/java/com/sandpipers/cdk/example/sns/TopicStack.java">sandpipers-cdk-example-sns</a></p>
 */
public class Topic<T extends TopicProps> extends AbstractTopic<T> {

  public Topic(@NotNull final Construct scope,
      @NotNull final SafeString id,
      @NotNull final T props) {
    super(scope, id, props);
  }

  @Getter
  @SuperBuilder
  public static class TopicProps extends AbstractTopicProps {

  }
}
