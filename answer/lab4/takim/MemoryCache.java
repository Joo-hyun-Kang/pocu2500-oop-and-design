package academy.pocu.comp2500.lab4;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MemoryCache {

    private static final long ERROR_PREVENTION_TIME = 1L;

    private static int maxInstanceCount = Integer.MAX_VALUE;

    private static HashMap<String, MemoryCache> instances = new HashMap<>();

    private static MemoryCache firstInstance;

    private static MemoryCache lastInstance;

    private static OffsetDateTime instanceLastUsedAt = OffsetDateTime.now();

    private static OffsetDateTime entryLastCreatedAt = OffsetDateTime.now();

    private static OffsetDateTime entryLastUsedAt = OffsetDateTime.now();

    private MemoryCache prev;

    private MemoryCache next;

    private EvictionPolicy evictionPolicy;

    private OffsetDateTime usedAt;

    private String name;

    private HashMap<String, Entry> entry;

    private int maxEntryCount;

    private Entry firstEntry;

    private Entry lastEntry;

    private MemoryCache() {
        OffsetDateTime time = OffsetDateTime.now();
        this.usedAt = time;
        this.entry = new HashMap<>();
        this.maxEntryCount = Integer.MAX_VALUE;
        this.evictionPolicy = EvictionPolicy.LEAST_RECENTLY_USED;
    }

    public class Entry {
        private final MemoryCache memoryCache;

        private OffsetDateTime createdAt;

        private OffsetDateTime usedAt;

        private Entry prev;

        private Entry next;

        private String key;

        private String value;

        public Entry(MemoryCache memoryCache, String key, String value) {
            OffsetDateTime time = OffsetDateTime.now();
            this.createdAt = time;
            this.usedAt = time;
            this.memoryCache = memoryCache;
            this.key = key;
            this.value = value;
        }
    }

    public void setEvictionPolicy(EvictionPolicy evictionPolicy) {
        this.evictionPolicy = evictionPolicy;

        if (this.firstEntry != null) {
            sortEntry();
        }
        while (this.entry.size() > maxEntryCount) {
            removeEntry();
        }
    }

    public static void setMaxInstanceCount(int count) {
        maxInstanceCount = count;
        while (instances.size() > maxInstanceCount) {
            removeInstance();
        }
    }

    public static MemoryCache getInstance(String cacheName) {
        if (instances.containsKey(cacheName)) {
            MemoryCache thisNode = instances.get(cacheName);
            thisNode.usedAt = OffsetDateTime.now();

            if (thisNode.usedAt.compareTo(instanceLastUsedAt) <= 0) {
                instanceLastUsedAt = instanceLastUsedAt.plusNanos(ERROR_PREVENTION_TIME);
                thisNode.usedAt = OffsetDateTime.of(instanceLastUsedAt.toLocalDateTime(), instanceLastUsedAt.getOffset());
            } else {
                instanceLastUsedAt = OffsetDateTime.of(thisNode.usedAt.toLocalDateTime(), thisNode.usedAt.getOffset());
            }

            if (lastInstance != thisNode) {
                if (thisNode.prev == null) {
                    thisNode.next.prev = null;
                    firstInstance = thisNode.next;
                } else {
                    thisNode.prev.next = thisNode.next;
                    thisNode.next.prev = thisNode.prev;
                }

                lastInstance.next = thisNode;
                thisNode.prev = lastInstance;
                lastInstance = thisNode;
                thisNode.next = null;
            }
            return thisNode;
        }

        if (instances.size() == maxInstanceCount) {
            removeInstance();
        }

        MemoryCache newNode = new MemoryCache();
        newNode.name = cacheName;

        if (instances.size() == 0) {
            firstInstance = newNode;
            lastInstance = newNode;
        } else {
            lastInstance.next = newNode;
            newNode.prev = lastInstance;
            lastInstance = newNode;
        }

        instances.put(cacheName, newNode);

        if (newNode.usedAt.compareTo(instanceLastUsedAt) <= 0) {
            instanceLastUsedAt = instanceLastUsedAt.plusNanos(ERROR_PREVENTION_TIME);
            newNode.usedAt = OffsetDateTime.of(instanceLastUsedAt.toLocalDateTime(), instanceLastUsedAt.getOffset());
        } else {
            instanceLastUsedAt = OffsetDateTime.of(newNode.usedAt.toLocalDateTime(), newNode.usedAt.getOffset());
        }

        return newNode;
    }

    public static void clear() {
        instances.clear();
        firstInstance = null;
        lastInstance = null;
    }

    public void addEntry(String key, String value) {
        if (this.entry.containsKey(key)) {
            Entry node = this.entry.get(key);
            if (!node.value.equals(value)) {
                node.value = value;
            }
            moveEntry(key);
            return;
        }

        if (this.entry.size() == maxEntryCount) {
            removeEntry();
        }

        Entry newNode = new Entry(this, key, value);

        if (this.entry.size() == 0) {
            this.firstEntry = newNode;
            this.lastEntry = newNode;
        } else {
            this.lastEntry.next = newNode;
            newNode.prev = this.lastEntry;
            this.lastEntry = newNode;
        }
        this.entry.put(key, newNode);

        if (newNode.createdAt.compareTo(entryLastCreatedAt) <= 0) {
            entryLastCreatedAt = entryLastCreatedAt.plusNanos(ERROR_PREVENTION_TIME);
            newNode.createdAt = OffsetDateTime.of(entryLastCreatedAt.toLocalDateTime(), entryLastCreatedAt.getOffset());
        } else {
            entryLastCreatedAt = OffsetDateTime.of(newNode.createdAt.toLocalDateTime(), newNode.createdAt.getOffset());
        }
    }

    public String getEntryOrNull(String key) {
        if (this.entry.containsKey(key)) {
            moveEntry(key);
            return this.entry.get(key).value;
        }

        return null;
    }

    public void setMaxEntryCount(int maxEntryCount) {
        this.maxEntryCount = maxEntryCount;
        while (this.entry.size() > this.maxEntryCount) {
            removeEntry();
        }
    }

    private static void removeInstance() {
        MemoryCache deleteNode = instances.get(firstInstance.name);
        firstInstance = deleteNode.next;
        firstInstance.prev = null;

        instances.remove(deleteNode.name);
    }

    private void removeEntry() {
        if (this.evictionPolicy.equals(EvictionPolicy.LAST_IN_FIRST_OUT)) {
            Entry deleteNode = this.entry.get(this.lastEntry.key);
            this.lastEntry = deleteNode.prev;
            this.lastEntry.next = null;

            this.entry.remove(deleteNode.key);
        } else {
            Entry deleteNode = this.entry.get(this.firstEntry.key);
            this.firstEntry = deleteNode.next;
            this.firstEntry.prev = null;

            this.entry.remove(deleteNode.key);
        }
    }

    private void sortEntry() {
        boolean swapped;
        switch (this.evictionPolicy) {
            case FIRST_IN_FIRST_OUT:
            case LAST_IN_FIRST_OUT:
                do {
                    swapped = false;
                    Entry thisNode = this.firstEntry;
                    while (thisNode != null) {
                        Entry checkNode = thisNode.next;
                        while (checkNode != null) {
                            // 오름차순 (등록된 날짜가 가장 오래되면 First 가장 최근이면 Last)
                            if (thisNode.createdAt.compareTo(checkNode.createdAt) > 0) {
                                // 가장자리 처리
                                if (this.firstEntry == thisNode) {
                                    this.firstEntry = checkNode;
                                } else {
                                    thisNode.prev.next = checkNode;
                                }

                                if (this.lastEntry == checkNode) {
                                    this.lastEntry = thisNode;
                                } else {
                                    checkNode.next.prev = thisNode;
                                }

                                // 인접한 노드 이라면
                                if (thisNode.next == checkNode) {
                                    checkNode.prev = thisNode.prev;
                                    thisNode.prev = checkNode;

                                    thisNode.next = checkNode.next;
                                    checkNode.next = thisNode;
                                } else {
                                    thisNode.next.prev = checkNode;
                                    checkNode.prev.next = thisNode;

                                    Entry temp = thisNode.next;
                                    thisNode.next = checkNode.next;
                                    checkNode.next = temp;

                                    temp = thisNode.prev;
                                    thisNode.prev = checkNode.prev;
                                    checkNode.prev = temp;
                                }

                                checkNode = thisNode.next;
                                swapped = true;
                            } else {
                                checkNode = checkNode.next;
                            }
                        }

                        thisNode = thisNode.next;
                    }
                } while (swapped);
                break;
            case LEAST_RECENTLY_USED:
                do {
                    swapped = false;
                    Entry thisNode = this.firstEntry;
                    while (thisNode.next != null) {
                        Entry checkNode = thisNode.next;
                        while (checkNode != null) {
                            // 오름차순 (수정된 날짜가 가장 오래되면 First 가장 최근이면 Last)
                            if (thisNode.usedAt.compareTo(checkNode.usedAt) > 0) {
                                // 가장자리 처리
                                if (this.firstEntry == thisNode) {
                                    this.firstEntry = checkNode;
                                } else {
                                    thisNode.prev.next = checkNode;
                                }

                                if (this.lastEntry == checkNode) {
                                    this.lastEntry = thisNode;
                                } else {
                                    checkNode.next.prev = thisNode;
                                }

                                // 인접한 노드 이라면
                                if (thisNode.next == checkNode) {
                                    checkNode.prev = thisNode.prev;
                                    thisNode.prev = checkNode;

                                    thisNode.next = checkNode.next;
                                    checkNode.next = thisNode;
                                } else {
                                    thisNode.next.prev = checkNode;
                                    checkNode.prev.next = thisNode;

                                    Entry temp = thisNode.next;
                                    thisNode.next = checkNode.next;
                                    checkNode.next = temp;

                                    temp = thisNode.prev;
                                    thisNode.prev = checkNode.prev;
                                    checkNode.prev = temp;
                                }

                                checkNode = thisNode.next;
                                swapped = true;
                            } else {
                                checkNode = checkNode.next;
                            }
                        }

                        thisNode = thisNode.next;
                    }
                } while (swapped);
                break;
            default:
                assert (false) : "sortEntry() error : evictionPolicy is invalid data";
                break;
        }
    }

    private void moveEntry(String key) {
        Entry node = this.entry.get(key);
        node.usedAt = OffsetDateTime.now();

        if (node.usedAt.compareTo(entryLastUsedAt) <= 0) {
            entryLastUsedAt = entryLastUsedAt.plusNanos(ERROR_PREVENTION_TIME);
            node.usedAt = OffsetDateTime.of(entryLastUsedAt.toLocalDateTime(), entryLastUsedAt.getOffset());
        } else {
            entryLastUsedAt = OffsetDateTime.of(node.usedAt.toLocalDateTime(), node.usedAt.getOffset());
        }

        if (this.evictionPolicy.equals(EvictionPolicy.LEAST_RECENTLY_USED) && this.lastEntry != node) {
            if (node.prev == null) {
                node.next.prev = null;
                this.firstEntry = node.next;
            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            }

            this.lastEntry.next = node;
            node.prev = this.lastEntry;
            this.lastEntry = node;
            node.next = null;
        }
    }
}
