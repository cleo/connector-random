package com.cleo.labs.connector.random;

import com.cleo.connector.api.property.ConnectorPropertyException;

public class RandomConnectorConfig {
    private RandomConnectorClient client;
    private RandomConnectorSchema schema;

    public RandomConnectorConfig(RandomConnectorClient client, RandomConnectorSchema schema) {
        this.client = client;
        this.schema = schema;
    }

    public long getSeed() throws ConnectorPropertyException {
        return schema.seed.getValue(client);
    }

    public String getLength() throws ConnectorPropertyException {
        return schema.length.getValue(client);
    }

    public boolean getMatch() throws ConnectorPropertyException {
        return schema.match.getValue(client);
    }
}
