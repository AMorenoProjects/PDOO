/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package irrgarten;

/**
 *
 * @author alejandro
 */
public class Weapon {
    private float power;
    private int uses;
    
    // Constructor público
    public Weapon(float power,int uses){
        this.power = power;
        this.uses = uses;
    }
    
    // Método que devuelve el ataque del arma y reduce 1 a sus usos
    // En caso de que los usos sean 0 devuelve cero.
    public float attack(){
        float resultado = 0.0f;
        
        if(uses > 0){
            uses--;
            resultado = power;
        }
        
        return resultado;
    }
    
    // Método que devulve un string con los usos y ataque del arma
    @Override
    public String toString(){
        return "W[" + power + ", " + uses + "]";
    }
    
    /*
       Este método produce el resultado delegando en el método discardElement
       del dado pasando al mismo la cantidad de usos restantes como parámetro
    */
    public boolean discard(){
        return Dice.discardElement(uses);
    }
}
