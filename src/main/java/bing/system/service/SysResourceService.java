package bing.system.service;

import java.util.List;

import bing.domain.GenericService;
import bing.system.condition.SysResourceCondition;
import bing.system.model.SysResource;
import bing.system.vo.SysResourceCategoryVO;
import bing.system.vo.SysResourceVO;

public interface SysResourceService extends GenericService<SysResource, SysResourceVO, SysResourceCondition> {

	List<SysResourceCategoryVO> getCategoryTree();

}
