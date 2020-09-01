package de.fhg.iais.roberta.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.fhg.iais.roberta.components.ConfigurationComponent;

public class NewUsedHardwareBean implements IProjectBean {
    private final List<ConfigurationComponent> usedConfigurationComponents = new ArrayList<>();

    public List<ConfigurationComponent> getUsedConfigurationComponents() {
        return Collections.unmodifiableList(this.usedConfigurationComponents);
    }

    public static class Builder implements IBuilder<NewUsedHardwareBean> {
        private final NewUsedHardwareBean usedHardwareBean = new NewUsedHardwareBean();

        public NewUsedHardwareBean.Builder addUsedConfigurationComponent(ConfigurationComponent actor) {
            this.usedHardwareBean.usedConfigurationComponents.add(actor);
            return this;
        }

        public NewUsedHardwareBean build() {
            return this.usedHardwareBean;
        }
    }
}
