package com.example.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "test")
public class SectionConfigurationProperties {

    private Map<String, Props> section;

    public Map<String, Props> getSection() {
        return section;
    }

    public void setSection(Map<String, Props> section) {
        this.section = section;
    }

    public static class Props {
        private String propA;
        private String propB;

        public String getPropA() {
            return propA;
        }

        public void setPropA(String propA) {
            this.propA = propA;
        }

        public String getPropB() {
            return propB;
        }

        public void setPropB(String propB) {
            this.propB = propB;
        }
    }
}
