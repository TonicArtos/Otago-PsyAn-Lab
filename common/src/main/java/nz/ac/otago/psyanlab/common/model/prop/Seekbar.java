
package nz.ac.otago.psyanlab.common.model.prop;

import nz.ac.otago.psyanlab.common.model.Prop;
import nz.ac.otago.psyanlab.common.model.util.NameResolverFactory;

public class Seekbar extends Prop {
    public static NameResolverFactory getEventNameFactory() {
        return new EventNameFactory();
    }

    @Override
    public NameResolverFactory getMethodNameFactory() {
        return new MethodNameFactory();
    }

    @Override
    public NameResolverFactory getParameterNameFactory() {
        return new ParameterNameFactory();
    }

    protected static class EventNameFactory extends Prop.EventNameFactory {
        @Override
        public int getResId(int lookup) {
            switch (lookup) {
                default:
                    return super.getResId(lookup);
            }
        }
    }

    protected static class MethodNameFactory extends Prop.MethodNameFactory {
        @Override
        public int getResId(int lookup) {
            switch (lookup) {
                default:
                    return super.getResId(lookup);
            }
        }
    }

    protected static class ParameterNameFactory extends Prop.ParameterNameFactory {
        @Override
        public int getResId(int lookup) {
            switch (lookup) {
                default:
                    return super.getResId(lookup);
            }
        }
    }
}