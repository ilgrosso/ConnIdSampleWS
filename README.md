# ConnIdSampleWS

The [ConnId SOAP connector](https://github.com/Tirasa/ConnIdSOAPBundle) is built to work against a pre-defined WSDL.

Problems arise when there are existing SOAP services to connect to, via ConnId.

This project shows how to rework the ConnId SOAP connector to support existing SOAP services.

## Modules

* `connector`
  
  The actual SOAP connector, working with the Java client classes dynamicaly generated via Maven under the `target/generated-sources` folder from the WSDL file under `src/main/resources/users.wsdl`
* `service`
  
  Sample SOAP service made with [Apache CXF](http://cxf.apache.org): start with `mvn clean tomcat7:run`
