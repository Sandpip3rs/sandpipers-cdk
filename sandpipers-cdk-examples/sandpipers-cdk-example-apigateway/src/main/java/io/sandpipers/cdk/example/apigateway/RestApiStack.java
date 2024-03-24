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
import io.sandpipers.cdk.core.construct.apigateway.RestApi;
import io.sandpipers.cdk.core.construct.apigateway.RestApi.RestApiProps;
import io.sadpipers.cdk.type.SafeString;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.services.apigateway.DomainNameOptions;
import software.amazon.awscdk.services.apigateway.Integration;
import software.amazon.awscdk.services.apigateway.IntegrationType;
import software.amazon.awscdk.services.apigateway.Resource;
import software.amazon.awscdk.services.apigateway.StageOptions;
import software.amazon.awscdk.services.certificatemanager.Certificate;

public class RestApiStack extends BaseStack {

  public RestApiStack(@NotNull final AbstractApp app, @NotNull final Environment environment) {
    super(app, environment);

    final Integration integration = Integration.Builder.create()
        .integrationHttpMethod("ANY")
        .type(IntegrationType.MOCK)
        .build();

    final String domainName = "sandpipers.yeah";
    final Certificate certificate = Certificate.Builder.create(this, "Certificate")
        .domainName(domainName)
        .build();

    final DomainNameOptions domainNameOptions = DomainNameOptions.builder()
        .domainName(domainName)
        .certificate(certificate).build();

    final RestApiProps restApiProps = RestApiProps.builder()
        .deployOptions(StageOptions.builder().stageName("Test").build())
        .defaultIntegration(integration)
        .domainName(domainNameOptions)
        .build();

    final RestApi restApi = new RestApi(this, SafeString.of("RestApi"), restApiProps);

    final Resource users = restApi.getRestApi().getRoot().addResource("users");
    users.addMethod("GET");
    users.addMethod("POST");

    users.addResource("{id}")
        .addMethod("GET");
  }
}
