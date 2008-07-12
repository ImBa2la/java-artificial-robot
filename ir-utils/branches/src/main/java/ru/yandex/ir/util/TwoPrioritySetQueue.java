package ru.yandex.ir.util;

import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yozh
 */
public class TwoPrioritySetQueue<E> {
    private final Lock lock;
    private final Condition condition;

    public TwoPrioritySetQueue(Lock lock, Condition condition) {
        this.lock = lock;
        this.condition = condition;
    }

    public TwoPrioritySetQueue(Lock lock) {
        this.lock = lock;
        condition = lock.newCondition();
    }

    public TwoPrioritySetQueue() {
        lock = new ReentrantLock();
        condition = lock.newCondition();
    }

    private Queue<E> lowPriorityQueue = new LinkedList<E>();
    private Queue<E> highPriorityQueue = new LinkedList<E>();

    public E poll() {
        lock.lock();
        try {
            E e = highPriorityQueue.poll();
            if (e != null) {
                return e;
            }

            e = lowPriorityQueue.poll();
            if (e != null) {
                return e;
            }

            return null;
        } finally {
            lock.unlock();
        }
    }

    public E take() throws InterruptedException {
        lock.lock();
        try {
            for (;;) {
                E e = poll();
                if (e != null) {
                    return e;
                }
                condition.await();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * @return true
     */
    public boolean add(E e) {
        lock.lock();
        try {
            if (lowPriorityQueue.contains(e)) {
                return false;
            }
            lowPriorityQueue.add(e);
            condition.signal();
            return true;
        } finally{
            lock.unlock();
        }
    }

    /**
     * @return false if category id is not in this queue
     */
    public boolean addToFaster(E e) {
        lock.lock();
        try {
            if (highPriorityQueue.contains(e)) {
                return false;
            }
            boolean removed = lowPriorityQueue.remove(e);
            highPriorityQueue.add(e);
            if (!removed) {
                condition.signal();
            }
            return true;
        } finally {
            lock.unlock();
        }
    }
} //~
