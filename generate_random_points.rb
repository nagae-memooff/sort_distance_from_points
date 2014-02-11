#!/usr/bin/env ruby
def random_points(count)
  points = ""
  count.to_i.times { points << "#{rand 200},#{rand 200}\n" }
  points
end

puts random_points ARGV.first
