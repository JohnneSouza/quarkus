package io.quarkus.qrs.runtime.jaxrs;

import java.io.InputStream;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import io.quarkus.qrs.runtime.core.QrsRequestContext;

public class QrsContainerRequestContext implements ContainerRequestContext {

    private QrsRequestContext context;
    private boolean aborted;
    private boolean preMatch;
    private boolean response;

    public QrsContainerRequestContext(QrsRequestContext requestContext) {
        this.context = requestContext;
    }

    @Override
    public Object getProperty(String name) {
        return context.getProperty(name);
    }

    @Override
    public Collection<String> getPropertyNames() {
        return context.getPropertyNames();
    }

    @Override
    public void setProperty(String name, Object object) {
        context.setProperty(name, object);
    }

    @Override
    public void removeProperty(String name) {
        context.removeProperty(name);
    }

    @Override
    public UriInfo getUriInfo() {
        return context.getUriInfo();
    }

    @Override
    public void setRequestUri(URI requestUri) {
        assertPreMatch();
        throw new RuntimeException("NYI");
    }

    @Override
    public void setRequestUri(URI baseUri, URI requestUri) {
        assertPreMatch();
        throw new RuntimeException("NYI");
    }

    @Override
    public Request getRequest() {
        return context.getRequest();
    }

    @Override
    public String getMethod() {
        return context.getMethod();
    }

    @Override
    public void setMethod(String method) {
        assertPreMatch();
        context.setMethod(method);
    }

    public void assertPreMatch() {
        if (!isPreMatch()) {
            throw new IllegalStateException("Can only be called from a @PreMatch filter");
        }
    }

    public void assertNotResponse() {
        if (!isPreMatch()) {
            throw new IllegalStateException("Cannot be called from response filter");
        }
    }

    @Override
    public MultivaluedMap<String, String> getHeaders() {
        return context.getHttpHeaders().getMutableHeaders();
    }

    @Override
    public String getHeaderString(String name) {
        return context.getHttpHeaders().getHeaderString(name);
    }

    @Override
    public Date getDate() {
        return context.getHttpHeaders().getDate();
    }

    @Override
    public Locale getLanguage() {
        return context.getHttpHeaders().getLanguage();
    }

    @Override
    public int getLength() {
        return context.getHttpHeaders().getLength();
    }

    @Override
    public MediaType getMediaType() {
        return context.getHttpHeaders().getMediaType();
    }

    @Override
    public List<MediaType> getAcceptableMediaTypes() {
        return context.getHttpHeaders().getAcceptableMediaTypes();
    }

    @Override
    public List<Locale> getAcceptableLanguages() {
        return context.getHttpHeaders().getAcceptableLanguages();
    }

    @Override
    public Map<String, Cookie> getCookies() {
        return context.getHttpHeaders().getCookies();
    }

    @Override
    public boolean hasEntity() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public InputStream getEntityStream() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setEntityStream(InputStream input) {
        assertNotResponse();
        // TODO Auto-generated method stub

    }

    @Override
    public SecurityContext getSecurityContext() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setSecurityContext(SecurityContext context) {
        assertNotResponse();
        // TODO Auto-generated method stub

    }

    public boolean isPreMatch() {
        return preMatch;
    }

    public QrsContainerRequestContext setPreMatch(boolean preMatch) {
        this.preMatch = preMatch;
        return this;
    }

    @Override
    public void abortWith(Response response) {
        assertNotResponse();
        context.setResult(response);
        context.restart(context.getDeployment().getAbortHandlerChain());
        aborted = true;
    }

    public boolean isResponse() {
        return response;
    }

    public QrsContainerRequestContext setResponse(boolean response) {
        this.response = response;
        return this;
    }

    public boolean isAborted() {
        return aborted;
    }
}