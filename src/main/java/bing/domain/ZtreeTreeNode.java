package bing.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * zTree树节点
 * 
 * @author IceWee
 */
@Getter
@Setter
@NoArgsConstructor
public class ZtreeTreeNode extends GenericTreeNode {

	protected boolean checked = false;

	protected boolean isParent = true;

	protected String iconCls;

	protected Integer nodeType;

	private List<ZtreeTreeNode> children = new ArrayList<>();

	public void addChild(ZtreeTreeNode node) {
		this.children.add(node);
	}

	public void removeChild(ZtreeTreeNode node) {
		this.children.remove(node);
	}

	/**
	 * 递归构建树
	 * 
	 * @param parentTreeNodes 上级节点
	 * @param treeNodes 全部节点
	 */
	public static void buildTree(List<ZtreeTreeNode> parentTreeNodes, List<ZtreeTreeNode> treeNodes) {
		parentTreeNodes.forEach(parentNode -> {
			String parentId = parentNode.getId();
			treeNodes.forEach(node -> {
				if (parentNode.isParent && Objects.equals(parentId, node.getParentId())) {
					parentNode.addChild(node);
				}
			});
			if (!parentNode.getChildren().isEmpty()) {
				buildTree(parentNode.getChildren(), treeNodes);
			}
		});
	}

	public boolean isIsParent() {
		return this.isParent;
	}

}