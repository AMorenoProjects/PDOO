# encoding: utf-8

module Irrgarten
  class Player
    # Constantes
    @@max_weapon = 2
    @@max_shield = 3
    @@initial_health = 10
    @@hit2lose = 3

    # Variable de clase
    @@contador_jugadores = 0

    # Constructor
    def initialize(number, intelligence, strength)
      @@contador_jugadores += 1

      @name = "Player ##{@@contador_jugadores}"

      @number = number
      @intelligence = intelligence
      @strength = strength
      @health = @@initial_health
      @row = 0
      @col = 0

      @consecutive_hits = 0

      @weapons = []
      @shields = []
    end

    # Reinicia armas, escudos, salud e impactos
    def resurrect
      @weapons.clear
      @shields.clear
      @health = @@initial_health
      @consecutive_hits = 0
    end

    # Getters
    attr_reader :row, :col, :name, :number

    def set_pos(row, col)
      @row = row
      @col = col
    end

    # Devuelve true si el jugador está muerto
    def dead?
      @health <= 0
    end

    # Metodo completado
    def move(direction, valid_moves)
      size = valid_moves.size
      contained = valid_moves.include?(direction)

      if size.positive? && !contained
        valid_moves.first
      else
        direction
      end
    end

    # Calcula la suma de la fuerza del jugador y la suma de lo aportado por sus armas
    def attack
      @strength + sum_weapons
    end

    # Delega en manage_hit
    def defend(received_attack)
      manage_hit(received_attack)
    end

    # Metodo completado
    def receive_reward
      w_reward = Dice.weapons_reward
      s_reward = Dice.shield_reward

      (w_reward - 1).times do
        wnew = new_weapon
        receive_weapon(wnew)
      end

      (s_reward - 1).times do
        snew = new_shield
        receive_shield(snew)
      end

      extra_health = Dice.health_reward

      @health += extra_health
    end

    # Metodo para imprimir la información
    def to_s
      "P[#{@name}, #{@number}, INT:#{@intelligence}, STR:#{@strength}, " +
        "HP:#{@health}, HITS:#{@consecutive_hits}, POS:(#{@row},#{@col})]"
    end

    # Metodo completado
    def receive_weapon(w)

      @weapons.delete_if do |weapon|
        weapon.discard
      end

      if @weapons.size < @@max_weapon
        @weapons << w
      end
    end

    # Metodo completado
    def receive_shield(s)
      @shields.delete_if do |shield|
        shield.discard
      end

      if @shields.size < @@max_shield
        @shields << s
      end
    end

    # Genera una instancia de armas
    def new_weapon
      Weapon.new(Dice.weapon_power,Dice.uses_left)
    end

    # Genera una instancia de escudo
    def new_shield
      Shield.new(Dice.shield_power, Dice.uses_left)
    end

    # Suma los ataques de todas las armas
    def sum_weapons
      total_damage = 0.0
      # Por cada elemento del array w toma su valor
      @weapons.each do | w|
        total_damage += w.attack
      end
      total_damage
    end

    # Suma las protecciones de todos los escudos
    def sum_shields
      total_protection = 0.0
      @shields.each do |s|
        total_protection += s.protect # (En Java tenías 'protec', el PDF dice 'protect')
      end
      total_protection
    end

    # Genera energía defensiva
    def defensive_energy
      @intelligence + sum_shields
    end

    # Metodo completado
    def manage_hit(received_attack)
      defense = defensive_energy

      if defense < received_attack
        got_wounded
        inc_consecutive_hits
      else
        reset_hits
      end

      if (@consecutive_hits == @@hit2lose) || dead
        reset_hits
        lose = true
      else
        lose = false
      end

      lose
    end

    # Pone los impactos consecutivos a 0
    def reset_hits
      @consecutive_hits = 0
    end

    # Decrementa en 1 la salud
    def got_wounded
      if @health > 0
        @health -= 1
      end
    end

    # Incrementa en 1 los impactos consecutivos
    def inc_consecutive_hits
      @consecutive_hits += 1
    end

  end
end