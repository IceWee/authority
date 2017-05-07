package bing.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import bing.constant.GlobalConstants;
import bing.constant.TreeNodeTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 通用树节点
 * 
 * @author IceWee
 */
@Getter
@Setter
@NoArgsConstructor
public class GenericTreeNode {

	protected String id;

	protected String text;

	protected boolean checked = false;

	protected String iconCls;

	protected Map<String, Object> attributes = new HashMap<>();

	protected List<GenericTreeNode> children = new ArrayList<>();

	public void addChild(GenericTreeNode child) {
		this.children.add(child);
	}

	/**
	 * 递归构建资源分类树
	 * 
	 * @param parentTreeNodes 上级节点
	 * @param treeNodes 全部节点
	 */
	public static void buildGenericTree(List<GenericTreeNode> parentTreeNodes, List<GenericTreeNode> treeNodes) {
		parentTreeNodes.forEach(parentNode -> {
			Object parentId = parentNode.getAttribute(GlobalConstants.ATTRIBUT_ID);
			Object parentType = parentNode.getAttribute(GlobalConstants.ATTRIBUT_TYPE);
			treeNodes.forEach(node -> {
				Object subParentId = node.getAttribute(GlobalConstants.ATTRIBUT_PARENT_ID);
				if (Objects.equals(TreeNodeTypeEnum.BRANCH.ordinal(), parentType) && Objects.equals(parentId, subParentId)) {
					parentNode.addChild(node);
				}
			});
			if (!parentNode.getChildren().isEmpty()) {
				buildGenericTree(parentNode.getChildren(), treeNodes);
			}
		});
	}

	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	public void setAttribute(String name, Object value) {
		attributes.put(name, value);
	}

}
