package com.crowdin.common;

import com.crowdin.exception.CrowdinException;

public class Settings {

    private String protocol = "https";

    private String token;
    private String userId;
    private String organisationName;
    private ApiType apiType;
    private Environment environment;
    private String baseUrl;

    private Settings(String token, String baseUrl) {
        this.token = token;
        this.baseUrl = baseUrl;
    }

    private Settings(String token, String userId, String organisationName, ApiType apiType, Environment environment) {
        this.token = token;
        this.userId = userId;
        this.organisationName = organisationName;
        this.apiType = apiType;
        this.environment = environment;
    }

    public static Settings enterprise(String token, String organisationName) {
        return new Settings(token, null, organisationName, ApiType.ENTERPRISE, Environment.ENTERPRISE_PRODUCTION);
    }


    public static Settings withBaseUrl(String token, String baseUrl) {
        if (baseUrl == null || baseUrl.isEmpty()) {
            throw new CrowdinException("Base url can`t be null or empty");
        }

        return new Settings(token, baseUrl);
    }

    public String getBaseUrl() {
        if (baseUrl == null) {
            baseUrl = compileBaseUrl();
        }
        return baseUrl;
    }

    private String compileBaseUrl() {
        return protocol
                .concat("://")
                .concat(
                        apiType == ApiType.ENTERPRISE
                                ? organisationName + "." + environment.getUrl()
                                : environment.getUrl() + "/users/" + userId
                );
    }

    public String getToken() {
        return token;
    }

    public String getUserId() {
        return userId;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public ApiType getApiType() {
        return apiType;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public boolean isEnterprise() {
        return apiType == ApiType.ENTERPRISE;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public enum ApiType {
        ENTERPRISE,
        SIMPLE
    }

    public enum Environment {
        ENTERPRISE_PRODUCTION("enterprise.crowdin.net/api/v2");

        private String url;

        Environment(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }
}