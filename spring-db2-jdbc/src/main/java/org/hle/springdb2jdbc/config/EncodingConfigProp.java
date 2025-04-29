package org.hle.springdb2jdbc.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "encoding.adapter")
public class EncodingConfigProp {
    private String databaseCodePage;
    private String valueEncoding;
}
