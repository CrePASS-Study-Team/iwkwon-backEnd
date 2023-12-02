//package com.study.mbti.config;
//
//import javax.persistence.AttributeConverter;
//import javax.persistence.Converter;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//@Converter(autoApply = true)
//public class JsonObjectConverter implements AttributeConverter<Object, String> {
//
//    @Override
//    public String convertToDatabaseColumn(Object attribute) {
//        if (attribute == null) {
//            return null;
//        }
//        if (attribute instanceof JSONObject) {
//            return ((JSONObject) attribute).toString();
//        } else if (attribute instanceof JSONArray) {
//            return ((JSONArray) attribute).toString();
//        } else {
//            throw new IllegalArgumentException("Invalid type for JSON conversion");
//        }
//    }
//
//    @Override
//    public Object convertToEntityAttribute(String dbData) {
//        if (dbData == null) {
//            return null;
//        }
//        try {
//            return new JSONObject(dbData);
//        } catch (Exception e) {
//            try {
//                return new JSONArray(dbData);
//            } catch (Exception ex) {
//                throw new IllegalArgumentException("Failed to convert string to JSON");
//            }
//        }
//    }
//}