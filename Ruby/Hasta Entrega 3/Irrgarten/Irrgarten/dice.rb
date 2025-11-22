module Irrgarten
  class Dice
    @@max_uses = 5
    @@max_intelligence = 10.0
    @@max_strength = 10.0
    @@resurrect_prob = 0.3
    @@weapons_reward = 2
    @@shields_reward = 3
    @@health_reward = 5
    @@max_attack = 3
    @@max_shield = 2

    def self.random_pos(max)
      rand(max)
    end

    def self.who_starts(nplayers)
      rand(nplayers)
    end

    def self.random_intelligence
      rand * @@max_intelligence
    end

    def self.random_strength
      rand * @@max_strength
    end

    def self.resurrect_player
      rand < @@resurrect_prob
    end

    def self.weapons_reward
      rand(@@weapons_reward)
    end

    def self.shield_reward
      rand(@@shields_reward)
    end

    def self.health_reward
      rand(@@health_reward + 1)
    end

    def self.weapon_power
      rand * @@max_attack
    end

    def self.shield_power
      rand * @@max_shield
    end

    def self.uses_left
      rand(@@max_uses)
    end

    def self.intensity(competence)
      rand * competence
    end

    def self.discard_element(uses_left)
      return true if uses_left <= 0
      return false if uses_left >= @@max_uses
      prob = 1.0 - (uses_left.to_f / @@max_uses.to_f)
      rand < prob
    end
  end
end
