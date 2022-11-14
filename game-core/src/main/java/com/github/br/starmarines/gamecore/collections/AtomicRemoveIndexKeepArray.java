package com.github.br.starmarines.gamecore.collections;


import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Коллекия сделана, как замена хэшмапе для ускорения. в качестве ключа - int, который равен позиции в массиве. значение - значениев массиве
 *
 * коллекция, которая вместо удаления запоминает удаленные элементы для последущей вставки в эти участки
 * новых элементов. Сами ячейки помечаются как null
 * Таким образом, имеем следующие особенности:
 *  1) индексы элементов остаются постоянными и не сдвигаются при удалении элементов
 *  2) Коллекция может содержать null. помеченный удаленным элемент меняется при вставке, а при удалении меняется на null.
 *  3) порядок вставки элементов не определен. после ячейки 5 может быть вставка в ячейку 2, если элемент в ячейке 2 был удален.
 * @param <T>
 */
public class AtomicRemoveIndexKeepArray<T> {

    private final Lock lock = new ReentrantLock();

    private final int capacity;
    private AtomicReferenceArray<T> array;
    private final IntArray removedQueue;

    private volatile int counter = 0;

    public AtomicRemoveIndexKeepArray(int capacity) {
        this.capacity = capacity;
        array = new AtomicReferenceArray<>(capacity);
        removedQueue = new IntArray(capacity);
        removedQueue.add(Integer.MIN_VALUE);
    }
    
    public int getLength() {
        return capacity;
    }

    public int getActiveSize() {
        lock.lock();
        try {
            return capacity - removedQueue.size;
        } finally {
            lock.unlock();
        }
    }

    public T get(int index) {
        return array.get(index);
    }

    /**
     * возвращает индекс элемента
     * @param t элемент
     * @return индекс этого элемента в массиве
     */
    public int add(T t) {
        lock.lock();
        try {
            int number = removedQueue.pop();
            int result;
            if(number == Integer.MIN_VALUE) {
                removedQueue.add(Integer.MIN_VALUE);
                array.set(counter, t);
                counter++;
                result = counter;
            } else {
                array.set(number, t);
                result = number;
            }
            return result;
        } finally {
            lock.unlock();
        }
    }

    public T remove(int index) {
        lock.lock();
        try {
            removedQueue.add(index);
            return array.getAndSet(index, null);
        } finally {
            lock.unlock();
        }
    }

    public void clear() {
        lock.lock();
        try {
            array = new AtomicReferenceArray<>(capacity);
            removedQueue.clear();
        } finally {
            lock.unlock();
        }
    }

    public Collection<Wrapper<T>> asCollection(int from, int to) {
        if(from < 0) {
            throw new IllegalArgumentException("the parameter 'from' is lower than 1. from=[" + from + "]");
        }
        int max = Math.max(to, capacity);

        ArrayList<Wrapper<T>> result = new ArrayList<>(capacity);
        for(int i = from; i < max; i++) {
            T t = array.get(i);
            if(t != null) {
                result.add(new Wrapper<T>(i, t));
            }
        }

        return result;
    }

    public static class Wrapper<T> {
        public int id;
        public T value;

        public Wrapper(int id, T value) {
            this.id = id;
            this.value = value;
        }
    }

}
