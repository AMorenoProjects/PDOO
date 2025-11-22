require_relative 'directions'

module Irrgarten
  class Labyrinth
    # Atributos de clase
    @@block_char = 'X'
    @@empty_char = '-'
    @@monster_char = 'M'
    @@combat_char = 'C'
    @@exit_char = 'E'
    @@row = 0
    @@col = 1

    def initialize(n_row,n_col,exit_row,exit_col)
      # Atributos de instancia
      @n_row = n_row
      @n_col = n_col
      @exit_row = exit_row
      @exit_col = exit_col

      # Tablas
      @laberinto = Array.new(@n_row) { Array.new(@n_col,) }
      @monster = Array.new(@n_row) { Array.new(@n_col) }
      @players = Array.new(@n_row) { Array.new(@n_col) }

      # Inicializo el tablero

      (0...@n_row).each do |i|
        (0...@n_col).each do |j|
          @laberinto[i][j] = @@empty_char # Usando la constante
        end
      end

      # Coloco la salida del laberinto
      @laberinto[@exit_row][@exit_col] = @@exit_char
    end

    # Metodo completado
    def spread_players(players)
      players.each do |p|
        pos = random_empty_pos
        put_player_2d(-1,-1,pos[@@row],pos[@@col],p)
      end
    end

    # Devuelve true si el jugador está en la casilla de salida
    def have_a_winner
      @players[@exit_row][@exit_col] != nil
    end

    # Método to_string
    def to_s
      s = " "

      (0...@n_row).each do |i|
        (0...@n_col).each do |j|
          s += @laberinto[i][j]
        end
        s += "\n"
      end
      s
    end

    # Comprueba si la posición dada está dentro del tablero y si lo es
    # añade al monstruo al laberinto y actualiza su posición
    def add_monster(row, col, monstruo)
      if pos_ok(row, col) && empty_pos(row, col)
        @laberinto[row][col] = @@monster_char
        @monster[row][col] = monstruo
        monstruo.set_pos(row, col)
      end
    end

    # Metodo completado
    def put_player(direction, player)
      old_row = player.row
      old_col = player.col

      new_pos = dir2_pos(old_row, old_col, direction)

      monster = put_player_2d(old_row, old_col, new_pos[@@row], new_pos[@@col], player)

      monster
    end

    # Metodo completado
    def add_block(orientation, start_row, start_col, length)
      if orientation == Orientation::VERTICAL
        inc_row = 1
        inc_col = 0
      else
        inc_row = 0
        inc_col = 1
      end

      row = start_row
      col = start_col

      while pos_ok(row, col) && (length > 0)
        @laberinto[row][col] = @@block_char
        length -= 1
        row += inc_row
        col += inc_col
      end
    end

    # Metodo completado
    def valid_moves(row, col)
      output = [] # Array vacío

      # Añadimos direcciones si can_step_on es true
      # (Asumo que existe un módulo Directions::UP, ::DOWN, etc.)
      output << Directions::DOWN if can_step_on(row + 1, col)
      output << Directions::UP if can_step_on(row - 1, col)
      output << Directions::RIGHT if can_step_on(row, col + 1)
      output << Directions::LEFT if can_step_on(row, col - 1)

      output
    end

    # --- Métodos Privados ---
    private

    # Devuelve true si la posición proporcionada está dentro del laberinto
    def pos_ok(row, col)
      (row >= 0 && row < @n_row) && (col >= 0 && col < @n_col)
    end

    # Devuelve true si la posición suministrada está vacía
    def empty_pos(row, col)
      @laberinto[row][col] == @@empty_char
    end

    # Devuelve true si la posición suministrada alberga solo un monstruo
    def monster_pos(row, col)
      @laberinto[row][col] == @@monster_char
    end

    # Devuelve true si la posición suministrada es la de salida
    def exit_pos(row, col)
      row == @exit_row && col == @exit_col
    end

    # Devuelve true si la posición suministrada contiene un monstruo y un jugador
    def combat_pos(row, col)
      @laberinto[row][col] == @@combat_char
    end

    # Indica si la posición es válida y se puede pisar (vacía, monstruo o salida)
    def can_step_on(row, col)
      pos_ok(row, col) && (empty_pos(row, col) || monster_pos(row, col) || exit_pos(row, col))
    end

    # Actualiza la casilla que un jugador abandona
    def update_old_pos(row, col)
      if pos_ok(row, col)
        if combat_pos(row, col)
          @laberinto[row][col] = @@monster_char
        else
          @laberinto[row][col] = @@empty_char
        end
      end
    end

    # Calcula la nueva posición [row, col] basada en una dirección
    def dir2_pos(row, col, direction)
      new_pos = [row, col]

      case direction
      when Directions::UP
        new_pos[@@row] -= 1
      when Directions::DOWN
        new_pos[@@row] += 1
      when Directions::LEFT
        new_pos[@@col] -= 1
      when Directions::RIGHT
        new_pos[@@col] += 1
      end

      new_pos
    end

    # Genera una posición [row, col] aleatoria que esté vacía
    def random_empty_pos
      row = 0
      col = 0

      begin
        row = Dice.random_pos(@n_row)
        col = Dice.random_pos(@n_col)
      end until empty_pos(row, col)

      [row, col]
    end

    # Metodo completado
    def put_player_2d(old_row, old_col, row, col, player)
      output = nil  # Equivalente a 'output = null'

      if can_step_on(row, col)

        if pos_ok(old_row, old_col)
          p = @players[old_row][old_col]
          if p == player
            update_old_pos(old_row, old_col)
            @players[old_row][old_col] = nil
          end
        end

        is_monster_pos = monster_pos(row, col)

        if is_monster_pos
          @laberinto[row][col] = @@combat_char
          output = @monster[row][col]
        else
          number_char = player.number.to_s
          @laberinto[row][col] = number_char
        end

        @players[row][col] = player
        player.set_pos(row, col)
      end

      output
    end


  end
end