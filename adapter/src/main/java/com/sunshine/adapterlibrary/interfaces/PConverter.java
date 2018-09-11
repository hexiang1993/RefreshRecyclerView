package com.sunshine.adapterlibrary.interfaces;


/**
 * Created by xhe
 */
@FunctionalInterface
public interface PConverter<T> {
    void convert(Holder holder, T item, int p);

}