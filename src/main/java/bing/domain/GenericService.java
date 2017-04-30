package bing.domain;

public interface GenericService<T extends GenericObject, V extends GenericObject, E extends GenericCondition> {

	GenericPage<V> listByPage(E condition);

	void save(T entity);

	void update(T entity);

	T getById(Integer id);

	void deleteById(Integer id, String username);

}
