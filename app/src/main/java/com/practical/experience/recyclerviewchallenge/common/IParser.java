package com.practical.experience.recyclerviewchallenge.common;

import java.util.List;

public interface IParser<T>  {
    List<T> parse(String text);
}
