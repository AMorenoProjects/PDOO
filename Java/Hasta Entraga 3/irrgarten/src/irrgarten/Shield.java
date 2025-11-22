/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package irrgarten;

/**
 *
 * @author alejandro
 */
public class Shield {
    private float protection;
    private int uses;
    
    // Constructor por defecto
    public Shield(float protection,int uses){
        this.protection = protection;
        this.uses = uses;
    }
    
    // Método que devuelve la defensa del arma y reduce 1 a sus usos
    // En caso de que los usos sean 0 devuelve cero.
    public float protec(){
        float resultado = 0.0f;
        
        if(uses > 0){
            uses--;
            resultado = protection;
        }
        
        return resultado;
    }
    
    // Método que devulve un string coné los usos y defensa del arma
    @Override
    public String toString(){
        return "W[" + protection + ", " + uses + "]";
    }
    
    /*
      Este método produce el resultado delegando en el método discardElement
      del dado pasando al mismo la cantidad de usos restantes como parámetr
    */
    public boolean discard(){
        return Dice.discardElement(uses);
    }
}
