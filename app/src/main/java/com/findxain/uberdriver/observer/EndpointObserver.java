package com.findxain.uberdriver.observer;

import io.reactivex.observers.DisposableObserver;

public abstract class EndpointObserver<T> extends DisposableObserver<T> {

    @Override
    public void onNext(T o) {
        try {
            onData(o);
        } catch (Exception e) {
            onError(e);
        }
        onComplete();


    }

    @Override
    public void onError(Throwable e) {
        onComplete();
        onHandledError(e);

    }

    @Override
    public abstract void onComplete();

    public abstract void onData(T o) throws Exception;

    public abstract void onHandledError(Throwable e);

}
