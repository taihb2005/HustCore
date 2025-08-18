package org.kat.app.util;

import org.kat.app.ui.views.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class GenericViewTree<T extends View> implements Tree<T>{
    TreeNode<T> root;

    public GenericViewTree(){
        root = null;
    }

    public GenericViewTree(TreeNode<T> root){
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

    public List<View> getAll(TreeNode<T> root, Predicate<TreeNode<T>> predicate) {
        List<View> viewList = new ArrayList<>();

        inOrderTraverse(root,
                (node) -> {
                    if(predicate.test(node)) viewList.add(node.getData());
                });

        return viewList;
    }

    public <K extends View> int getComponentCount(TreeNode<T> root, Class<K> clazz){
        AtomicInteger count = new AtomicInteger();
        inOrderTraverse(root,
                (node) -> {
                    if(clazz.isInstance(root.getData())) count.getAndIncrement();
                });
        return count.get();
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
