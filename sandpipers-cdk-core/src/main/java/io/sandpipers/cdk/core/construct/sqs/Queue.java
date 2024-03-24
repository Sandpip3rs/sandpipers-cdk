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

import io.sandpipers.cdk.core.construct.sqs.Queue.QueueProps;
import io.sadpipers.cdk.type.SafeString;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import software.constructs.Construct;

/**
 * L3 Construct representing AWS::SQS::Queue (not FIFO)
 *<p> Example usage can be found in <a href="https://github.com/muhamadto/sandpipers-cdk/blob/main/sandpipers-cdk-examples/sandpipers-cdk-example-sqs/src/main/java/com/sandpipers/cdk/example/queue/QueueStack.java">sandpipers-cdk-example-sqs</a></p>
 */
public class Queue<T extends QueueProps> extends AbstractQueue<T> {

  public Queue(@NotNull final Construct scope,
      @NotNull final SafeString id,
      @NotNull final T props) {
    super(scope, id, props);
  }

  @Getter
  @SuperBuilder
  public static class QueueProps extends AbstractQueueProps {

  }
}
