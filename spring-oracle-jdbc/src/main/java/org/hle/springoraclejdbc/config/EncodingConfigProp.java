package org.hle.springoraclejdbc.config;

import lombok.Getter;
import lombok.Setter;
import org.hle.springoraclejdbc.model.QueryMode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "encoding.adapter")
public class EncodingConfigProp {
    private String databaseCodePage;
    private String valueEncoding;
    private QueryMode mode;
}
