package com.amarmodi.gateway.routes;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.remote.ConsulConfigurationDefinition;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class RouteGateway extends RouteBuilder {
 
	@Autowired
	private CamelContext context;

	@Autowired
	private Environment environment;
	
    @Override
    public void configure() throws Exception {
		ConsulConfigurationDefinition config = new ConsulConfigurationDefinition();
		config.setComponent("netty4-http");
		config.setUrl(environment.getProperty("consul.server"));
		context.setServiceCallConfiguration(config);
        
		restConfiguration()
		.component("netty4-http")
		.bindingMode(RestBindingMode.json)
		.port(8000);
		
		from("rest:get:account:/customerxyz/{customerId}").serviceCall("account");
		from("rest:get:account:/getAll/{id}").serviceCall("account");
		from("rest:post:account:/create/{post}").serviceCall("account");
		from("rest:get:account:/{id}").serviceCall("account");
	}

}