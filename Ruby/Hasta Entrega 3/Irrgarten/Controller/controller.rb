require_relative '../Irrgarten/game'
require_relative '../UIText/ui_text'

module Controller
  class Controller
    def initialize(game, view)
      @game = game
      @view = view
    end

    def play
      end_of_game = false

      while !end_of_game
        # 1. Mostrar estado
        @view.show_game(@game.get_game_state)

        # 2. Pedir movimiento
        direction = @view.next_move

        # 3. Avanzar lógica del juego
        end_of_game = @game.next_step(direction)
      end

      # Mostrar estado final
      @view.show_game(@game.get_game_state)
    end
  end
end