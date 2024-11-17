package ai;

import entity.Entity;
import main.GamePanel;
import map.GameMap;

import java.util.ArrayList;

public class PathFinder {

    GameMap mp;
    Node[][] node;
    ArrayList<Node> openList = new ArrayList<>();
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

    //reset previous pathfinding result
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
        //reset other settings
        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }
    public void setNodes(int startCol, int startRow, int goalCol, int goalRow, Entity entity)
    {
        resetNodes();
        //set Start and Goal node
        startNode = node[startCol][startRow];
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];
        openList.add(currentNode);

        int col = 0;
        int row = 0;
        while(col < mp.maxWorldCol && row < mp.maxWorldRow)
        {
            //SET SOLID NODE
            for(int i = 0 ; i < mp.inactiveObj.length ; i++){
                if(mp.inactiveObj[i] != null ){
                    if(mp.inactiveObj[i].solidArea1 != null) {
                        int worldCol = (mp.inactiveObj[i].worldX + mp.inactiveObj[i].solidArea1.x) / 64;
                        int worldRow = (mp.inactiveObj[i].worldY + mp.inactiveObj[i].solidArea1.y) / 64;
                        int totalWidth = (mp.inactiveObj[i].solidArea1.x + mp.inactiveObj[i].solidArea1.width);
                        int totalHeight = (mp.inactiveObj[i].solidArea1.y + mp.inactiveObj[i].solidArea1.height);
                        int xOffSet = (totalWidth % 64 == 0) ? totalWidth / 64 : totalWidth / 64 + 1;
                        int yOffSet = (totalHeight % 64 == 0) ? totalHeight / 64 : totalHeight / 64 + 1;
                        for (int x = worldCol; x < worldCol + xOffSet; x++) {
                            for (int y = worldRow; y < worldRow + yOffSet; y++) {
                                node[x][y].solid = true;
                            }
                        }
                    }
                    if(mp.inactiveObj[i].solidArea2 != null){
                        int worldCol = (mp.inactiveObj[i].worldX + mp.inactiveObj[i].solidArea2.x) / 64;
                        int worldRow = (mp.inactiveObj[i].worldY + mp.inactiveObj[i].solidArea2.y) / 64;
                        int totalWidth = (mp.inactiveObj[i].solidArea2.x + mp.inactiveObj[i].solidArea2.width);
                        int totalHeight = (mp.inactiveObj[i].solidArea2.y + mp.inactiveObj[i].solidArea2.height);
                        int xOffSet = (totalWidth % 64 == 0) ? totalWidth / 64 : totalWidth / 64 + 1;
                        int yOffSet = (totalHeight % 64 == 0) ? totalHeight / 64 : totalHeight / 64 + 1;
                        for (int x = worldCol; x < worldCol + xOffSet; x++) {
                            for (int y = worldRow; y < worldRow + yOffSet; y++) {
                                node[x][y].solid = true;
                            }
                        }
                    }

                }
            }

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
        while(!goalReached && step < 500)
        {
            int col = currentNode.col;
            int row = currentNode.row;

            //check the current node
            currentNode.checked = true;
            openList.remove(currentNode);

            //open the UP node
            if(row-1 >= 0)
            {
                openNode(node[col][row-1]);
            }
            //open the LEFT node
            if(col - 1 >= 0)
            {
                openNode(node[col-1][row]);
            }
            //open the DOWN node
            if(row + 1 < mp.maxWorldRow)
            {
                openNode(node[col][row+1]);
            }
            //open the RIGHT node
            if(col + 1 < mp.maxWorldCol)
            {
                openNode(node[col+1][row]);
            }

            //Find the best node
            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for(int i = 0; i < openList.size(); i++)
            {
                //Check if this node's F cost is better
                if(openList.get(i).fCost < bestNodefCost)
                {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                }
                //If F cost is equal, check the G cost
                else if(openList.get(i).fCost == bestNodefCost)
                {
                    if(openList.get(i).gCost < openList.get(bestNodeIndex).gCost)
                    {
                        bestNodeIndex = i;

                    }
                }
            }

            //If there is no node in the openList, end the loop
            if(openList.isEmpty())
            {
                break;
            }

            //After the loop, openList(bestNodeIndex] is the next step (= currentNode)
            currentNode = openList.get(bestNodeIndex);

            if(currentNode == goalNode)
            {
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
            pathList.add(0,current); //last added node is in the [0]
            current = current.parent;
        }
    }
    private void checkForSolidTile(Entity[] list){

    }
}