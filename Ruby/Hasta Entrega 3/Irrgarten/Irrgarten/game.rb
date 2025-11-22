module Irrgarten
  class Game
    # Atributos de clase
    @@max_rounds = 10
    @@max_players = 4
    @@max_monsters = 10
    @@rows = 10
    @@cols = 10

    # Atributos de instancia
    def initialize(current_player_index)
      @current_player_index = current_player_index
      @log = " "

      # Inicializo los players
      @players = []
      (0...@@max_players).each do |i|
        new_player = Player.new(i, Dice.random_intelligence, Dice.random_strength)
        @players << new_player
      end

      # Inicializo los monstruos
      @monsters = []

      @rows = @@rows
      @cols = @@cols
      @exitR = Dice.random_pos(@rows)
      @exitC = Dice.random_pos(@cols)

      # Inicializo el laberinto
      @labyrinth = Labyrinth.new(@rows, @cols, @exitR, @exitC)

      # Llamo al método que configura el laberinto
      configure_labyrinth

      @labyrinth.spread_players(@players)
    end

    # Devuelve true si hay un ganador
    def finished
      @labyrinth.have_a_winner
    end

    # Metodo completado
    def next_step(preferred_direction)
      @log = ""
      current_player = @players[@current_player_index]
      dead = current_player.dead?

      if !dead
        direction = actual_direction(preferred_direction)

        if direction != preferred_direction
          log_player_no_orders
        end

        monster = @labyrinth.put_player(direction, current_player)

        if monster.nil? # .nil? es el '== null' de Ruby
          log_no_monster
        else
          winner = combat(monster)
          manage_reward(winner)
        end
      else
        manage_resurrection
      end

      end_game = finished

      if !end_game
        next_player
      end

      end_game
    end

    # Devuelve toda la información actual del estado del juego
    def get_game_state
      laberinto = @labyrinth.to_s

      players_string = ""
      @players.each do |player|
        players_string += "#{player.to_s}\n"
      end

      monster_string = " "
      @monsters.each do |monster|
        monster_string += "#{monster.to_s}\n"
      end

      GameState.new(laberinto, players_string, monster_string, @current_player_index, finished, @log)
    end

    # Configura el laberinto añadiendo bloques de obstáculos y monstruos.
    private def configure_labyrinth
      # Añado bloques
      @labyrinth.add_block(Orientation::HORIZONTAL, 0, 0, @@cols)        # Pared superior
      @labyrinth.add_block(Orientation::HORIZONTAL, @@rows - 1, 0, @@cols)  # Pared inferior
      @labyrinth.add_block(Orientation::VERTICAL, 0, 0, @@rows)        # Pared izquierda
      @labyrinth.add_block(Orientation::VERTICAL, 0, @@cols - 1, @@rows)  # Pared derecha

      # Añado monstruos
      @@max_monsters.times do |i|
        monster = Monster.new("Monster ##{i}", Dice.random_intelligence, Dice.random_strength)

        r = Dice.random_pos(@@rows)
        c = Dice.random_pos(@@cols)

        @labyrinth.add_monster(r, c, monster)

        @monsters << monster
      end
    end

    # Actualiza los dos atributos que indican el jugador (current*) con el turno pasando al siguiente jugador.
    private def next_player
      # Uso el módulo para que sea cíclico
      @current_player_index = (@current_player_index + 1) % @players.size
    end

    # Metodo completado
    private def actual_direction(preferred_direction)
      current_player = @players[@current_player_index]

      # (Asumimos que Player tiene attr_reader :row y :col)
      current_row = current_player.row
      current_col = current_player.col

      valid_moves = @labyrinth.valid_moves(current_row, current_col)

      output = current_player.move(preferred_direction, valid_moves)

      output
    end

    # Metodo completado
    private def combat(monster)
      rounds = 0
      winner = GameCharacter::PLAYER
      current_player = @players[@current_player_index]

      player_attack = current_player.attack
      lose = monster.defend(player_attack)

      # Usamos la variable de clase @@max_rounds
      while !lose && (rounds < @@max_rounds)
        winner = GameCharacter::MONSTER
        rounds += 1

        monster_attack = monster.attack
        lose = current_player.defend(monster_attack)

        if !lose
          player_attack = current_player.attack
          winner = GameCharacter::PLAYER
          lose = monster.defend(player_attack)
        end
      end

      log_rounds(rounds, @@max_rounds)

      winner
    end

    # Metodo completado
    private def manage_reward(winner)
      if winner == GameCharacter::PLAYER
        @players[@current_player_index].receive_reward
        log_player_won
      else
        log_monster_won
      end
    end

    # Metodo completado
    private def manage_resurrection
      resurrect = Dice.resurrect_player

      if resurrect
        @players[@current_player_index].resurrect
        log_resurrected
      else
        log_player_skip_turn
      end
    end

    # Añade al final del atributo log (concatena cadena al final) el mensaje
    # indicando que el jugador ha ganado el combate.
    private def log_player_won
      @log += " ¡Victoria! \n"
    end

    # Añade al final del atributo log (concatena cadena al final) el mensaje
    # indicando que el monstruo ha ganado el combate.
    private def log_monster_won
      @log += " ¡Monster Won! \n"
    end

    # Añade al final del atributo log (concatena cadena al final) el mensaje
    # indicando que el jugador ha resucitado
    private def log_resurrected
      @log += " ¡El jugador ha resucitado! \n"
    end

    # Añade al final del atributo log (concatena cadena al final) el mensaje
    # indicando que el jugador ha perdido el turno por estar muerto.
    private def log_player_skip_turn
      @log += " ¡El jugador perdió el turno! \n"
    end

    # Añade al final del atributo log (concatena cadena al final) el mensaje
    # indicando que el jugador no ha seguido las instrucciones del jugador humano (no fue posible).
    private def log_player_no_orders
      @log += " ¡No se puede realizar esa acción! \n"
    end

    # Añade al final del atributo log (concatena cadena al final) el mensaje
    # indicando que el jugador se ha movido a una celda vacía o no le ha sido posible moverse.
    private def log_no_monster
      @log += " ¡Se ha movido a una casilla vacia, no se puede realizar dicha acción \n"
    end

    # Añade al final del atributo log (concatena cadena al final) el
    # mensaje que se han producido rounds de max rondas de combate. También añade el indicador de
    # nueva línea al final.
    private def log_rounds(rounds, max)
      # En Ruby es más idiomático usar interpolación de strings
      @log += " ¡Se han producido #{rounds} de #{max} rondas! \n"
    end

  end
end