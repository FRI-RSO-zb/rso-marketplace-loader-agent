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
}
