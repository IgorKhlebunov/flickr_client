package com.practical.experience.recyclerviewchallenge.common;

import java.util.List;

public interface IListenerCallback<T> {
    void onComplete(List<T> responseContent);
    void onError(String errorData);
}
