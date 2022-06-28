package com.godfunc.plugin;

import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

public class BasePlugin extends Plugin {
    /**
     * Constructor to be used by plugin manager for plugin instantiation.
     * Your plugins have to provide constructor with this exact signature to
     * be successfully loaded by manager.
     *
     * @param wrapper
     */
    public BasePlugin(PluginWrapper wrapper) {
        super(wrapper);
    }
}
