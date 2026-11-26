package com.proyectoBD2.APIrest;

import org.bson.types.ObjectId;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ser.std.ToStringSerializer;

@SpringBootApplication
public class ApIrestApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApIrestApplication.class, args);
	}
	@Bean
    public SimpleModule objectIdModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(ObjectId.class, ToStringSerializer.instance);
        return module;
    }


}
