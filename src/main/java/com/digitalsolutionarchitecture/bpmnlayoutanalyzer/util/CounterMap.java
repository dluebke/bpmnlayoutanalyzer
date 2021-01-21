package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CounterMap<K> {
	
	private final Map<K, Integer> store = new HashMap<>();
	
	public int inc(K key) {
		int newValue = get(key) + 1;
		store.put(key, newValue);
		return newValue;
	}
	
	public int get(K key) {
		Integer result = store.get(key);
		
		if(result != null) {
			return result;
		} else {
			return 0;
		}
	}
	
	public Set<K> keySet() {
		return store.keySet();
	}

	public int sum(@SuppressWarnings("unchecked") K... keys) {
		int result = 0;
		
		for(K key : keys) {
			result += get(key);
		}
		
		return result;
	}
	
	public int sum(List<K> keys) {
		int result = 0;
		
		for(K key : keys) {
			result += get(key);
		}
		
		return result;
	}

	public int sumAll() {
		int result = 0;
		for(Integer i : store.values()) {
			result += i;
		}
		return result;
	}
	
	@Override
	public String toString() {
		return store.toString();
	}
	
	@Override
	public int hashCode() {
		return store.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null || other.getClass() != this.getClass()) {
			return false;
		}
		
		return ((CounterMap<?>)other).store.equals(store);
	}
}
