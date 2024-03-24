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
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;

@SuppressWarnings("unchecked")
public abstract class AbstractCDKResourcesAssert<SELF extends AbstractCDKResourcesAssert<SELF, ACTUAL>, ACTUAL extends Map<String, Object>> extends
    AbstractAssert<SELF, ACTUAL> {

  protected AbstractCDKResourcesAssert(ACTUAL actual, Class<?> selfType) {
    super(actual, selfType);
  }

  public SELF hasTag(final String key, final Object value) {
    hasTag("Tags", key, value);
    return myself;
  }

  public SELF hasTag(final String tagsMapId,
      final String key,
      final Object value) {

    final Map<String, Map<String, Object>> properties =
        (Map<String, Map<String, Object>>) actual.get("Properties");

    final List<Object> tags = (List<Object>) properties.get(tagsMapId);

    Assertions.assertThat(tags)
        .isNotEmpty()
        .contains(Map.of("Key", key, "Value", value));
    return myself;
  }

  public SELF hasDependency(final String expected) {

    final List<String> actualDependency = (List<String>) actual.get("DependsOn");

    Assertions.assertThat(actualDependency)
        .isInstanceOf(List.class)
        .anyMatch(s -> s.matches(expected));

    return myself;
  }

  public SELF hasUpdateReplacePolicy(final String expected) {

    final String actualUpdateReplacePolicy = (String) actual.get("UpdateReplacePolicy");

    Assertions.assertThat(actualUpdateReplacePolicy)
        .isInstanceOf(String.class)
        .isEqualTo(expected);

    return myself;
  }

  public SELF hasDeletionPolicy(final String expected) {

    final String actualDeletionPolicy = (String) actual.get("DeletionPolicy");

    Assertions.assertThat(actualDeletionPolicy)
        .isInstanceOf(String.class)
        .isEqualTo(expected);

    return myself;
  }

  public SELF hasEnvironmentVariable(final String key, final String value) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Map<String, Object> environment = (Map<String, Object>) properties.get("Environment");
    final Map<String, Object> variables = (Map<String, Object>) environment.get("Variables");

    Assertions.assertThat(variables.get(key))
        .isInstanceOf(String.class)
        .isEqualTo(value);

    return myself;
  }

  public SELF hasDescription(final String expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final String description = (String) properties.get("Description");

    Assertions.assertThat(description)
        .isInstanceOf(String.class)
        .isEqualTo(expected);

    return myself;
  }

  public SELF hasMemorySize(final Integer expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Integer memorySize = (Integer) properties.get("MemorySize");

    Assertions.assertThat(memorySize)
        .isInstanceOf(Integer.class)
        .isEqualTo(expected);

    return myself;
  }

  public SELF hasRuntime(final String expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final String runtime = (String) properties.get("Runtime");

    Assertions.assertThat(runtime)
        .isInstanceOf(String.class)
        .isEqualTo(expected);

    return myself;
  }

  public SELF hasTimeout(final Integer timeoutInSeconds) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final Integer timeout = (Integer) properties.get("Timeout");

    Assertions.assertThat(timeout)
        .isInstanceOf(Integer.class)
        .isEqualTo(timeoutInSeconds);

    return myself;
  }

  public SELF hasPolicy(final Map<String, String> principal,
      final String resource,
      final String effect,
      final String policyDocumentVersion,
      final String action,
      final Map<String, Object> policyDocument) {

    Assertions.assertThat(policyDocument)
        .isNotNull()
        .isNotEmpty()
        .containsEntry("Version", policyDocumentVersion);

    List<Map<String, Object>> statements = (List<Map<String, Object>>) policyDocument.get("Statement");

    Assertions.assertThat(statements)
        .isNotNull()
        .isNotEmpty();

    Assertions.assertThat(statements).allSatisfy(s -> Assertions.assertThat(s)
        .isNotNull()
        .isNotEmpty()
        .containsEntry("Effect", effect)
        .containsEntry("Action", action));

    Assertions.assertThat(statements)
        .anySatisfy(s -> {
          if (principal == null) {
            return;
          }

          Assertions.assertThat(s)
              .extracting("Principal")
              .asInstanceOf(InstanceOfAssertFactories.MAP)
              .matches(e -> e.keySet().containsAll(principal.keySet()) && e.values().containsAll(principal.values()));
        });

    Assertions.assertThat(statements)
        .anySatisfy(s -> {
          if (resource == null) {
            return;
          }

          final Map<String, Object> actualResource = (Map<String, Object>) s.get("Resource");

          if (actualResource.containsKey("Fn::GetAtt")) {
            Assertions.assertThat(actualResource)
                .isNotEmpty()
                .flatExtracting("Fn::GetAtt")
                .map(e -> (String) e)
                .anySatisfy(e -> Assertions.assertThat(e).matches(resource));
          } else {
            Assertions.assertThat(actualResource)
                .isNotEmpty()
                .extracting("Ref")
                .asInstanceOf(InstanceOfAssertFactories.STRING)
                .matches(e -> e.matches(resource));
          }
        });

    return myself;
  }

  protected String flattenSourceArn(final Map<String, Object> sourceArnMap) {
    if (org.apache.commons.collections4.MapUtils.isEmpty(sourceArnMap)) {
      return "";
    }
    // Get the Fn::Join list from the SourceArn map
    final List<Object> joinList = (List<Object>) sourceArnMap.get("Fn::Join");

    // Create a StringBuilder to construct the ARN
    final StringBuilder arnBuilder = new StringBuilder();

    // Concatenate the elements of the joinList
    for (final Object element : joinList) {
      if (element instanceof String) {
        arnBuilder.append(element);
      } else if (element instanceof List) {
        // Handle nested lists
        for (final Object nestedElement : (List<Object>) element) {
          if (nestedElement instanceof String) {
            arnBuilder.append(nestedElement);
          } else if (nestedElement instanceof Map) {
            final Map<String, String> nestedMap = (Map<String, String>) nestedElement;
            final String refValue = nestedMap.get("Ref");
            if (refValue != null) {
              arnBuilder.append(refValue);
            }
          }
        }
      } else if (element instanceof Map) {
        // Handle nested maps
        final Map<String, String> nestedMap = (Map<String, String>) element;
        final String refValue = nestedMap.get("Ref");
        if (refValue != null) {
          arnBuilder.append(refValue);
        }
      }
    }

    // Get the final flattened ARN string
    return arnBuilder.toString()
        .replace("AWS::Partition", "aws")
        .replace("AWS::Region", "");
  }
}
