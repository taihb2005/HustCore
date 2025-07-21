package org.kat.app.util;

import java.util.ArrayList;
import java.util.List;

public class TreeNode<T>{
    private final T data;

    private TreeNode<T> parent;
    private List<TreeNode<T>> children;

    public TreeNode(T data){
        this.data = data;
        children = new ArrayList<>();
    }

    public T getData() {
        return data;
    }

    public TreeNode<T> getParent(){
        return parent;
    }

    public List<TreeNode<T>> getChildren(){
        return children;
    }

    public void add(T data){
        TreeNode<T> newNode = new TreeNode<>(data);
        newNode.parent = this;

        if(children == null){
            children = new ArrayList<>();
        }

        children.add(newNode);
    }

    public void add(TreeNode<T> child) {
        if (child == null) return;

        if (child.parent != null) {
            child.parent.children.remove(child);
        }

        child.parent = this;

        if (children == null) {
            children = new ArrayList<>();
        }

        children.add(child);
    }

    public void remove() {
        if (parent != null && parent.children != null) {
            parent.children.remove(this);
        }
        if (children != null) {
            for (TreeNode<T> child : children) {
                child.parent = null;
            }
            children.clear();
            children = null;
        }
        parent = null;
    }

}
