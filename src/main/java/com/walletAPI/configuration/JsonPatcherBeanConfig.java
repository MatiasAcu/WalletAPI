package com.walletAPI.configuration;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.walletAPI.json.JsonPatcher;
import com.walletAPI.json.JsonViews;
import com.walletAPI.model.entities.Account;
import com.walletAPI.model.entities.Transaction;
import com.walletAPI.model.entities.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
    Exposes the JsonPatcher beans for each class that uses it and configures it according the need

 */

@Configuration
public class JsonPatcherBeanConfig {

    @Bean
    public JsonPatcher<User> userJsonPatcher() {
        JsonMapper mapper = JsonMapper.builder()
                .disable(MapperFeature.DEFAULT_VIEW_INCLUSION)
                .build();

        mapper.setConfig(mapper.getDeserializationConfig()
                .withView(JsonViews.User.Patch.class));


        return new JsonPatcher<User>(User.class, mapper);
    }

    @Bean
    public JsonPatcher<Account> accountJsonPatcher() {
        JsonMapper mapper = JsonMapper.builder()
                .disable(MapperFeature.DEFAULT_VIEW_INCLUSION)
                .build();

        mapper.setConfig(mapper.getDeserializationConfig()
                .withView(JsonViews.Account.Patch.class));

        return new JsonPatcher<Account>(Account.class, mapper);
    }

    @Bean
    public JsonPatcher<Transaction> transactionJsonPatcher() {

        JsonMapper mapper = JsonMapper.builder()
                .disable(MapperFeature.DEFAULT_VIEW_INCLUSION)
                .build();

        mapper.registerModule(new JavaTimeModule());

        mapper.setConfig(mapper.getDeserializationConfig()
                .withView(JsonViews.Transaction.Patch.class));

        return new JsonPatcher<Transaction>(Transaction.class, mapper);
    }

}
