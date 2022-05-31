package com.walletAPI.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;


public class JsonPatcher<T> {

    private final JsonMapper objectMapper;

    private final Class<T> tClass;


    public JsonPatcher(Class<T> tClass, JsonMapper objectMapper) {
        this.objectMapper = configureMapper(objectMapper);
        this.tClass = tClass;
    }


    protected JsonMapper configureMapper(JsonMapper objectMapper) {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;

    }

    public T applyPatchToObject(JsonPatch patch, T object) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(object, JsonNode.class));
        return objectMapper.treeToValue(patched, tClass);


    }


}
