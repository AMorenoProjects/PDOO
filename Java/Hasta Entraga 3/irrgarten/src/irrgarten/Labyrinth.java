/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package irrgarten;

import java.util.ArrayList;

/**
 *
 * @author alejandro
 */
public class Labyrinth {
    // Atributos de clase estáticos
    private static final char BLOCK_CHAR = 'X';
    private static final char EMPTY_CHAR = '-';
    private static final char MONSTER_CHAR = 'M';
    private static final char COMBAT_CHAR = 'C';
    private static final char EXIT_CHAR = 'E';
    private static final int ROW = 0;
    private static final int COL = 1;
    
    // Atributos de instancia
    private int nRows;
    private int nCols;
    private int exitRow;
    private int exitCol;
    
    // Tablas
    private Monster monster[][];
    private Player players[][];
    private char laberinto[][];
    
    // Constructor
    public Labyrinth(int nRows, int nCols, int exitRow, int exitCol){
        this.nRows = nRows;
        this.nCols = nCols;
        this.exitRow = exitRow;
        this.exitCol = exitCol;
        
        this.monster = new Monster[nRows][nCols];
        this.players = new Player[nRows][nCols];
        this.laberinto = new char[nRows][nCols];
        
        //Inicializo el tablero de caracteres
        for(int i=0; i <nRows;i++){
            for(int j=0; j < nCols;j++){
                this.laberinto[i][j] = EMPTY_CHAR;
            }
        }
        
        //Coloco la salida del laberinto
        this.laberinto[exitRow][exitCol] = EXIT_CHAR;
    }
    
    // Método completado
    public void spreadPlayers(ArrayList<Player> players){
        for(int i = 0; i < players.size();i++){
            Player p = players.get(i);
            
            int pos[] = randomEmptyPos();
            
            putPlayer2D(-1,-1,pos[ROW],pos[COL],p);
        }
    }
    
    // Devuelve true si el jugador está en la casilla de salida
    public boolean haveAWinner(){
        return this.players[this.exitRow][this.exitCol] != null;
    }
    
    //
    @Override
    public String toString(){
        String s = " ";
        
        for(int i=0; i < nRows;i++){
            for(int j=0; j < nCols;j++){
                s = s + this.laberinto[i][j] + "\t";
            }
            s = s + "\n";
        }
        return s;
    }
    
    // Comprueba si la posición dada está dentro del tablero y si lo es
    // añade al monstruo al laberinto y actualiza su posición
    public void addMonster(int row, int col, Monster monstruo){
        if(posOK(row,col) && emptyPos(row,col)){
            this.laberinto[row][col] = MONSTER_CHAR;
            
            this.monster[row][col] = monstruo;
            monstruo.setPos(row,col);
        }
    }
    
    // Método completado
    public Monster putPlayer(Directions direction, Player player){
        int oldRow = player.getRow();
        int oldCol = player.getCol();
        
        int newPos[] = dir2Pos(oldRow,oldCol,direction);
        Monster monster = putPlayer2D(oldRow,oldCol,newPos[ROW], newPos[COL],player);
        
        return monster;
    }
    
    
    // Método completado
    public void addBlock(Orientation orientation, int startRow, int startCol, int length){
        int incRow;
        int incCol;
        
        if(orientation == Orientation.VERTICAL){
            incRow = 1;
            incCol = 0;
        } else{
            incRow = 0;
            incCol = 1;
        }
        
        int row = startRow;
        int col = startCol;
        
        while((posOK(row,col)) && (length > 0)){
            laberinto[row][col] = BLOCK_CHAR;
            length -= 1;
            row += incRow;
            col += incCol;
        }
    }
    
    // Método completado
    public ArrayList<Directions> validMoves(int row,int col){
        ArrayList<Directions> output = new ArrayList<>();
        
        if(canStepOn(row+1,col)){
            output.add(Directions.DOWN);
        }
        if(canStepOn(row-1,col)){
            output.add(Directions.UP);
        }
        if(canStepOn(row,col+1)){
            output.add(Directions.RIGHT);
        }
        if(canStepOn(row, col-1)){
            output.add(Directions.LEFT);
        }
        return output;
    }
    
    //
    private boolean posOK(int row,int col){
        return (row >= 0 && row < nRows) && (col >= 0 && col < nCols);
    }
    
    // 
    private boolean emptyPos(int row, int col){
        return this.laberinto[row][col] == EMPTY_CHAR;
    }
    
    // Devuelve true si la posición suministrada alberga solo un monstruo
    private boolean monsterPos(int row, int col){
        return this.laberinto[row][col] == MONSTER_CHAR;
    }
    
    // Devuelve true si la posición suministrada es la de salida
    private boolean exitPos(int row, int col){
        return row == this.exitRow && col == this.exitCol;
    }
    
    // Devuelve true si la posición suministrada contiene a la vez un monstruo y un jugador
    private boolean combatPos(int row,int col){
        return this.laberinto[row][col] == COMBAT_CHAR;
    }
    
    // Indica si la posición suministrada está dentro del laberinto y se
    // corresponde con una de estas tres opciones: casilla vacía, casilla donde 
    // habita un monstruo o salida
    private boolean canStepOn(int row, int col){
        return posOK(row,col) && (emptyPos(row,col) || monsterPos(row,col) || exitPos(row,col));
    }
    
    // Actualiza la casilla que un jugador abandona
    private void updateOldPos(int row, int col){
        if(posOK(row,col)){
            if(combatPos(row,col)){
                this.laberinto[row][col] = MONSTER_CHAR;
            } else{
                this.laberinto[row][col] = EMPTY_CHAR;
            }
        }
    }
    
    // Calcula la nueva posición [row, col] basada en una dirección 
    private int[] dir2Pos(int row, int col,Directions direction){
        int[] newPos = {row,col};
        
        // Switch con reglas, cambiado porque netbeans me daba la opción
        switch (direction){
            case UP -> newPos[ROW]--;
            case DOWN -> newPos[ROW]++;
            case LEFT -> newPos[COL]--;
            case RIGHT -> newPos[COL]++;
        }
        
        return newPos;
    }
    
    // Genera una posición [row, col] aleatoria que esté vacía 
    private int[] randomEmptyPos(){
        int row, col;
        
        do{
            row = Dice.randomPos(this.nRows);
            col = Dice.randomPos(this.nCols);
        } while (!this.emptyPos(row, col));
        
        return new int[]{row,col};
    }
    
    // Método completado
    private Monster putPlayer2D(int oldRow, int oldCol, int row, int col, Player player){
        Monster output = null;
        
        if(canStepOn(row,col)){
            if(posOK(oldRow,oldCol)){
                Player p = players[oldRow][oldCol];
                if(p == player){
                    updateOldPos(oldRow,oldCol);
                    players[oldRow][oldCol] = null;
                }
            }
            
            boolean monsterPos = monsterPos(row,col);
            
            if(monsterPos){
                laberinto[row][col] = COMBAT_CHAR;
                output = monster[row][col];
            }else{
                char number = (char)('0' + player.getNumber());
                laberinto[row][col] = number;          
            }
            
            players[row][col] = player;
            player.setPos(row,col);
        }
        
        return output;
    }
}
