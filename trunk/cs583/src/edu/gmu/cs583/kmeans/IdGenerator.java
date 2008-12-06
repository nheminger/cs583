package edu.gmu.cs583.kmeans;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class IdGenerator {

  private int upperBound;
  
  private Random randomizer;
  private Set ids = new HashSet();
  
  public IdGenerator(int upperBound) {
    this.upperBound = upperBound;
    randomizer = new Random();
  }
  
  public int getNextId() {
    if (ids.size() == upperBound) {
      ids.clear();
    }
    for (;;) {
      int id = randomizer.nextInt(upperBound);
      if (ids.contains(id)) {
        continue;
      }
      ids.add(id);
      return id;
    }
  }
}
