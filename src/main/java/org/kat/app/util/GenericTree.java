package org.kat.app.util;

import org.kat.app.ui.views.View;

public class GenericTree<T extends View> implements Tree<T>{
    TreeNode<T> root;

    public GenericTree(){
        root = null;
    }

    public GenericTree(TreeNode<T> root){
        this.root = root;
    }

    @Override
    public void add(T data, String id) {
        TreeNode<T> desNode = get(this.root,
                (node) -> node.getData().getId().equals(id));

        if(desNode != null) {
            desNode.add(data);
        }
    }
    @Override
    public void clear() {
        remove(this.root);
    }

    @Override
    public TreeNode<T> getRoot(){
        return this.root;
    }
}
