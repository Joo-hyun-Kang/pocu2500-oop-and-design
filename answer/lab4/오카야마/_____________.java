package academy.pocu.comp2500.lab4;
import academy.pocu.comp2500.lab4.DLL;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryCache {
    private static int maxInstanceCount = Integer.MAX_VALUE;
    private static HashMap<String, MemoryCache> cacheMap = new HashMap<String, MemoryCache>();
    private static ArrayList<String> cacheKeyLRU = new ArrayList<String>();
    private int maxEntryCount;
    private EvictionPolicy evictionPolicy;
    private HashMap<String, String> entryMap;
    private ArrayList<String> entryKeys;
    private ArrayList<String> entryKeysLRU;

    private MemoryCache() {
        this.maxEntryCount = Integer.MAX_VALUE;
        this.evictionPolicy = EvictionPolicy.LEAST_RECENTLY_USED;

        this.entryMap = new HashMap<String, String>();
        this.entryKeys = new ArrayList<String>();
        this.entryKeysLRU = new ArrayList<String>();
    }

    public static MemoryCache getInstance(String instance) {
        if (cacheMap.containsKey(instance)) {
            MemoryCache cache = cacheMap.get(instance);

            cacheKeyLRU.remove(instance);
            cacheKeyLRU.add(instance);

            return cache;
        } else {
            if (cacheMap.size() >= maxInstanceCount) {
                removeInstance(1);
            }
            MemoryCache cache = new MemoryCache();
            cacheMap.put(instance, cache);
            cacheKeyLRU.add(instance);

            return cache;
        }
    }

    public static void clear() {
        cacheMap.clear();
        cacheKeyLRU.clear();
    }

    public static void setMaxInstanceCount(int instanceCount) {
        assert instanceCount >= 0;

        if (cacheMap.size() > instanceCount) {
            removeInstance(cacheMap.size() - instanceCount);
        }

        maxInstanceCount = instanceCount;
    }

    public void setEvictionPolicy(EvictionPolicy evictionPolicy) {
        this.evictionPolicy = evictionPolicy;
    }

    public void addEntry(String key, String value) { // update LRU
        if (this.entryMap.size() >= this.maxEntryCount && !this.entryMap.containsKey(key)) {
            removeEntry(1);
        }

        if (this.entryMap.containsKey(key)) {
            this.entryMap.put(key, value);
            this.entryKeysLRU.remove(key);
        } else {
            this.entryMap.put(key, value);
            this.entryKeys.add(key);
            this.entryKeysLRU.add(key);
        }
    }

    public String getEntryOrNull(String key) {
        if (this.entryMap.containsKey(key)) { // if entry is already in
            this.entryKeysLRU.remove(key); // update LRU
            this.entryKeysLRU.add(key);

            return this.entryMap.get(key);
        }

        return null;
    }

    public void setMaxEntryCount(int maxEntryCount) {
        assert maxEntryCount >= 0;

        if (this.entryMap.size() > maxEntryCount) {
            removeEntry(this.entryMap.size() - maxEntryCount);
        }
        this.maxEntryCount = maxEntryCount;
    }

    private static void removeInstance(int removeCount) {
        assert cacheMap.size() > removeCount;

        for (int i = 0; i < removeCount; ++i) {
            String key = cacheKeyLRU.get(0);
            cacheMap.remove(key);
            cacheKeyLRU.remove(key);
        }
    }

    private void removeEntry(int removeCount) {
        assert entryMap.size() > removeCount;

        for (int i = 0; i < removeCount; ++i) {
            switch (this.evictionPolicy) {
                case LAST_IN_FIRST_OUT:
                    int lastEntryIndex = this.entryKeys.size() - 1;
                    String key = this.entryKeys.get(lastEntryIndex);
                    this.entryMap.remove(key);
                    this.entryKeys.remove(lastEntryIndex);
                    this.entryKeysLRU.remove(key);
                    break;
                case FIRST_IN_FIRST_OUT:
                    key = this.entryKeys.get(0);
                    this.entryMap.remove(key);
                    this.entryKeys.remove(0);
                    this.entryKeysLRU.remove(key);
                    break;
                case LEAST_RECENTLY_USED:
                    key = this.entryKeysLRU.get(0);
                    this.entryMap.remove(key);
                    this.entryKeys.remove(key);
                    this.entryKeysLRU.remove(0);
                    break;
                default:
                    assert (false) : "unknown cache eviction policy" + this.evictionPolicy;
                    break;
            }
        }
    }
}

------------------------------------------------------------------------

package academy.pocu.comp2500.lab4;

public class DLL {
    private Node head;
    private Node tail;
    private int length;

    public class Node {
        private String data;
        private Node prev;
        private Node next;

        public Node(String data) {
            this.data = data;
        }
    }

    public DLL() {
        this.head = null;
        this.tail = null;
        this.length = 0;
    }

    public void addToHead(String data) {
        assert (data != null);

        Node newNode = new Node(data);
        if (isEmpty()) {
            tail = newNode;
        } else {
            head.prev = newNode;
        }
        newNode.next = head;
        head = newNode;

        ++length;
    }

    public void addToTail(String data) {
        assert (data != null);

        Node newNode = new Node(data);
        if (isEmpty()) {
            head = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
        }
        tail = newNode;

        ++length;
    }

    public Node removeHead() {
        assert (isEmpty() == false);

        Node temp = head;
        if (head == tail) {
            tail = null;
        } else {
            head.next.prev = null;
        }
        head = head.next;
        temp.next = null;
        return temp;
    }

    public Node removeTail() {
        assert (isEmpty() == false);

        Node temp = tail;
        if (head == tail) {
            head = null;
        } else {
            tail.prev.next = null;
        }
        tail = tail.prev;
        temp.prev = null;
        return temp;
    }

    public void printFrontToBack() {
        Node temp = head;

        while (temp != null) {
            System.out.printf("String value is : %s%s", temp.data, System.lineSeparator());
            temp = temp.next;
        }
    }

    public void printBackToFront() {
        Node temp = tail;

        while (temp != null) {
            System.out.printf("String value is : %s%s", temp.data, System.lineSeparator());
            temp = temp.prev;
        }
    }

    private boolean isEmpty() {
        return (length == 0);
    }
}