package org.kat.app.ui.views;

import org.kat.app.ui.Alignment;

import java.awt.*;

abstract public class View {
    protected final static Padding DEFAULT_PADDING = new Padding(0,0,0,0);

    protected int x;
    protected int y;
    protected int width;
    protected int height;

    protected int parentX;
    protected int parentY;
    protected int parentWidth;
    protected int parentHeight;

    protected String id;

    protected Padding padding = DEFAULT_PADDING;
    protected Alignment hAlign = Alignment.HORIZONTAL_CENTER;
    protected Alignment vAlign = Alignment.VERTICAL_CENTER;

    protected Visibility currentVisibility = Visibility.VISIBLE;
    protected Visibility lastVisibility = Visibility.INVISIBLE;
    // ======Getter========
    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setWidth(int width){
        this.width = width;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public void setDimensions(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setPadding(int left, int top, int right, int bottom){
        padding = new Padding(left, top, right, bottom);
    }

    public void setHorizontalAlignment(Alignment hAlignment) {
        this.hAlign = hAlignment;
    }

    public void setVerticalAlignment(Alignment vAlignment) {
        this.vAlign = vAlignment;
    }

    public void setAlignment(Alignment hAlignment, Alignment vAlignment){
        this.hAlign = hAlignment;
        this.vAlign = vAlignment;
    }

    public void show(){
        currentVisibility = Visibility.VISIBLE;
    }

    public void hide(){
        currentVisibility = Visibility.INVISIBLE;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getId(){
        return id;
    }

    public boolean isVisible(){
        return currentVisibility == Visibility.VISIBLE;
    }

    public void attach(View view){
        this.parentX = view.x + view.padding.pLeft;
        this.parentY = view.y + view.padding.pTop;
        this.parentWidth = view.width - view.padding.pLeft - view.padding.pRight;
        this.parentHeight = view.height - view.padding.pTop - view.padding.pBottom;
    }

    abstract public void render(Graphics2D g2);

    final public static class Padding{
        public int pLeft, pTop, pRight, pBottom;

        public Padding(int left, int top, int right, int bottom){
            pLeft = left;
            pTop = top;
            pRight = right;
            pBottom = bottom;
        }
    }
}
