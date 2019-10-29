package de.fhg.iais.roberta.bean;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Container for used methods.
 */
public class UsedMethodBean {
    private final Set<Enum<?>> usedMethods = new HashSet<>(); //All needed helper methods as a Set

    public Set<Enum<?>> getUsedMethods() {
        return Collections.unmodifiableSet(this.usedMethods);
    }

    public static class Builder {
        private final UsedMethodBean usedMethodBean = new UsedMethodBean();

        public Builder addUsedMethod(Enum<?> usedMethod) {
            this.usedMethodBean.usedMethods.add(usedMethod);
            return this;
        }

        public UsedMethodBean build() {
            return this.usedMethodBean;
        }
    }
}
