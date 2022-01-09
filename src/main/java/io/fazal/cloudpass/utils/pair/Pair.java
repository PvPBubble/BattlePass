// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.utils.pair;

import java.util.Objects;
import java.io.Serializable;

public class Pair<K, V> implements Serializable
{
    private final K key;
    private final V value;
    
    public Pair(@NamedArg("key") final K key, @NamedArg("value") final V value) {
        this.key = key;
        this.value = value;
    }
    
    public K getKey() {
        return this.key;
    }
    
    public V getValue() {
        return this.value;
    }
    
    @Override
    public String toString() {
        return this.key + "=" + this.value;
    }
    
    @Override
    public int hashCode() {
        return this.key.hashCode() * 13 + ((this.value == null) ? 0 : this.value.hashCode());
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Pair) {
            final Pair pair = (Pair)o;
            return Objects.equals(this.key, pair.key) && Objects.equals(this.value, pair.value);
        }
        return false;
    }
}
