
require_relative 'dice'
require_relative 'directions'
require_relative 'orientation'
require_relative 'game_character'
require_relative 'weapon'
require_relative 'shield'
require_relative 'game_state'
require_relative 'player'
require_relative 'monster'
require_relative 'labyrinth'
require_relative 'game'
require_relative '../UIText/ui_text'
require_relative '../Controller/controller'

class Main
  def self.main
    # 1. Crear la VISTA (Desde el módulo UIText)
    view = UIText::TextUI.new

    # 2. Crear el JUEGO (Desde el módulo Irrgarten)
    # Pasamos 0 para indicar que empieza el jugador 0
    game = Irrgarten::Game.new(0)

    # 3. Crear el CONTROLADOR (Desde el módulo Controller)
    controller = Controller::Controller.new(game, view)

    # 4. Iniciar la partida
    controller.play
  end
end

# Ejecución del programa
Main.main