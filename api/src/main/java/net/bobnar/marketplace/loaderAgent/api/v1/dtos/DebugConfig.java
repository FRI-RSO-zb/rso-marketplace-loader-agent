package net.bobnar.marketplace.loaderAgent.api.v1.dtos;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema
public class DebugConfig {
    public boolean enabled;
    public boolean useInternalResources;
    public boolean intentionalyBrokenCircuit;

    public DebugConfig() {}

    public DebugConfig(boolean enabled, boolean useInternalResources, boolean intentionalyBrokenCircuit) {
        this.enabled = enabled;
        this.useInternalResources = useInternalResources;
        this.intentionalyBrokenCircuit = intentionalyBrokenCircuit;
    }

    public boolean isEnabled() {
        return enabled;
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

    public boolean isIntentionalyBrokenCircuit() {
        return intentionalyBrokenCircuit;
    }

    public void setIntentionalyBrokenCircuit(boolean intentionalyBrokenCircuit) {
        this.intentionalyBrokenCircuit = intentionalyBrokenCircuit;
    }
}
