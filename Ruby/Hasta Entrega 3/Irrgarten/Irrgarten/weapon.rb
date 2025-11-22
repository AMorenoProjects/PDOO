require_relative "dice"

module Irrgarten
  class Weapon
    def initialize(power, uses)
      @power = power
      @uses = uses
    end

    def attack
      if @uses > 0
        @uses -= 1
        @power
      else
        0.0
      end
    end

    def discard
      Dice.discard_element(@uses)
    end

    def to_s
      "W[#{@power}, #{@uses}]"
    end
  end
end
