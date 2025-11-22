/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package irrgarten;
import java.util.Random;
import java.util.ArrayList;

/**
 *
 * @author alejandro
 */
public class Dice {
    private static final int MAX_USES = 5;
    private static final float MAX_INTELLIGENCE = 10.0f;
    private static final float MAX_STRENGTH = 10.0f;
    private static final float RESURRECT_PROB = 0.3f;
    private static final int WEAPONS_REWARD = 2; 
    private static final int SHIELDS_REWARD = 3;
    private static final int HEALTH_REWARD = 5;
    private static final int MAX_ATTACK = 3;
    private static final int MAX_SHIELD = 2;
    
    // A través del objeto generator usamos los métodos .nextInt() y .nextFloat()
    private static final Random generator = new Random();
    
    // Devuelve valores entre 0 y max
    public static int randomPos(int max){
        return generator.nextInt(max);
    }
    
    // Devuelve el índice del jugador que empieza la partida, se empieza desde el 0
    public static int whoStarts(int nplayers){
        return generator.nextInt(nplayers);
    }
    
    // Devuelve una valor aleatorio de inteligencia del intervalo [0,MAX_STRENGTH[
    public static float randomIntelligence(){
        return generator.nextFloat(MAX_INTELLIGENCE);
    }
    
    // Devuelve una valor aleatorio de fuerza del intervalo [0,MAX_STRENGTH
    public static float randomStrength(){
        return generator.nextFloat(MAX_STRENGTH);
    }
    
    // indica si un jugador muerto debe ser resucitado o no.
    public static boolean resurrectPlayer(){
       return generator.nextFloat() < RESURRECT_PROB;
    }
    
    // Indica la cantidad de armas que recibirá el jugador por ganar el combate.
    public static int weaponsReward(){
        return generator.nextInt(WEAPONS_REWARD + 1);
    }
    
    // Indica la cantidad de escudos que recibirá el jugador por ganar el combate
    public static int shieldReward(){
        return generator.nextInt(SHIELDS_REWARD + 1);
    }
    
    // Indica la cantidad de unidades de salud que recibirá el jugador por ganar el combate.
    public static int healthReward(){
        return generator.nextInt(HEALTH_REWARD + 1);
    }
    
    // Devuelve un valor aleatorio en el intervalo [0, MAX_ATTACK]
    public static float weaponPower(){
        return generator.nextFloat(MAX_ATTACK);
    }
    
    // Devuelve un valor aleatorio en el intervalo [0, MAX_SHIELD]
    public static float shieldPower(){
        return generator.nextFloat(MAX_SHIELD);
    }
    
    // Devuelve el número de usos que se asignará a un arma o escudo
    public static int usesLeft(){
        return generator.nextInt(MAX_USES);
    }
    
    // Devuelve la cantidad de competencia aplicada
    public static float intensity(float competence){
        return generator.nextFloat(competence);
    }
    
    /* 
       Este método devuelve true con una probabilidad inversamente proporcional 
       a lo cercano que esté el parámetro del número máximo de usos que puede
       tener un arma o escudo
    */
    public static boolean discardElement(int usesLeft){
        if (usesLeft <= 0) {
            return true; // caso extremo
        }
        if (usesLeft >= MAX_USES) {
            return false; // caso extremo
        }
        
        float prob = 1.0f - ((float) usesLeft / (float) MAX_USES);
        return generator.nextFloat() < prob;
    }
    
    // Este método devolverá la dirección de movimiento preferente
    // con una probabilidad proporcional al valor de inteligencia suministrado
    public static Directions nextStep(Directions preference,ArrayList<Directions> validMoves, float intelligence){
        if(generator.nextFloat(MAX_INTELLIGENCE) > intelligence){
            return validMoves.get(generator.nextInt(2));
        }
        return preference;
    }
}
