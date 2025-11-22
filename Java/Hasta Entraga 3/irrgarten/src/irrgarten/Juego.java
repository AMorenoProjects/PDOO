/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package irrgarten;

import irrgarten.Controller.Controller;
import irrgarten.UI.TextUI;

public class Juego {

    public static void main(String[] args) {
        // 1. Creación de la VISTA (UI)
        // Se encarga de la entrada y salida de datos por consola.
        TextUI view = new TextUI();

        // 2. Creación del MODELO (Game)
        // El constructor de Game recibe el índice del jugador que comienza.
        // Pasamos '0' para que empiece el primer jugador.
        // Nota: Game inicializa internamente el laberinto, jugadores y monstruos
        int jugadorInicial = 0;
        Game game = new Game(jugadorInicial);

        // 3. Creación del CONTROLADOR (Controller)
        // El controlador une la vista y el modelo.
        Controller controller = new Controller(game, view);

        // 4. Iniciar el juego
        // Esto lanza el bucle principal while(!endOfGame) definido en tu Controller.java
        controller.play();
    }
}