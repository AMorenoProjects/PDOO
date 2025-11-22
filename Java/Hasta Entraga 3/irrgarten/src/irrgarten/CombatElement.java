/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package irrgarten;

/**
 *
 * @author alejandro
 */
public abstract class CombatElement {
    private float effect;
    private int uses;
    
    // Constructor???? -> Se supone que las clases abstractas no tiene contructures ----------
    public CombatElement(float effect, int uses){
        this.effect = effect;
        this.uses = uses;
    }
    
    /*
       Este método produce el resultado delegando en el método discardElement
       del dado pasando al mismo la cantidad de usos restantes como parámetro
    */
    public boolean discard(){
        return Dice.discardElement(uses);
    }
    
    @Override
    public String toString(){
        return "W[" + effect + ", " + uses + "]";
    }
}
