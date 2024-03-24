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
 * Fluent assertions for <code>AWS::Route53::RecordSet</code>. This should be used if the resource map is extracted from the AWS template. Otherwise, start with
 * <ul>
 *   <li>{@link CDKStackAssert#containsRecordSet(String)}</li>
 * </ul>
 */
@SuppressWarnings("unchecked")
public class RecordSetAssert extends AbstractCDKResourcesAssert<RecordSetAssert, Map<String, Object>> {

  private RecordSetAssert(final Map<String, Object> actual) {
    super(actual, RecordSetAssert.class);
  }

  public static RecordSetAssert assertThat(final Map<String, Object> actual) {
    return new RecordSetAssert(actual);
  }

  public RecordSetAssert hasName(final String expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final String name = (String) properties.get("Name");

    Assertions.assertThat(name)
        .isNotBlank()
        .matches(expected);

    return this;
  }

  public RecordSetAssert hasType(final String expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final String type = (String) properties.get("Type");

    Assertions.assertThat(type)
        .isNotBlank()
        .matches(expected);

    return this;
  }

  public RecordSetAssert hasTtl(final String expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final String ttl = (String) properties.get("TTL");

    Assertions.assertThat(ttl)
        .isNotNull()
        .isEqualTo(expected);

    return this;
  }

  public RecordSetAssert hasHostedZone(final String id) {
    final Map<String, List<Object>> properties = ((Map<String, List<Object>>) actual.get("Properties"));
    final Map<String, String> hostedZoneId = (Map<String, String>)properties.get("HostedZoneId");

    Assertions.assertThat(hostedZoneId)
        .isNotNull()
        .isNotEmpty()
        .anySatisfy((k,v) -> Assertions.assertThat(v).matches(id));

    return this;
  }

  public RecordSetAssert hasResourceRecords(final List<String> expected) {
    final Map<String, Object> properties = (Map<String, Object>) actual.get("Properties");
    final List<String> resourceRecords = (List<String>) properties.get("ResourceRecords");

    Assertions.assertThat(resourceRecords)
        .isNotNull()
        .isNotEmpty()
        .containsAll(expected);

    return this;
  }
}
