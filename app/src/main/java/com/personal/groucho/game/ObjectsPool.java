package com.personal.groucho.game;

import androidx.annotation.NonNull;

public class ObjectsPool<T> {
    private final T[] mPool;
    private int mPoolSize;

    @SuppressWarnings("unchecked")
    public ObjectsPool(int maxPoolSize, Class<T> tClass) {
        if (maxPoolSize <= 0) {
            throw new IllegalArgumentException("The max pool size must be > 0");
        }
        mPoolSize = maxPoolSize;
        mPool = (T[]) new Object[mPoolSize];

        for (int i = 0; i < mPoolSize; i++) {
            mPool[i] = createObject(tClass);
        }
    }

    private T createObject(Class<T> tClass) {
        try {
            return tClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Unable to create object instance", e);
        }
    }

    public T acquire() {
        if (mPoolSize == 0) {
            throw new IllegalStateException("Empty pool");
        }
        mPoolSize--;
        return mPool[mPoolSize];
    }

    public void release(@NonNull T instance) {
        if (mPoolSize < mPool.length) {
            mPool[mPoolSize] = instance;
            mPoolSize++;
        }
    }
}
