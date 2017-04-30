package bing.domain;

import bing.domain.GenericCondition;
import bing.domain.GenericObject;
import bing.domain.GenericPage;

public interface GenericService<T extends GenericObject, E extends GenericCondition> {

	GenericPage<T> listByPage(E condition);

	void save(T model);

	void update(T model);

	T getById(Integer id);

	void deleteById(Integer id, String username);

}
