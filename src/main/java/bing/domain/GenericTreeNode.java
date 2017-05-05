package bing.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 通用树节点
 * 
 * @author IceWee
 */
public class GenericTreeNode {

	protected Integer id;

	protected Integer parentId;

	protected String text;

	protected String type;

	protected boolean checked = false;

	protected Map<String, Object> attributes = new HashMap<>();

	protected List<GenericTreeNode> children = new ArrayList<>();

	public GenericTreeNode() {
		super();
	}

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
			treeNodes.forEach(node -> {
				if (Objects.equals(parentNode.getId(), node.getParentId())) {
					parentNode.addChild(node);
				}
			});
			if (!parentNode.getChildren().isEmpty()) {
				buildGenericTree(parentNode.getChildren(), treeNodes);
			}
		});
	}

	public void setAttribute(String name, Object value) {
		attributes.put(name, value);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public List<GenericTreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<GenericTreeNode> children) {
		this.children = children;
	}

}
