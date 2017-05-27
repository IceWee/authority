package bing.system.service;

import java.util.List;

import bing.domain.GenericService;
import bing.domain.ResourceTreeNode;
import bing.system.condition.SysResourceCondition;
import bing.system.model.SysResource;
import bing.system.model.SysResourceCategory;
import bing.system.vo.SysResourceVO;

public interface SysResourceService extends GenericService<SysResource, SysResourceVO, SysResourceCondition> {

	List<ResourceTreeNode> getCategoryTree();

	SysResourceCategory getCategoryById(Integer categoryId);

	void saveCategory(SysResourceCategory category);

	void updateCategory(SysResourceCategory category);

	void deleteCategoryById(Integer categoryId, String username);

	/**
	 * 获取资源树，节点中有资源分类和资源，角色ID是用来自动勾选使用
	 * 
	 * @param roleId
	 * @return
	 */
	List<ResourceTreeNode> getResourceTree(Integer roleId);

	List<ResourceTreeNode> getResourceTree();

}
