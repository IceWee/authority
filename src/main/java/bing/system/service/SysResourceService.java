package bing.system.service;

import java.util.List;

import bing.domain.GenericService;
import bing.domain.GenericTreeNode;
import bing.system.condition.SysResourceCondition;
import bing.system.model.SysResource;
import bing.system.model.SysResourceCategory;
import bing.system.vo.SysResourceVO;

public interface SysResourceService extends GenericService<SysResource, SysResourceVO, SysResourceCondition> {

	List<GenericTreeNode> getCategoryTree();

	SysResourceCategory getCategoryById(Integer categoryId);

	void saveCategory(SysResourceCategory category);

	void updateCategory(SysResourceCategory category);

	void deleteCategoryById(Integer categoryId, String username);

}
