package com.ly.base.common.redis;

public interface EntityCache <E>{
	
	public Class<E> getEntityClass();

	public String getByKey(String id) throws CacheAccessException;
	
	public E getEntityByKey(String id) throws CacheAccessException; 
	
	public void deleteByKey(String id) throws CacheAccessException;
	
	public void add( E entity) throws CacheAccessException;
	
	
}
