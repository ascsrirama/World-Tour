package nz.ac.auckland.se281;

import java.util.*;
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
    // adjacencies.get(country2).add(country1); 
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

  // BFS method to find the shortest path between two countries
  public List<String> findShortestPath(String source, String destination) {
    Queue<String> queue = new LinkedList<>();
    Set<String> visited = new HashSet<>();
    Map<String, String> parent = new HashMap<>();

    // Start BFS from the source country
    queue.add(source);
    visited.add(source);
    while (!queue.isEmpty()) {
      String current = queue.poll();

      // If we reached the destination, reconstruct the path
      if (current.equals(destination)) {
        List<String> path = new ArrayList<>();
        String country = destination;
        while (country != null) {
          path.add(0, country);
          country = parent.get(country);
        }
        return path;
      }

      // Explore neighbors
      for (String neighbor : getNeighbours(current)) {
        if (!visited.contains(neighbor)) {
          visited.add(neighbor);
          parent.put(neighbor, current);
          queue.add(neighbor);
        }
      }
    }

    // If we exhaust the queue without finding the destination
    return new ArrayList<>(); // return an empty list if no path found
  }
}
