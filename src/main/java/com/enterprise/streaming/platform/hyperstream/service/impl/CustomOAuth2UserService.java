package com.enterprise.streaming.platform.hyperstream.service.impl;

import com.enterprise.streaming.platform.hyperstream.model.User;
import com.enterprise.streaming.platform.hyperstream.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.*;
import org.springframework.security.oauth2.core.user.*;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {

        OAuth2User oAuth2User = super.loadUser(request);

        Map<String,Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        userRepository.findByEmail(email).orElseGet(() -> {

            User user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setSubscriptionType("BASIC");

            return userRepository.save(user);
        });

        return oAuth2User;
    }
}