package com.timezonescheduler.chronos.application.security.oauth.user;


import com.timezonescheduler.chronos.application.model.AuthProvider;
import com.timezonescheduler.chronos.application.security.OAuth2UserInfo;
import com.timezonescheduler.chronos.application.security.oauth.exception.OAuth2AuthenticationProcessingException;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(AuthProvider.google.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}