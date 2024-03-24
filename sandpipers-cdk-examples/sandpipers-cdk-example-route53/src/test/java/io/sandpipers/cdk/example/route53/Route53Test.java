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

package io.sandpipers.cdk.example.route53;

import static io.sandpipers.cdk.assertion.CDKStackAssert.assertThat;
import static io.sandpipers.cdk.core.AbstractApp.tagResources;
import static io.sandpipers.cdk.example.route53.Environment.SANDPIPERS_TEST_111111111111_AP_SOUTHEAST_2;

import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import software.amazon.awscdk.assertions.Template;

public class Route53Test extends TemplateSupport {

  @BeforeAll
  static void initAll() {

    final Application app = new Application();

    final Route53Stack route53Stack = new Route53Stack(app, SANDPIPERS_TEST_111111111111_AP_SOUTHEAST_2);
    tagResources(route53Stack, SANDPIPERS_TEST_111111111111_AP_SOUTHEAST_2, app.getApplicationName());

    template = Template.fromStack(route53Stack);
  }

  @Test
  void should_have_public_a_record() {
    assertThat(template)
        .containsRecordSet("^PublicARecord[a-zA-Z0-9]{8}$")
        .hasName("route53-cdk-example.sandpipers.yeah.")
        .hasHostedZone("^PublicHostedZone[a-zA-Z0-9]{8}$")
        .hasResourceRecords(List.of("services.load.balancer"))
        .hasType("A")
        .hasTtl("1800")
        .hasUpdateReplacePolicy("Delete")
        .hasDeletionPolicy("Delete");
  }

  @Test
  void should_have_private_a_record() {
    assertThat(template)
        .containsRecordSet("^PrivateARecord[a-zA-Z0-9]{8}$")
        .hasName("route53-cdk-example.sandpipers.yeah.")
        .hasHostedZone("^PrivateHostedZone[a-zA-Z0-9]{8}$")
        .hasResourceRecords(List.of("services.load.balancer"))
        .hasType("A")
        .hasTtl("1800")
        .hasUpdateReplacePolicy("Delete")
        .hasDeletionPolicy("Delete");
  }

  @Test
  void should_have_public_a_hosted_zone() {
    assertThat(template)
        .containsHostedZone("^PublicHostedZone[a-zA-Z0-9]{8}$")
        .hasName("sandpipers.yeah.")
        .hasTag("APPLICATION_NAME", "route53-cdk-example")
        .hasTag("ENVIRONMENT", "TEST")
        .hasTag("COST_CENTRE", "Sandpipers");
  }

  @Test
  void should_have_private_a_hosted_zone() {
    assertThat(template)
        .containsHostedZone("^PrivateHostedZone[a-zA-Z0-9]{8}$")
        .hasName("sandpipers.yeah.")
        .hasTag("APPLICATION_NAME", "route53-cdk-example")
        .hasTag("ENVIRONMENT", "TEST")
        .hasTag("COST_CENTRE", "Sandpipers");
  }
}
