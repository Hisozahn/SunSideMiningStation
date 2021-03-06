package SunSideMiningStation.Jersey;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jaxrs.Jaxrs2TypesModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

class ObjectMapperFactory {

    ObjectMapper buildObjectMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jaxrs2TypesModule());
        mapper.registerModule(new ParameterNamesModule());
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}