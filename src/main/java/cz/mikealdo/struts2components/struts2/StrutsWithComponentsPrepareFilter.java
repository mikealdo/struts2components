package cz.mikealdo.struts2components.struts2;

import ognl.IteratorEnumeration;
import org.apache.struts2.StrutsStatics;
import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareFilter;
import org.apache.struts2.dispatcher.ng.listener.StrutsListener;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This prepare filter for Struts2 should be used instead of standard {@link StrutsPrepareFilter} and
 * {@link StrutsListener} together. It initializes a dispatcher only once and also changes default
 * configuration files to load struts-overridden-default.xml instead of struts-overridden-default.xml, which will
 * be totally ignored. Obviously there is still option to configure configuration files by filter
 * init parameters.
 */

public class StrutsWithComponentsPrepareFilter extends StrutsPrepareFilter {

    public static final String CONFIG_PARAM_NAME = "config";
    public static final String DEFAULT_CONFIG_FILES = "struts-overridden-default.xml,struts-plugin.xml,struts.xml";

    /***
     * copy actual filter configuration and put default configuration files parameter if it's not already placed here
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //
        ConfigurableFilterConfig newerFilterConfig = new ConfigurableFilterConfig(filterConfig);
        if (!newerFilterConfig.containsInitParameter(CONFIG_PARAM_NAME)) {
            newerFilterConfig.addInitParameter(CONFIG_PARAM_NAME, DEFAULT_CONFIG_FILES);
        }

        super.init(newerFilterConfig);
    }

    @Override
    protected void postInit(Dispatcher dispatcher, FilterConfig filterConfig) {
        filterConfig.getServletContext().setAttribute(StrutsStatics.SERVLET_DISPATCHER, dispatcher);
    }

    /**
     * Object which has customizable init parameters. Everything else delegates to real {@link FilterConfig} instance.
     */
    private static class ConfigurableFilterConfig implements FilterConfig {

        private final FilterConfig filterConfig;
        private final Map<String, String> initParameters = new LinkedHashMap<String, String>();

        public ConfigurableFilterConfig(FilterConfig filterConfig) {
            super();
            this.filterConfig = filterConfig;
            for (Enumeration<?> iterator = filterConfig.getInitParameterNames(); iterator.hasMoreElements(); ) {
                String paramName = (String) iterator.nextElement();
                String paramValue = filterConfig.getInitParameter(paramName);
                initParameters.put(paramName, paramValue);
            }
        }

        @Override
        public String getFilterName() {
            return filterConfig.getFilterName();
        }

        @Override
        public ServletContext getServletContext() {
            return filterConfig.getServletContext();
        }

        @Override
        public String getInitParameter(String name) {
            return initParameters.get(name);
        }

        @Override
        public Enumeration<String> getInitParameterNames() {
            return new IteratorEnumeration(initParameters.keySet().iterator());
        }

        public void addInitParameter(String name, String value) {
            initParameters.put(name, value);
        }

        public boolean containsInitParameter(String name) {
            return initParameters.containsKey(name);
        }
    }

}
