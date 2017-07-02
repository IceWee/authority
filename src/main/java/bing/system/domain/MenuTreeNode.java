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
public class MenuTreeNode extends GenericTreeNode {

	private static final long serialVersionUID = -7990370120900511376L;

	private boolean checked = false;

	private boolean isParent = true;

	private boolean active = false;

	private String iconSkin;

	private Integer nodeType;

	private String url;

	private List<MenuTreeNode> children = new ArrayList<>();

	private MenuTreeNode parentMenu = null;

	public void addChild(MenuTreeNode node) {
		this.children.add(node);
	}

	public void removeChild(MenuTreeNode node) {
		this.children.remove(node);
	}

	public boolean hasChild() {
		return !getChildren().isEmpty();
	}

	/**
	 * 递归构建树
	 * 
	 * @param parentTreeNodes 上级节点
	 * @param treeNodes 全部节点
	 */
	public static void buildMenuTree(List<MenuTreeNode> parentTreeNodes, List<MenuTreeNode> treeNodes) {
		parentTreeNodes.forEach(parentNode -> {
			String parentId = parentNode.getId();
			treeNodes.forEach(node -> {
				if (parentNode.isParent && Objects.equals(parentId, node.getParentId())) {
					parentNode.addChild(node);
					node.setParentMenu(parentNode);
				}
			});
			if (!parentNode.getChildren().isEmpty()) {
				buildMenuTree(parentNode.getChildren(), treeNodes);
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