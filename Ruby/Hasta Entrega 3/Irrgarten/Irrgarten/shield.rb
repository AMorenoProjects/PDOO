require_relative "dice"

module Irrgarten
  class Shield
    def initialize(protection, uses)
      @protection = protection
      @uses = uses
    end

    def protec
      if @uses > 0
        @uses -= 1
        @protection
      else
        0.0
      end
    end

    def discard
      Dice.discard_element(@uses)
    end

    def to_s
      "S[#{@protection}, #{@uses}]"
    end
  end
end
