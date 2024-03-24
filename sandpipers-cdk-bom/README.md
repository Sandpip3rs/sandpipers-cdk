# sandpipers-cdk-bom

This project is a Bill of Materials (BOM) for the Sandpipers CDK examples. It is a Maven project that defines the versions of the CDK libraries used
in the project.

### Usage

To use this BOM in your project, add the following dependency to your `pom.xml` file:

```xml
<dependency>
  <groupId>io.sandpipers</groupId>
  <artifactId>sandpipers-cdk-bom</artifactId>
  <version>version</version>
  <type>pom</type>
  <scope>import</scope>
</dependency>
```