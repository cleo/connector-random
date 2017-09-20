package com.cleo.labs.connector.random;

import java.io.IOException;

import com.cleo.connector.api.ConnectorConfig;
import com.cleo.connector.api.annotations.Client;
import com.cleo.connector.api.annotations.Connector;
import com.cleo.connector.api.annotations.ExcludeType;
import com.cleo.connector.api.annotations.Info;
import com.cleo.connector.api.annotations.Property;
import com.cleo.connector.api.interfaces.IConnectorProperty;
import com.cleo.connector.api.property.CommonProperties;
import com.cleo.connector.api.property.CommonProperty;
import com.cleo.connector.api.property.PropertyBuilder;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

@Connector(scheme = "random", description = "Random Content Streams",
           excludeType = { @ExcludeType(type = ExcludeType.SentReceivedBoxes),
                           @ExcludeType(type = ExcludeType.Exchange) })
@Client(RandomConnectorClient.class)
public class RandomConnectorSchema extends ConnectorConfig {
    @Property
    final IConnectorProperty<Long> seed = new PropertyBuilder<>("Seed", 0L)
            .setDescription("The random seed.")
            .build();

    @Property
    final IConnectorProperty<String> length = new PropertyBuilder<>("Length", "1k")
            .setDescription("The number of random bytes.")
            .addPossibleRegexes("\\d+(?i:[kmgt]b?)?")
            .setRequired(true)
            .build();

    @Property
    final IConnectorProperty<Boolean> match = new PropertyBuilder<>("Match", true)
            .setDescription("Check that bytes written match expected based on seed.")
            .build();

    @Property
    final IConnectorProperty<Boolean> enableDebug = CommonProperties.of(CommonProperty.EnableDebug);

    @Info
    protected static String info() throws IOException {
        return Resources.toString(RandomConnectorSchema.class.getResource("info.txt"), Charsets.UTF_8);
    }
}
