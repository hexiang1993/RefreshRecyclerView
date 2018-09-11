package com.sunshine.adapterlibrary.interfaces;


/**
 * Created by xhe
 */
@FunctionalInterface
public interface Converter<T> {
    void convert(Holder holder, T item);

}