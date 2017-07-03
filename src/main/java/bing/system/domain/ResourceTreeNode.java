package bing.system.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bing.domain.GenericTreeNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResourceTreeNode extends GenericTreeNode {

	private static final long serialVersionUID = 8082943594655032068L;

	public static final Integer CATEGORY = 0;

	public static final Integer RESOURCE = 1;

	private String rid;

	private boolean checked = false;

	private boolean isParent = true;

	private String iconSkin;

	private Integer nodeType;

	private List<ResourceTreeNode> children = new ArrayList<>();

	public void addChild(ResourceTreeNode node) {
		this.children.add(node);
	}

	public void removeChild(ResourceTreeNode node) {
		this.children.remove(node);
	}

	/**
	 * 递归构建树
	 * 
	 * @param parentTreeNodes 上级节点
	 * @param treeNodes 全部节点
	 */
	public static void buildResourceTree(List<ResourceTreeNode> parentTreeNodes, List<ResourceTreeNode> treeNodes) {
		parentTreeNodes.forEach(parentNode -> {
			String parentId = parentNode.getRid();
			treeNodes.forEach(node -> {
				if (parentNode.isParent && Objects.equals(parentId, node.getParentId())) {
					parentNode.addChild(node);
				}
			});
			if (!parentNode.getChildren().isEmpty()) {
				buildResourceTree(parentNode.getChildren(), treeNodes);
			}
		});
	}

	public String getIconSkin() {
		if (this.getChildren().isEmpty()) {
			iconSkin = "";
		}
		return iconSkin;
	}

}