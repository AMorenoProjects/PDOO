/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package irrgarten;

/**
 *
 * @author alejandro
 */
public class Monster {
    // Atributos de clases estáticos
    private static final int INITIAL_HEALTH = 5;
    private static final int UNSET_POS = -1;
    
    // Atributos de instancia
    private String name;
    private float intelligence;
    private float strength;
    private float health;
    private int row;
    private int col;

    // Constructor de la clase Monster
    public Monster(String name, float intelligence, float strength) {
        this.name = name;
        this.intelligence = intelligence;
        this.strength = strength;
        this.health = INITIAL_HEALTH;
        this.row = UNSET_POS;
        this.col = UNSET_POS;
    }
    
    // Devuelve true si el monstruo está muerto
    public boolean dead(){
        return health <= 0;
    }
    
    // Devuelve la cantidad de daño que hace el monstruo
    public float attack(){
        return Dice.intensity(strength);
    }
    
    // Devuelve la cantidad de daño del que se defienfe el monstruo
    public boolean defend(float receivedAttack){
        boolean isDead = dead();
        
        if(!isDead){
            float devensiveEnergy = Dice.intensity(intelligence);
            if(devensiveEnergy < receivedAttack){
                gotWounded();
                isDead = dead();
            }
        }
        
        return isDead;
    }
    
    // Setter
    public void setPos(int row, int col){
        this.row = row;
        this.col = col;
    }
    
    @Override
    public String toString(){
        return "M[" + name + " , " + intelligence + " , " +
                strength + " , " + health + " , " + row +
                " , " + col + " ]";
    }
    
    // Decrementa en 1 la salud
    private void gotWounded(){
        if(this.health > 0)
            this.health--; 
    }
}

