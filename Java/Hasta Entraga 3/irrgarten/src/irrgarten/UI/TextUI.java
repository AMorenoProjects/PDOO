package irrgarten.UI;

import irrgarten.Directions;
import irrgarten.GameState;
import java.util.Scanner;


public class TextUI {
    
    private static Scanner in = new Scanner(System.in);
    
    private char readChar() {
        String s = in.nextLine();     
        return s.charAt(0);
    }
    

    public Directions nextMove() {
        System.out.print("Where? ");
        
        Directions direction = Directions.DOWN;
        boolean gotInput = false;
        
        while (!gotInput) {
            char c = readChar();
            switch(c) {
                case 'w':
                    System.out.print(" UP\n");
                    direction = Directions.UP;
                    gotInput = true;
                    break;
                case 's':
                    System.out.print(" DOWN\n");
                    direction = Directions.DOWN;
                    gotInput = true;
                    break;
                case 'd':
                    System.out.print("RIGHT\n");
                    direction = Directions.RIGHT;
                    gotInput = true;
                    break;
                case 'a':
                    System.out.print(" LEFT\n");
                    direction = Directions.LEFT;
                    gotInput = true;    
                    break;
            }
        }    
        return direction;
    }
    
    public void showGame(GameState gameState) {     
        // 1. El tablero 
        System.out.println(gameState.getLabyrinth());
        
        // 2. Saber a quién le toca
        System.out.println("Turno del Jugador: " + gameState.getCurrentPlayer());
        
        // 3. Mostrar mensajes de eventos (si ha habido combate, movimiento, etc.)
        String log = gameState.getLog();
        if (!log.isEmpty() && !log.isBlank()) {
            System.out.println("Eventos:\n" + log);
        }
        
        // 4. Mensaje final si alguien gana
        if(gameState.isWinner()) {
            System.out.println("¡Victoria! Ha ganado el jugador " + gameState.getCurrentPlayer());
        }
    }
    
}