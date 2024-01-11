package net.bobnar.marketplace.loaderAgent.services.config;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ConfigBundle("service.config")
@ApplicationScoped
public class ServiceConfig {

    @ConfigValue("enabled")
    private boolean enabled;

    @ConfigValue("useInternalResources")
    private boolean useInternalResources = true;

    @ConfigValue("intentionalyBrokenCircuit")
    private boolean intentionalyBrokenCircuit = false;

    @ConfigValue("catalogServiceUrl")
    private String catalogServiceUrl = "http://localhost:8801/v1";

    public boolean isEnabled() {
        return this.enabled;
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    public boolean shouldUseInternalResources() {
        return this.useInternalResources;
    }

    public void useInternalResources() {
        this.useInternalResources = true;
    }

    public void useExternalResources() {
        this.useInternalResources = false;
    }

    public boolean isIntentionalyBrokenCircuit() {
        return intentionalyBrokenCircuit;
    }

    public void setIntentionalyBrokenCircuit(boolean intentionalyBrokenCircuit) {
        this.intentionalyBrokenCircuit = intentionalyBrokenCircuit;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isUseInternalResources() {
        return useInternalResources;
    }

    public void setUseInternalResources(boolean useInternalResources) {
        this.useInternalResources = useInternalResources;
    }

    public String getCatalogServiceUrl() {
        return catalogServiceUrl;
    }

    public void setCatalogServiceUrl(String catalogServiceUrl) {
        this.catalogServiceUrl = catalogServiceUrl;
    }
}
