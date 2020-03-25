package edu.coursera.concurrent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Wrapper class for two lock-based concurrent list implementations.
 */
public final class CoarseLists {
    /**
     * An implementation of the ListSet interface that uses Java locks to
     * protect against concurrent accesses.
     * <p>
     * correct, concurrent access to this list. Use a Java ReentrantLock object
     * to protect against those concurrent accesses. You may refer to
     * SyncList.java for help understanding the list management logic, and for
     * guidance in understanding where to place lock-based synchronization.
     */
    public static final class CoarseList extends ListSet {

        private final ReentrantLock lock;

        /**
         * Default constructor.
         */
        public CoarseList() {
            super();
            this.lock = new ReentrantLock();
        }

        @Override
        boolean add(final Integer object) {
            lock.lock();

            try {
                Entry pred = this.head;
                Entry curr = pred.next;

                while (curr.object.compareTo(object) < 0) {
                    pred = curr;
                    curr = curr.next;
                }

                if (object.equals(curr.object)) {
                    return false;
                } else {
                    final Entry entry = new Entry(object);
                    entry.next = curr;
                    pred.next = entry;
                    return true;
                }
            } finally {
                lock.unlock();
            }

        }

        @Override
        boolean remove(final Integer object) {
            lock.lock();

            try {
                Entry pred = this.head;
                Entry curr = pred.next;

                while (curr.object.compareTo(object) < 0) {
                    pred = curr;
                    curr = curr.next;
                }

                if (object.equals(curr.object)) {
                    pred.next = curr.next;
                    return true;
                } else {
                    return false;
                }
            } finally {
                lock.unlock();
            }
        }

        @Override
        boolean contains(final Integer object) {
            lock.lock();

            try {
                Entry pred = this.head;
                Entry curr = pred.next;

                while (curr.object.compareTo(object) < 0) {
                    pred = curr;
                    curr = curr.next;
                }
                return object.equals(curr.object);
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * An implementation of the ListSet interface that uses Java read-write
     * locks to protect against concurrent accesses.
     * <p>
     * TODO Implement the add, remove, and contains methods below to support
     * correct, concurrent access to this list. Use a Java
     * ReentrantReadWriteLock object to protect against those concurrent
     * accesses. You may refer to SyncList.java for help understanding the list
     * management logic, and for guidance in understanding where to place
     * lock-based synchronization.
     */
    public static final class RWCoarseList extends ListSet {

        private final ReadWriteLock lock;

        private final Lock readLock;
        private final Lock writeLock;

        /**
         * Default constructor.
         */
        public RWCoarseList() {
            super();
            this.lock = new ReentrantReadWriteLock();
            this.readLock = lock.readLock();
            this.writeLock = lock.writeLock();
        }

        @Override
        boolean add(final Integer object) {
            writeLock.lock();

            try {
                Entry pred = this.head;
                Entry curr = pred.next;

                while (curr.object.compareTo(object) < 0) {
                    pred = curr;
                    curr = curr.next;
                }

                if (object.equals(curr.object)) {
                    return false;
                } else {
                    final Entry entry = new Entry(object);
                    entry.next = curr;
                    pred.next = entry;
                    return true;
                }
            } finally {
                writeLock.unlock();
            }

        }

        @Override
        boolean remove(final Integer object) {
            writeLock.lock();

            try {
                Entry pred = this.head;
                Entry curr = pred.next;

                while (curr.object.compareTo(object) < 0) {
                    pred = curr;
                    curr = curr.next;
                }

                if (object.equals(curr.object)) {
                    pred.next = curr.next;
                    return true;
                } else {
                    return false;
                }
            } finally {
                writeLock.unlock();
            }
        }

        @Override
        boolean contains(final Integer object) {
            readLock.lock();

            try {
                Entry pred = this.head;
                Entry curr = pred.next;

                while (curr.object.compareTo(object) < 0) {
                    pred = curr;
                    curr = curr.next;
                }
                return object.equals(curr.object);
            } finally {
                readLock.unlock();
            }

        }
    }
}
