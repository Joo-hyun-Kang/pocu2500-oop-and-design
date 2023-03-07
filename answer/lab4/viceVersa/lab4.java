// EvictionPolicy.java(enum)는 생략


//MemoryCache.java

package academy.pocu.comp2500.lab4;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class MemoryCache {
    private static HashMap<String, MemoryCache> instanceMap = new HashMap<>();
    private static LinkedHashSet<String> instances = new LinkedHashSet<>();
    private static int maxInstanceCount = Integer.MAX_VALUE;
    private EvictionPolicy evictionPolicy = EvictionPolicy.LEAST_RECENTLY_USED;
    private HashMap<String, String> entryMap = new HashMap<>();
    private LinkedList<String> entryAddedOrder = new LinkedList<>();
    private LinkedHashSet<String> entryUsedOrder = new LinkedHashSet<>();
    private int maxEntryCount = Integer.MAX_VALUE;

    private MemoryCache() {
    }

    public static MemoryCache getInstance(final String myHardDiskName) {
        if (instanceMap.containsKey(myHardDiskName) == false) {
            instanceMap.put(myHardDiskName, new MemoryCache());
            instances.add(myHardDiskName);
            MemoryCache.removeExceedingMaxInstance();
        } else {
            instances.remove(myHardDiskName);
            instances.add(myHardDiskName);
        }
        return instanceMap.get(myHardDiskName);
    }

    public static void clear() {
        instances.clear();
        instanceMap.clear();
    }

    public static void setMaxInstanceCount(final int maxInstanceCount) {
        MemoryCache.maxInstanceCount = maxInstanceCount;
        MemoryCache.removeExceedingMaxInstance();
    }

    public void setEvictionPolicy(final EvictionPolicy evictionPolicy) {
        this.evictionPolicy = evictionPolicy;
    }

    public void addEntry(final String key, final String value) {
        if (entryMap.containsKey(key) == false) {
            this.removeExceedingMaxEntry(1);
            entryMap.put(key, value);
            entryAddedOrder.add(key);
            entryUsedOrder.add(key);
        } else {
            entryMap.put(key, value);
            entryUsedOrder.remove(key);
            entryUsedOrder.add(key);
        }
    }

    public String getEntryOrNull(final String key) {
        if (entryMap.containsKey(key) == false) {
            return null;
        } else {
            entryUsedOrder.remove(key);
            entryUsedOrder.add(key);
            return entryMap.get(key);
        }
    }

    public void setMaxEntryCount(final int maxEntryCount) {
        this.maxEntryCount = maxEntryCount;
        this.removeExceedingMaxEntry(0);
    }

    private static void removeExceedingMaxInstance() {
        while (instanceMap.size() > maxInstanceCount) {
            String key = instances.iterator().next();
            instances.remove(key);
            instanceMap.remove(key);
        }
    }

    private void removeExceedingMaxEntry(final int addEntry) {
        String key = null;
        while (entryMap.size() + addEntry > maxEntryCount) {
            if (this.evictionPolicy == EvictionPolicy.FIRST_IN_FIRST_OUT) {
                key = entryAddedOrder.get(0);
                entryAddedOrder.remove(0);
                entryUsedOrder.remove(key);
            } else if (this.evictionPolicy == EvictionPolicy.LAST_IN_FIRST_OUT) {
                key = entryAddedOrder.get(entryAddedOrder.size() - 1);
                entryAddedOrder.remove(entryAddedOrder.size() - 1);
                entryUsedOrder.remove(key);
            } else if (this.evictionPolicy == EvictionPolicy.LEAST_RECENTLY_USED) {
                key = entryUsedOrder.iterator().next();
                entryUsedOrder.remove(key);
                entryAddedOrder.remove(key);
            } else {
                assert (false) : "Unknown case evictionPolicy in 'removeExceedingMaxEntry' method";
            }
            entryMap.remove(key);
        }
    }
}