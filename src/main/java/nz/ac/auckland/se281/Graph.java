package nz.ac.auckland.se281;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class Graph {

  private HashMap<String, LinkedHashSet<String>> adjacencies;
  private HashMap<String, Country> countries;

  public Graph() {
    adjacencies = new HashMap<>();
    countries = new HashMap<>();
  }

  public void addNode(String country) {
    adjacencies.putIfAbsent(country, new LinkedHashSet<>());
  }

  public void addCountry(Country country) {
    String name = country.getName();
    countries.put(name, country);
    adjacencies.putIfAbsent(name, new LinkedHashSet<>());
  }

  public void addEdge(String country1, String country2) {
    adjacencies.get(country1).add(country2);
  }

  public Set<String> getNeighbours(String country) {
    return adjacencies.getOrDefault(country, new LinkedHashSet<>());
  }

  public Country getCountry(String name) {
    return countries.get(name);
  }

  public boolean hasCountry(String name) {
    return countries.containsKey(name);
  }
}
