package io.enotion.demo.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class GrpcPropertiesProvider {
    public GrpcChannelProperties getProperties() throws Exception{
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory()); // jackson databind
        var resource =  getClass().getClassLoader().getResource("client.yaml");
        return mapper.readValue(new File(resource.toURI()), GrpcChannelProperties.class);

    }
}
