module Irrgarten
  class Monster
    @@initial_health = 5
    @@unset_pos = -1

    def initialize(name,intelligence,strength)
      @name = name
      @intelligence = intelligence
      @strength = strength
      @health = @@initial_health
      @row = @@unset_pos
      @col = @@unset_pos
    end

    # Devuelve true si el monstruo está muerto
    def dead?
      @health <= 0
    end

    # Devuelve la cantidad de daño que hace el mosntruo
    def attack
      Dice.intensity(@strength)
    end

    # Metodo completado
    def defend(received_attack)
      is_dead = dead

      unless is_dead
        defensive_energy = Dice.intensity(@intelligence)

        if defensive_energy < received_attack
          got_wounded
          is_dead = dead
        end
      end

      is_dead
    end

    # Setter
    def set_pos(row,col)
      @row = row
      @col = col
    end

    #Sobrecarga del método to_string
    def to_s
      "M[#{@name}, #{@intelligence}, #{@strength}, #{@health}, #{@row}, #{@col}]"
    end

    private
    #Decrementa la salud en 1
    def got_wounded
      if @health > 0
        @health -= 1
      end
    end

  end
end

