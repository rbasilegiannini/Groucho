package com.personal.groucho.game;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ObjectsPool<T> {
    private final ArrayList<T> mPool;
    private int mPoolSize;

    public ObjectsPool(int maxPoolSize, Class<T> tClass) {
        if (maxPoolSize <= 0) {
            throw new IllegalArgumentException("The max pool size must be > 0");
        }
        mPool = new ArrayList<>();
        mPoolSize = maxPoolSize;

        for (int i = 0; i < mPoolSize; i++) {
            mPool.add(createObject(tClass));
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
        return mPool.get(mPoolSize);
    }

    public boolean release(@NonNull T instance) {
        if (isInPool(instance)) {
            throw new IllegalStateException("Already in the pool!");
        }
        if (mPoolSize < mPool.size()) {
            mPool.add(mPoolSize, instance);
            mPoolSize++;
            return true;
        }
        return false;
    }

    private boolean isInPool(@NonNull T instance) {
        for (int i = 0; i < mPoolSize; i++) {
            if (mPool.get(i) == instance) {
                return true;
            }
        }
        return false;
    }
}
