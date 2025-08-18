package org.kat.app.util;

import org.kat.app.ui.views.View;
import org.kat.app.util.exception.TreeHasNoRootException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface Tree<T>{
    default void add(T data, String id){};
    default void add(T data){};
    default void remove(TreeNode<T> root){
        postOrderTraverse(root, TreeNode::remove);
    };

    void clear();
    TreeNode<T> getRoot();

    default TreeNode<T> get(TreeNode<T> root, Predicate<TreeNode<T>> predicate) {
        if(root == null){
            throw new TreeHasNoRootException();
        }

        Queue<TreeNode<T>> queue = new LinkedList<>();
        queue.add(root);

        while(!queue.isEmpty()){
            TreeNode<T> node = queue.poll();
            if(predicate.test(node)){
                return node;
            }
            queue.addAll(node.getChildren());
        }

        return null;
    }

    default void inOrderTraverse(TreeNode<T> root, Consumer<TreeNode<T>> consumer){
        Queue<TreeNode<T>> queue = new LinkedList<>();
        queue.add(root);

        while(!queue.isEmpty()){
            TreeNode<T> node = queue.poll();
            consumer.accept(node);
            queue.addAll(node.getChildren());
        }
    };

    default void postOrderTraverse(TreeNode<T> root, Consumer<TreeNode<T>> consumer){
        if(root == null) return;

        for(TreeNode<T> child: root.getChildren()){
            postOrderTraverse(child, consumer);
        }

        consumer.accept(root);
    };
}
