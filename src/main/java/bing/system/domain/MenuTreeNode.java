package bing.system.domain;

import bing.domain.GenericTreeNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    private String iconClass;

    private List<MenuTreeNode> children = new ArrayList<>();

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
     * @param treeNodes       全部节点
     */
    public static void buildMenuTree(List<MenuTreeNode> parentTreeNodes, List<MenuTreeNode> treeNodes) {
        parentTreeNodes.forEach(parentNode -> {
            String parentId = parentNode.getId();
            treeNodes.forEach(node -> {
                if (parentNode.isParent && Objects.equals(parentId, node.getParentId())) {
                    parentNode.addChild(node);
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MenuTreeNode other = (MenuTreeNode) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}