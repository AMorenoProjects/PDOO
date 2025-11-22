require 'io/console'
require_relative '../Irrgarten/directions'

module UIText
  class TextUI

    def show_game(game_state)
      puts "\n" + "=" * 20
      # Mostrar el laberinto
      puts game_state.labyrinth

      # Mostrar información básica del turno
      puts "Turno del Jugador: #{game_state.current_player}"

      # Mostrar logs si los hay
      unless game_state.log.strip.empty?
        puts "Eventos:\n#{game_state.log}"
      end

      # Mostrar mensaje de victoria
      if game_state.winner
        puts "¡VICTORIA! El jugador #{game_state.current_player} ha ganado."
      end
    end

    def next_move
      print "\n¿Movimiento? (w:Arriba, s:Abajo, a:Izquierda, d:Derecha): "
      input = gets.chomp.downcase

      case input
      when 'w'
        puts "MOVER: ARRIBA"
        return Irrgarten::Directions::UP      # <--- AÑADIR Irrgarten::
      when 's'
        puts "MOVER: ABAJO"
        return Irrgarten::Directions::DOWN    # <--- AÑADIR Irrgarten::
      when 'd'
        puts "MOVER: DERECHA"
        return Irrgarten::Directions::RIGHT   # <--- AÑADIR Irrgarten::
      when 'a'
        puts "MOVER: IZQUIERDA"
        return Irrgarten::Directions::LEFT    # <--- AÑADIR Irrgarten::
      else
        puts "Dirección desconocida. Se mantiene posición."
        return Irrgarten::Directions::DOWN    # <--- AÑADIR Irrgarten::
      end
    end

  end
end