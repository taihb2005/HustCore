package ai;

import entity.Entity;
import map.GameMap;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PathFinder {
    GameMap mp;
    public Node[][] node;
    //ArrayList<Node> openList = new ArrayList<>();
    public PriorityQueue<Node> openList = new PriorityQueue<>((n1, n2) -> {
        if (n1.fCost != n2.fCost) {
            return Integer.compare(n1.fCost, n2.fCost); //
        }
        return Integer.compare(n1.gCost, n2.gCost);
    });
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    public boolean goalReached = false;
    int step = 0;

    public PathFinder(GameMap mp)
    {
        this.mp = mp;
        instantiateNodes();
    }
    public void instantiateNodes()
    {
        node = new Node[mp.maxWorldCol][mp.maxWorldRow];

        int col = 0;
        int row = 0;

        while(col < mp.maxWorldCol && row < mp.maxWorldRow)
        {
            node[col][row] = new Node(col,row);

            col++;
            if(col == mp.maxWorldCol)
            {
                col = 0;
                row++;
            }
        }
    }

    public void resetNodes()
    {
        int col = 0;
        int row = 0;
        while(col < mp.maxWorldCol && row < mp.maxWorldRow)
        {
            //reset open, checked and solid state
            node[col][row].open = false;
            node[col][row].checked = false;
            node[col][row].solid = false;

            col++;
            if(col == mp.maxWorldCol)
            {
                col = 0;
                row++;
            }
        }

        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }
    public void setNodes(int startCol, int startRow, int goalCol, int goalRow)
    {
        resetNodes();

        startNode = node[startCol][startRow];
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];
        openList.add(currentNode);

        int col = 0;
        int row = 0;

        checkForSolidTile(mp.inactiveObj);
        checkForSolidTile(mp.activeObj);
        checkForSolidTile(mp.npc);

        while(col < mp.maxWorldCol && row < mp.maxWorldRow)
        {
            //SET SOLID NODE

            //CHECK TILES

            //SET COST
            getCost(node[col][row]);

            col++;
            if(col == mp.maxWorldCol)
            {
                col = 0;
                row++;
            }
        }
    }
    public void getCost(Node node)
    {
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
    public boolean search()
    {
        while(!goalReached && step < 1000)
        {
            currentNode = openList.poll();

            if (currentNode == null) {
                break;
            }

            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.checked = true;

            if (row - 1 >= 0) {
                openNode(node[col][row - 1]);
            }
            if (col - 1 >= 0) {
                openNode(node[col - 1][row]);
            }
            if (row + 1 < mp.maxWorldRow) {
                openNode(node[col][row + 1]);
            }
            if (col + 1 < mp.maxWorldCol) {
                openNode(node[col + 1][row]);
            }

            // Kiểm tra xem đã đạt đến nút mục tiêu chưa
            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }

            step++;
        }
        return goalReached;
    }
    public void openNode(Node node)
    {
        if(!node.open && !node.checked && !node.solid)
        {
            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }
    public void trackThePath()
    {
        Node current = goalNode;

        while(current != startNode)
        {
            pathList.add(0,current);
            current = current.parent;
        }
    }

    private void checkForSolidTile(Entity[] list){
        for(int i = 0 ; i < list.length ; i++){
            if(list[i] != null ){
                if(list[i].solidArea1 != null) {
                    int worldCol = (list[i].worldX + list[i].solidArea1.x) / mp.childNodeSize;
                    int worldRow = (list[i].worldY + list[i].solidArea1.y) / mp.childNodeSize;
                    int totalWidth = (list[i].solidArea1.x + list[i].solidArea1.width);
                    int totalHeight = (list[i].solidArea1.y + list[i].solidArea1.height);
                    int xOffSet = (totalWidth % mp.childNodeSize == 0) ? totalWidth / mp.childNodeSize : totalWidth / mp.childNodeSize + 1;
                    int yOffSet = (totalHeight % mp.childNodeSize == 0) ? totalHeight / mp.childNodeSize : totalHeight / mp.childNodeSize + 1;
                    for (int x = worldCol; x < worldCol + xOffSet; x++) {
                        for (int y = worldRow; y < worldRow + yOffSet; y++) {
                            if(x >= mp.maxWorldCol || y >= mp.maxWorldCol) continue;
                            node[x][y].solid = true;
                        }
                    }
                }
                if(list[i].solidArea2 != null){
                    int worldCol = (list[i].worldX + list[i].solidArea2.x) / mp.childNodeSize;
                    int worldRow = (list[i].worldY + list[i].solidArea2.y) / mp.childNodeSize;
                    int totalWidth = (list[i].solidArea2.x + list[i].solidArea2.width);
                    int totalHeight = (list[i].solidArea2.y + list[i].solidArea2.height);
                    int xOffSet = (totalWidth % mp.childNodeSize == 0) ? totalWidth / mp.childNodeSize : totalWidth / mp.childNodeSize + 1;
                    int yOffSet = (totalHeight % mp.childNodeSize == 0) ? totalHeight / mp.childNodeSize : totalHeight / mp.childNodeSize + 1;
                    for (int x = worldCol; x < worldCol + xOffSet; x++) {
                        for (int y = worldRow; y < worldRow + yOffSet; y++) {
                            if(x >= mp.maxWorldCol || y >= mp.maxWorldCol) continue;
                            node[x][y].solid = true;
                        }
                    }
                }
            }
        }

    }
}

