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
public class Player {
    private static final int MAX_WEAPONS = 2;
    private static final int MAX_SHIELDS = 3;
    private static final int INITIAL_HEALTH = 10;
    private static final int HIT2LOSE = 3;
    
    private static int contadorJugadores = 0;
    
    private String name;
    private int number;
    private float intelligence;
    private float strength;
    private float health;
    private int row; // 0 por defecto
    private int col; // 0 por defecto
    private int consecutiveHits = 0;
    
    // Contenedores de Armas y Escudos
    private ArrayList<Weapon> weapons;
    private ArrayList<Shield> shields;

    public Player(int number, float intelligence, float strength) {
        contadorJugadores++;
        
        this.name = "Player #" + contadorJugadores;
        
        this.number = number;
        this.intelligence = intelligence;
        this.strength = strength;
        this.health = INITIAL_HEALTH;
        
        // Contenedores de Armas y Escudos
        this.weapons = new ArrayList<>();
        this.shields = new ArrayList<>();
        
    }
    
    //Imcompleto
    public void resurrect(){
        this.weapons.clear();
        this.shields.clear();
        this.health = INITIAL_HEALTH;
        this.consecutiveHits = 0;
        
    }
    
    // Getters
    public int getRow(){
        return row;
    }
    
    public int getCol(){
        return col;
    }
    
    public String getNombre(){
        return name;
    }
    
    public int getNumber(){
        return number;
    }
    
    // Setters
    public void setPos(int row, int col){
        this.row = row;
        this.col = col;
    }
    
    // Devuelve true si el jugador está muerto.
    public boolean dead(){
        return health <= 0;
    }
    
    // Método completado
    public Directions move(Directions direction, ArrayList<Directions> validMoves){
        int size = validMoves.size();
        boolean contained = validMoves.contains(direction);
        
        if((size > 0) && !contained){
            Directions firstElement = validMoves.get(0);
            return firstElement;
        }else{
            return direction;
        }
    }
    
    // Calcula la suma de la fuerza del jugador y la suma de lo aportado por sus armas
    public float attack(){
        return strength + sumWeapons();
    }
    
    // Delega su funcionalidad en el método manageHit
    public boolean defend(float receivedAttack){
        return manageHit(receivedAttack);
    }
    
    // Recibes recopensas en armaw, escudos y vida
    public void receiveReward(){
        int wReward = Dice.weaponsReward();
        int sReward = Dice.shieldReward();
        
        for(int i=1; i < wReward; i++){
            Weapon wnew = newWeapon();
            receiveWeapon(wnew);
        }
        
        for(int i=1; i < sReward; i++){
            Shield snew = newShield();
            receiveShield(snew);
        }
        
        int extraHealth = Dice.healthReward();
        
        this.health += extraHealth;
    }
    
    @Override
    public String toString(){
        return "P[" + name + ", " + number + ", INT:" + intelligence + ", STR:" + strength +
               ", HP:" + health + ", HITS:" + consecutiveHits + ", POS:(" + row + "," + col + ")]";
    }
    
    // Método completado
    private void receiveWeapon(Weapon w){
        for(int i = weapons.size() -1; i >= 0; i--){
            Weapon wi = weapons.get(i);
            if(wi.discard()){
                weapons.remove(i);
            }
        }
        
        int size = weapons.size();
        if(size < MAX_WEAPONS){
            weapons.add(w);
        }
    }
    
    // Método completado
    private void receiveShield(Shield s) {
        // Recorremos la lista al revés
        for (int i = shields.size() - 1; i >= 0; i--) {
            Shield si = shields.get(i);
            if (si.discard()) {
                shields.remove(i); 
            }
        }

        int size = shields.size();
        if (size < MAX_SHIELDS) {
            shields.add(s);
        }
    }
    
    // Genera una instancia de armas
    private Weapon newWeapon(){
        Weapon nuevaArma = new Weapon(Dice.weaponPower(),Dice.usesLeft());
        
        return nuevaArma;
    }
    
    // Genera una instancia de escudo
    private Shield newShield(){
        Shield nuevoEscudo = new Shield(Dice.shieldPower(),Dice.usesLeft());
        
        return nuevoEscudo;
    }
    
    // Suma los ataques de todas las armas
    private float sumWeapons(){
        float sumaArmas = 0.0f;
        
       for(Weapon weapon : this.weapons){
           sumaArmas += weapon.attack();
       }
       return sumaArmas;
    }
    
    // Suma las protecciones de todos los escudos
    private float sumShields(){
        float sumaEscudos = 0.0f;
        
        for(Shield shield : this.shields){
            sumaEscudos += shield.protec();
        }
        return sumaEscudos;
    }
    
    // Genera energía defensiva con la inteligencia y sumShields
    private float defensiveEnergy(){
        return intelligence + sumShields();
    }
    
   
    // Maneja los golpes                                
    private boolean manageHit(float receivedAttack){
        float defense = defensiveEnergy();
        boolean lose;
        
        if(defense < receivedAttack){
            gotWounded();
            incConsecutiveHits();
        } else{
            resetHits();
        }
        
        if((consecutiveHits == HIT2LOSE) || dead()){
            resetHits();
            lose = true;
        }else{
            lose = false;
        }
        
        return lose;
    }
    
    // Pone los impactos consecutivos a 0
    private void resetHits(){
        this.consecutiveHits = 0;
    }
    
    // Decrementa en 1 la salud
    private void gotWounded(){
        if(this.health > 0)
            this.health--;
    }
    
    // Incrementa en 1 el valor de los impactos consecutivos
    private void incConsecutiveHits(){
        this.consecutiveHits++;
    }
     
 }
