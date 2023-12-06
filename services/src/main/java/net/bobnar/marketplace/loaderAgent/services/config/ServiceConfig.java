package net.bobnar.marketplace.loaderAgent.services.config;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;

import javax.enterprise.context.ApplicationScoped;

@ConfigBundle("service-config")
@ApplicationScoped
public class ServiceConfig {

    private boolean enabled;

    public boolean isEnabled() {
        return this.enabled;
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

}
