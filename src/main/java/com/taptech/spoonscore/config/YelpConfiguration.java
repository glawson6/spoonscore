package com.taptech.spoonscore.config;

import com.taptech.spoonscore.config.locale.AngularCookieLocaleResolver;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.inject.Inject;

@Configuration
public class YelpConfiguration {

    @Inject
    Environment env;

    @Bean
    public OAuthService getOAuthService(){
        String CONSUMER_KEY = env.getProperty("YELP_CONSUMER_KEY");
        String CONSUMER_SECRET = env.getProperty("YELP_CONSUMER_SECRET");
        OAuthService service =
                new ServiceBuilder().provider(TwoStepOAuth.class).apiKey(CONSUMER_KEY)
                        .apiSecret(CONSUMER_SECRET).build();
        return service;
    }

    @Bean
    public Token getToken(){
        String TOKEN  = env.getProperty("YELP_TOKEN");
        String TOKEN_SECRET = env.getProperty("YELP_TOKEN_SECRET");
        Token accessToken = new Token(TOKEN, TOKEN_SECRET);
        return accessToken;
    }

    public static class TwoStepOAuth  extends DefaultApi10a {

        @Override
        public String getAccessTokenEndpoint() {
            return null;
        }

        @Override
        public String getAuthorizationUrl(Token arg0) {
            return null;
        }

        @Override
        public String getRequestTokenEndpoint() {
            return null;
        }
    }
}
