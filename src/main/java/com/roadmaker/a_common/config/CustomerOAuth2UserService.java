package com.roadmaker.a_common.config;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
// SNS에게 사용자 정보 요청을 처리하고, 사용자 정보를 수신한다. OAuth2User 생성하고 리턴한다.
public class CustomerOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 부모 클래스의 loadUser 메서드를 호출하여 기본 사용자 정보를 가져온다.
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 사용자 속성 가져오기
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 어떤 제공자 인지 구분하자
        String provider = userRequest.getClientRegistration().getRegistrationId();

        if (provider.equals("kakao")) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            if (kakaoAccount == null) {
                throw new OAuth2AuthenticationException("kakao error");
            }
            String email = (String) kakaoAccount.get("email");
            Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
            if (properties == null) {
                throw new OAuth2AuthenticationException("kakao error");
            }
            String name = (String) properties.get("nickname");
            // 카카오 아이디가 아니다.
            String id = String.valueOf(attributes.get("id"));

            log.info("카카오 id {}", id);
            log.info("카카오 email {}", email);
            log.info("카카오 name {}", name);

            return new DefaultOAuth2User(oAuth2User.getAuthorities(), Map.of(
                    "email", email,
                    "name", name,
                    "id", id), "email");
        } else if (provider.equals("naver")) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            if (response == null) {
                throw new OAuth2AuthenticationException("naver error");
            }

            String name = (String) response.get("name");
            String email = (String) response.get("email");
            String id = (String) response.get("id");

            log.info("네이버 id {}", id);
            log.info("네이버 email {}", email);
            log.info("네이버 name {}", name);

            return new DefaultOAuth2User(oAuth2User.getAuthorities(), Map.of(
                    "email", email,
                    "name", name,
                    "id", id), "email");
        } else if (provider.equals("google")) {
            // String email = (String) attributes.get("email");
            // String name = (String) attributes.get("name");
            // String sub = (String) attributes.get("sub");
        }

        return oAuth2User;
    }
}
