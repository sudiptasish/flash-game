package com.ws.jg.common.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.RequestBuilder;
import com.sun.jersey.api.client.UniformInterface;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class RESTClient {
    
    // Name of the remote service end point
    private final String name;
    
    // Version of the remote servic end point
    private final String version;
    
    // Remote service end point
    private final String basePath;

    private static Client client = null;

    private RESTClient() {
        this.name = null;
        this.version = null;
        this.basePath = null;
    }
    
    /**
     * Create a new instance of a web service client.
     * 
     * It also sets the base path (essentially the end point of a remote service).
     * One has to be cautious while using this constructor and reusing the object
     * created by invoking 'new RESTClient(<base_uri>)'. Because it is possible that
     * during subsequent (GET/PUT/POST/DELETE) call, it may fail, possibly due to
     * the fact that the end point (identified by basePath) is no longer available,
     * and that registry service is already updated with a new end point. Therefore one should
     * destory the instance after its very first usage.
     * 
     * @param name      Name of the remote service
     * @param version   The version
     * @param basePath  Base URI
     */
    public RESTClient(String name, String version, String basePath) {
        this.name = name;
        this.version = version;
        this.basePath = basePath;
    }

    /**
     * Return a new instance of web service client.
     * It works as an Factory API for client-side representation of a resource.
     * An instance created by this factory method doesn't have the base URI set
     * that represent the remote resource. Caller has to ensure to pass the
     * complete (canonical) URI at the time of making a remote (GET/PUT/POST/DELETE) call.
     * 
     * @return RESTClient
     */
    public static RESTClient newClient() {
        return new RESTClient();
    }

    /**
     * Return the remote service name
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Return the remote end point version
     * @return String
     */
    public String getVersion() {
        return version;
    }

    /**
     * Return the canonical path that represents the remote service.
     * @return String
     */
    public String getBasePath() {
        return basePath;
    }
    
    /**
     * Create and return the web resource.
     * @param serviceUrl
     * @return UniformInterface
     */
    private WebResource createWebResource(String serviceUrl) {
        if (client == null) {
            ClientConfig config = new DefaultClientConfig();
            
            config.getFeatures().put(com.sun.jersey.api.json.JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
            config.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);
            config.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, (int)TimeUnit.SECONDS.toMillis(20));
            config.getProperties().put(ClientConfig.PROPERTY_READ_TIMEOUT, (int)TimeUnit.MINUTES.toMillis(2));

            client = Client.create(config);
        }
        return client.resource(serviceUrl);
    }
    
    public ClientResponse method(RESTClient.RESTClientParam wsParam, String method, Object obj) {
        return getWebResource(wsParam).method(method, ClientResponse.class, obj);
    }
    
    /**
     * Make a remote GET call.
     * Use this API when the Proxy web service client is created by invoking
     * the parameterized constructor to set its basePath.
     * 
     * @param wsParam
     * @return ClientResponse
     */
    public ClientResponse get(RESTClient.RESTClientParam wsParam) {
        return getWebResource(wsParam).get(ClientResponse.class);
    }
    
    /**
     * Make a remote POST call.
     * Use this API when the Proxy web service client is created by invoking
     * the parameterized constructor to set its basePath.
     * 
     * @param wsParam
     * @param obj
     * @return ClientResponse
     */
    public ClientResponse post(RESTClient.RESTClientParam wsParam, Object obj) {
        return getWebResource(wsParam).post(ClientResponse.class, obj);
    }
    
    /**
     * Make a remote PUT call.
     * Use this API when the Proxy web service client is created by invoking
     * the parameterized constructor to set its basePath.
     * 
     * @param wsParam
     * @param obj
     * @return ClientResponse
     */
    public ClientResponse put(RESTClient.RESTClientParam wsParam, Object obj) {
        return getWebResource(wsParam).put(ClientResponse.class, obj);
    }
    
    /**
     * Make a remote DELETE call.
     * Use this API when the Proxy web service client is created by invoking
     * the parameterized constructor to set its basePath.
     * 
     * @param wsParam
     * @return ClientResponse
     */
    public ClientResponse delete(RESTClient.RESTClientParam wsParam) {
        return getWebResource(wsParam).delete(ClientResponse.class);
    }
    
    /**
     * Make a remote GET call.
     * 
     * @param uri
     * @param wsParam
     * @return ClientResponse
     */
    public ClientResponse get(String uri, RESTClient.RESTClientParam wsParam) {
        return getWebResource(uri, wsParam).get(ClientResponse.class);
    }
    
    /**
     * Make a remote PUT call.
     * 
     * @param uri
     * @param wsParam
     * @param obj
     * @return ClientResponse
     */
    public ClientResponse put(String uri, RESTClient.RESTClientParam wsParam, Object obj) {
        return getWebResource(uri, wsParam).put(ClientResponse.class, obj);
    }
    
    /**
     * Make a remote POST call.
     * 
     * @param uri
     * @param wsParam
     * @param obj
     * @return ClientResponse
     */
    public ClientResponse post(String uri, RESTClient.RESTClientParam wsParam, Object obj) {
        return getWebResource(uri, wsParam).post(ClientResponse.class, obj);
    }
    
    /**
     * Make a remote DELETE call.
     * 
     * @param uri
     * @param wsParam
     * @return ClientResponse
     */
    public ClientResponse delete(String uri, RESTClient.RESTClientParam wsParam) {
        return getWebResource(uri, wsParam).delete(ClientResponse.class);
    }
    
    private UniformInterface getWebResource(RESTClient.RESTClientParam wsParam) {
        if (basePath == null || basePath.trim().length() == 0) {
            throw new IllegalStateException("No base path/service end point uri specified");
        }
        return getWebResource(basePath, wsParam);
    }
    
    private UniformInterface getWebResource(String uri, RESTClient.RESTClientParam wsParam) {
        WebResource webResource = createWebResource(uri);
        UniformInterface uniformIntf = addParameter(webResource, wsParam);
        uniformIntf = addHeader(uniformIntf, wsParam);
        
        return uniformIntf;
    }
    
    /**
     * Add path and query parameters.
     * 
     * @param uniformIntf
     * @param wsParam
     * @return 
     */
    private UniformInterface addParameter(UniformInterface uniformIntf, RESTClient.RESTClientParam wsParam) {
        WebResource webResource = (WebResource)uniformIntf;
        
        // Add the path parameter(s) (if any)
        for (String pathParm : wsParam.pathParams) {
            webResource = webResource.path(pathParm);
        }
        // Add the query parameter(s)
        for (Map.Entry<String, String> me : wsParam.queryParams.entrySet()) {
            webResource = webResource.queryParam(me.getKey(), me.getValue());
        }
        return webResource;
    }

    /**
     * Add the header informations.
     * 
     * @param uniformIntf
     * @param wsParam
     */
    private UniformInterface addHeader(UniformInterface uniformIntf, RESTClient.RESTClientParam wsParam) {
        RequestBuilder builder = (RequestBuilder)uniformIntf;
        
        // Add available media types.
        if (wsParam.mediaTypes.size() > 0) {
            builder = builder.type(wsParam.mediaTypes.get(0))
                                 .accept(wsParam.mediaTypes.toArray(new MediaType[wsParam.mediaTypes.size()]));
        }     
        // Add the supported languages
        if (wsParam.languages.size() > 0) {
            builder = builder.acceptLanguage(wsParam.languages.toArray(new Locale[wsParam.languages.size()]));
        }
        // Add the appropriate request headers
        for (Map.Entry<String, Object> me : wsParam.headers.entrySet()) {
            builder = builder.header(me.getKey(), me.getValue());
        }
        return (UniformInterface)builder;
    }
    
    // Class to hold the REST client parameters.
    // Client has to create an instance of this class with appropriate parameters which
    // they want to be a part of REST call.
    public static class RESTClientParam {
        
        private final Map<String, Object> headers = new HashMap<>();
        private final Map<String, String> queryParams = new HashMap<>();
        private final List<MediaType> mediaTypes = new ArrayList<>();
        private final List<Locale> languages = new ArrayList<>();        
        private final List<String> pathParams = new ArrayList<>();
        
        /**
         * Add the request header key and value.
         * @param key
         * @param value 
         */
        public void addRequestHeader(String key, Object value) {
            headers.put(key, value);
        }
        
        /**
         * Add the specified query parameters.
         * @param paramKey
         * @param paramValue 
         */
        public void addQueryParam(String paramKey, String paramValue) {
            queryParams.put(paramKey, paramValue);
        }
        
        /**
         * Add the supported media type
         * @param mediaType 
         */
        public void addSupportedMediaType(MediaType mediaType) {
            mediaTypes.add(mediaType);
        }
        
        /**
         * Add the language.
         * @param locale 
         */
        public void addsupportedLanguages(Locale locale) {
            languages.add(locale);
        }
        
        /**
         * Add the path parameters.
         * @param param 
         */
        public void addPathParam(String param) {
            pathParams.add(param);
        }        
    } // RESTClientParam class
}
