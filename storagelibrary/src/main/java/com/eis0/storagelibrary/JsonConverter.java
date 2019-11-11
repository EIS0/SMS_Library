package com.eis0.storagelibrary;

public interface JsonConverter <T> {
    String convertToJson(T objectToConvert);
    T convertFromJson(String json);
}
