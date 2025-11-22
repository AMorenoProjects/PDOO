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
public class Game {
    // Atributos de clase
    private static final int MAX_ROUNDS = 10;
    private static final int MAX_PLAYERS = 4; // Número de jugadores máximos
    private static final int MAX_MONSTERS = 10; // Número de monstruos máximos
    private static final int ROWS = 10; // Filas por defecto - Preguntar
    private static final int COLS = 10; // Comlumnas por defecto - Preguntar
    
    // Atributos de instancia
    private int currentPlayerIndex;
    private String log;
    
    private Labyrinth labyrinth;
    private ArrayList<Player> players;
    private ArrayList<Monster> monsters;

    // Constructor
    public Game(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
        this.log = " ";
        
        // Inicializo los players
        this.players = new ArrayList<>();
        
        for(int i=0; i < MAX_PLAYERS; i++){
            Player newPlayer = new Player(i, Dice.randomIntelligence(), Dice.randomStrength());
            this.players.add(newPlayer);
        }
        
        // Inicializo los monstruos
        this.monsters  = new ArrayList<>();
        
        // Asumo los valores del laberinto ------ Preguntar al profesor
        int rows = ROWS;
        int cols = COLS;
        int exitR = Dice.randomPos(rows);
        int exitC = Dice.randomPos(cols);
        
        // Inicializo el laberinto
        this.labyrinth = new Labyrinth(rows,cols,exitR,exitC);
        
        // Lamo al método que te configura el laberinto
        this.configureLabyrinth();
        
        this.labyrinth.spreadPlayers(players);
    }
    
    
    // Devuelve true si hay un ganador
    public boolean finished(){
        return labyrinth.haveAWinner();
    }
    
    // Método completado
    public boolean nextStep(Directions preferredDirection){
        this.log = "";
        boolean dead = players.get(currentPlayerIndex).dead();
        
        if(!dead){
            Directions direction = actualDirection(preferredDirection);
            
            if(direction != preferredDirection){
                logPlayerNoOrders();
            }
            
            Monster monster = labyrinth.putPlayer(direction, players.get(currentPlayerIndex));
        
            if(monster == null){
                logNoMonster();
            } else{
                GameCharacter winner = combat(monster);
                manageReward(winner);
            }
        } else{
            manageResurrection();
        }
        
        boolean endGame = finished();
        
        if(!endGame){
            nextPlayer();
        }
        
        return endGame;
    }
    
    // Devuelve toda la informacición actual del estado del juegoó
    public GameState getGameState(){
        String laberinto = this.labyrinth.toString();
        
        String playersString = "";
        for(int i= 0; i < MAX_PLAYERS;i++){
            playersString += this.players.get(i).toString() + "\n";
        }
        
        String monsterString = " ";
        for(int i=0; i < MAX_MONSTERS; i++){
            monsterString += this.monsters.get(i).toString() + "\n";
        }
        
        return new GameState(laberinto,playersString,monsterString,this.currentPlayerIndex,this.finished(), this.log);
    }
    
    // Configura el laberinto añadiendo bloques de obstáculos y monstruos.
    private void configureLabyrinth(){
    // Añado bloques para rodear el escenario
        this.labyrinth.addBlock(Orientation.HORIZONTAL, 0, 0, COLS); // Pared superior
        this.labyrinth.addBlock(Orientation.HORIZONTAL, ROWS - 1, 0, COLS); // Pared inferior
        this.labyrinth.addBlock(Orientation.VERTICAL, 0, 0, ROWS); // Pared izquierda
        this.labyrinth.addBlock(Orientation.VERTICAL, 0, COLS - 1, ROWS); // Pared derecha
        

        this.labyrinth.addBlock(Orientation.HORIZONTAL, 2, 2, 4); 
        this.labyrinth.addBlock(Orientation.VERTICAL, 4, 5, 4);   
        this.labyrinth.addBlock(Orientation.VERTICAL, 6, 2, 3);
        this.labyrinth.addBlock(Orientation.HORIZONTAL, 3, 7, 2);
        
        // Añado monstruos
        for(int i = 0; i < MAX_MONSTERS; i++){
            Monster monster = new Monster("Monster #" + i, Dice.randomIntelligence(), Dice.randomStrength());
            
            int r = Dice.randomPos(ROWS);
            int c = Dice.randomPos(COLS);
            
            this.labyrinth.addMonster(r, c, monster);
            
            this.monsters.add(monster);
        }
    }
    
