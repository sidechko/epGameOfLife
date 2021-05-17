package educationPractice.model;

import java.util.Random;
/***
 * Доска для отрисовки
 */
public class Board {
    public int width;
    public int height;
    public boolean[][] tempCellsData;
    public boolean[][] cellsData;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.cellsData = new boolean[width][height];
        this.tempCellsData = new boolean[width][height];
        clearData(cellsData);
        clearData(tempCellsData);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (boolean[] row:this.cellsData) {
            for (boolean el: row) {
                String tempStr = el ? "  " : "\u2588\u2588";
                sb.append(tempStr);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public void clearData(boolean[][] clearedData){
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                clearedData[j][i] = false;
            }
        }
    }

    public void generate(int countLivedCell){
        int x,y,i = 0;
        Random rnd = new Random();
        while (i<countLivedCell){
            x = rnd.nextInt(this.width);
            y = rnd.nextInt(this.height);
            if(this.cellsData[x][y]) continue;
            this.cellsData[x][y] = true;
            i++;
        }
    }

    public void nextStep(Rules rules){
        for(int h = 0; h <height;h++){
            for (int w = 0; w < width; w++) {
                this.checkNeighbors(w,h,rules);
            }
        }
        this.migrateFromTemp();
    }

    public void checkNeighbors(int i, int j, Rules rules){
        int cLB = 0;
        try{
            if(this.cellsData[i+1][j+1])cLB++;
        } catch (Exception ignored) {}
        try{
            if(this.cellsData[i][j+1])cLB++;
        } catch (Exception ignored) {}
        try{
            if(this.cellsData[i-1][j+1])cLB++;
        } catch (Exception ignored) {}

        try{
            if(this.cellsData[i+1][j])cLB++;
        } catch (Exception ignored) {}
        try{
            if(this.cellsData[i-1][j])cLB++;
        } catch (Exception ignored) {}

        try{
            if(this.cellsData[i+1][j-1])cLB++;
        } catch (Exception ignored) {}
        try{
            if(this.cellsData[i][j-1])cLB++;
        } catch (Exception ignored) {}
        try{
            if(this.cellsData[i-1][j-1])cLB++;
        } catch (Exception ignored) {}

        if(cLB == rules.forSafe && this.cellsData[i][j]) this.tempCellsData[i][j] = true;
        else this.tempCellsData[i][j] = cLB == rules.forBirth;
    }

    public void migrateFromTemp(){
        for(int h = 0; h <height;h++){
            for (int w = 0; w < width; w++) {
            this.cellsData[w][h] = this.tempCellsData[w][h];
            }
        }
        this.clearData(tempCellsData);
    }
}
