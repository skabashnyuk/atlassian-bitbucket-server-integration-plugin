package com.atlassian.bitbucket.jenkins.internal.applink.oauth.fullapplink;

import hudson.Extension;
import hudson.util.PluginServletFilter;
import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;
import org.tuckey.web.filters.urlrewrite.UrlRewriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

@Extension
public class ApplinksServletFilter extends PluginServletFilter {

    /*
    This class is currently not loaded, this is where we should setup our url re-writing filter, as opposed to the OAuthRequestFilter.
    Just laziness meant that we didn't fix this class up and added the filter in properly.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
    }
}