    // Actualiza los dos atributos que indican el jugador (current*) con el turno pasando al siguiente jugador.
    private void nextPlayer(){
        // Uso el módulo para que sea ciclicoó
        this.currentPlayerIndex = (this.currentPlayerIndex + 1) % this.players.size();
    }
    
    // Método completado
    private Directions actualDirection(Directions preferredDirection){
        int currentRow = players.get(currentPlayerIndex).getRow();
        int currentCol = players.get(currentPlayerIndex).getCol();
        
        ArrayList<Directions> validMoves = labyrinth.validMoves(currentRow, currentCol);
        
        Directions output = players.get(currentPlayerIndex).move(preferredDirection, validMoves);
        
        return output;
    }
    
    // Método completado
    private GameCharacter combat(Monster monster){
        int rounds = 0;
        GameCharacter winner;
        winner = GameCharacter.PLAYER;
        
        float playerAttack = players.get(currentPlayerIndex).attack();
        boolean lose = monster.defend(playerAttack);
        
        while(!lose && (rounds < MAX_ROUNDS)){
            winner = GameCharacter.MONSTER;
            rounds++;
            
            float monsterAttack = monster.attack();
            
            lose = players.get(currentPlayerIndex).defend(monsterAttack);
            
            if(!lose){
                playerAttack = players.get(currentPlayerIndex).attack();
                
                winner = GameCharacter.PLAYER;
                
                lose = monster.defend(playerAttack);
            }
        }
        
        logRounds(rounds,MAX_ROUNDS);
        
        return winner;         
    }
    
    // Método completado
    private void manageReward(GameCharacter winner){
        if(winner == GameCharacter.PLAYER){
            players.get(currentPlayerIndex).receiveReward();
            logPlayerWon();
        } else{
            logMonsterWon();
        }
    }
    
    // Método completado
    private void manageResurrection(){
        boolean resurrect = Dice.resurrectPlayer();
        
        if(resurrect){
            players.get(currentPlayerIndex).resurrect();
            logResurrected();
        } else{
            logPlayerSkipTurn();
        }
    }
    
    // Añade al final del atributo log (concatena cadena al final) el mensaje
    // indicando que el jugador ha ganado el combate.
    private void logPlayerWon(){
        this.log += " ¡Victoria! \n";
    }
    
    // Añade al final del atributo log (concatena cadena al final) el mensaje
    // indicando que el monstruo ha ganado el combate.
    private void logMonsterWon(){
        this.log += " ¡Monster Won! \n";
    }
    
    // Añade al final del atributo log (concatena cadena al final) el mensaje
    // indicando que el jugador ha resucitado
    private void logResurrected(){
        this.log += " ¡El jugador ha resucitado! \n";
    }
    
    // Añade al final del atributo log (concatena cadena al final) el mensaje
    // indicando que el jugador ha perdido el turno por estar muerto.
    private void logPlayerSkipTurn(){
        this.log += " ¡El jugador perdió el turno! \n";
    }
    
    // Añade al final del atributo log (concatena cadena al final) el mensaje
    // indicando que el jugador no ha seguido las instrucciones del jugador humano (no fue posible).
    private void logPlayerNoOrders(){
        this.log += " ¡No se puede realizar esa acción! \n";
    }
    
    // Añade al final del atributo log (concatena cadena al final) el mensaje
    // indicando que el jugador se ha movido a una celda vacía o no le ha sido posible moverse.
    private void logNoMonster(){
        this.log += " ¡Se ha movido a una casilla vacia, no se puede realizar dicha acción \n";
    }
    
    // Añade al final del atributo log (concatena cadena al final) el
    // mensaje que se han producido rounds de max rondas de combate. También añade el indicador de
    // nueva línea al final.
    private void logRounds(int rounds, int max){
        this.log += " ¡Se han producido " + rounds + " de " + max + " rondas! \n";
    }
    
    
    
}
