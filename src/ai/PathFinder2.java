package ai;

import entity.Entity;
import map.GameMap;

import java.awt.*;
import java.util.ArrayList;

public class PathFinder2 {

    GameMap mp;
    Node[][] node;
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    boolean goalReached = false;
    int step = 0;

    public PathFinder2(GameMap mp) {
        this.mp = mp;
        instantiateNodes();
    }

    public void instantiateNodes() {
        node = new Node[mp.maxWorldCol][mp.maxWorldRow];

        int col = 0;
        int row = 0;

        while (col < mp.maxWorldCol && row < mp.maxWorldRow) {
            node[col][row] = new Node(col, row);

            col++;
            if (col == mp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }

    // Reset trạng thái của các Node
    public void resetNodes() {
        int col = 0;
        int row = 0;

        while (col < mp.maxWorldCol && row < mp.maxWorldRow) {
            node[col][row].open = false;
            node[col][row].checked = false;
            node[col][row].solid = false;

            col++;
            if (col == mp.maxWorldCol) {
                col = 0;
                row++;
            }
        }

        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {
        resetNodes();

        // Đặt nút bắt đầu và nút mục tiêu
        startNode = node[startCol][startRow];
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];
        openList.add(currentNode);

        int col = 0;
        int row = 0;

        // Xử lý trạng thái solid từ các đối tượng trong bản đồ
        checkForSolidTile(mp.inactiveObj);
        checkForSolidTile(mp.activeObj);
        checkForSolidTile(mp.npc);

        while (col < mp.maxWorldCol && row < mp.maxWorldRow) {
            getCost(node[col][row]);

            col++;
            if (col == mp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }

    public void getCost(Node node) {
        // G Cost
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;

        // H Cost
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        // F Cost
        node.fCost = node.gCost + node.hCost;
    }

    public boolean search() {
        while (!goalReached && step < 6000) {
            currentNode = getBestNode();

            if (currentNode == null) {
                break; // Không còn nút để kiểm tra
            }

            currentNode.checked = true;

            // Mở các nút lân cận
            int col = currentNode.col;
            int row = currentNode.row;

            if (row - 1 >= 0) openNode(node[col][row - 1]); // Lên
            if (col - 1 >= 0) openNode(node[col - 1][row]); // Trái
            if (row + 1 < mp.maxWorldRow) openNode(node[col][row + 1]); // Xuống
            if (col + 1 < mp.maxWorldCol) openNode(node[col + 1][row]); // Phải

            // Kiểm tra đạt đến mục tiêu
            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }

            step++;
        }

        return goalReached;
    }

    private Node getBestNode() {
        Node bestNode = null;
        int bestCost = Integer.MAX_VALUE;

        for (Node node : openList) {
            if (node.fCost < bestCost || (node.fCost == bestCost && node.gCost < bestCost)) {
                bestNode = node;
                bestCost = node.fCost;
            }
        }

        if (bestNode != null) {
            openList.remove(bestNode);
        }

        return bestNode;
    }

    public void openNode(Node node) {
        if (!node.open && !node.checked && !node.solid) {
            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }

    public void trackThePath() {
        Node current = goalNode;

        while (current != startNode) {
            pathList.add(0, current);
            current = current.parent;
        }
    }

    private void checkForSolidTile(Entity[] list) {
        for (Entity entity : list) {
            if (entity != null) {
                if (entity.solidArea1 != null) {
                    markSolidTiles(entity, entity.solidArea1);
                }
                if (entity.solidArea2 != null) {
                    markSolidTiles(entity, entity.solidArea2);
                }
            }
        }
    }

    private void markSolidTiles(Entity entity, Rectangle solidArea) {
        int worldCol = (entity.worldX + solidArea.x) / GameMap.childNodeSize;
        int worldRow = (entity.worldY + solidArea.y) / GameMap.childNodeSize;
        int xOffSet = (solidArea.width + solidArea.x) / GameMap.childNodeSize;
        int yOffSet = (solidArea.height + solidArea.y) / GameMap.childNodeSize;

        for (int x = worldCol; x <= worldCol + xOffSet; x++) {
            for (int y = worldRow; y <= worldRow + yOffSet; y++) {
                if (x < mp.maxWorldCol && y < mp.maxWorldRow) {
                    node[x][y].solid = true;
                }
            }
        }
    }
}
