package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** This class is the main entry point. */
public class MapEngine {

  private Graph graph = new Graph(); // initialize the graph

  public MapEngine() {
    // add other code here if you wan
    loadMap(); // keep this mehtod invocation
  }

  /** invoked one time only when constracting the MapEngine class. */
  private void loadMap() {

    List<String> countries = Utils.readCountries();
    List<String> adjacencies = Utils.readAdjacencies();

    for (String country : countries) {
      String[] parts = country.split(",");
      String name = parts[0];
      String continent = parts[1];
      int fuelCost = Integer.parseInt(parts[2]);
      Country c = new Country(name, continent, fuelCost);
      // add the country to the map
      graph.addCountry(c);
    }

    for (String adjacency : adjacencies) {
      String[] parts = adjacency.split(",");
      String mainCountry = parts[0];
      // add the adjacency between country1 and country2

      for (int i = 1; i < parts.length; i++) {
        String neighbours = parts[i];
        graph.addEdge(mainCountry, neighbours); // assuming graph is an instance of Graph
        // add the adjacency between country1 and neighbours
      }
    }
  }

  /** this method is invoked when the user run the command info-country. */
  public void showInfoCountry() {
    while (true) {
      MessageCli.INSERT_COUNTRY.printMessage();
      String input = Utils.scanner.nextLine();
      String formattedName = Utils.capitalizeFirstLetterOfEachWord(input);

      // Exception handling for country not found
      try {
        Country country = getCountryOrThrow(formattedName);
        String continent = country.getContinent();
        int fuelCost = country.getFuelCost();
        List<String> neighbours = new ArrayList<>(graph.getNeighbours(formattedName));

        MessageCli.COUNTRY_INFO.printMessage(
            formattedName, continent, String.valueOf(fuelCost), neighbours.toString());
        break;
      } catch (CountryNotFoundException e) {
        MessageCli.INVALID_COUNTRY.printMessage(formattedName);
      }
    }
  }

  // ** This method retrieves a country by its name or throws an exception if not found. */
  public Country getCountryOrThrow(String countryName) throws CountryNotFoundException {
    if (!graph.hasCountry(countryName)) {
      throw new CountryNotFoundException(countryName);
    }
    return graph.getCountry(countryName);
  }

  /** this method is invoked when the user run the command route. */
  public void showRoute() {

    // SOURCE LOCATION ==
    String formattedSource;
    while (true) {

      MessageCli.INSERT_SOURCE.printMessage();
      String userinputSource = Utils.scanner.nextLine();
      formattedSource = Utils.capitalizeFirstLetterOfEachWord(userinputSource);

      try {
        getCountryOrThrow(formattedSource);
        break;

      } catch (CountryNotFoundException e) {
        MessageCli.INVALID_COUNTRY.printMessage(formattedSource);
      }
    }

    // DESTINATION LOCATION ==
    String formattedDestination;
    while (true) {
      MessageCli.INSERT_DESTINATION.printMessage();
      String userinputDestination = Utils.scanner.nextLine();
      formattedDestination = Utils.capitalizeFirstLetterOfEachWord(userinputDestination);

      try {
        getCountryOrThrow(formattedDestination);
        break;

      } catch (CountryNotFoundException e) {
        MessageCli.INVALID_COUNTRY.printMessage(formattedDestination);
      }
    }
    // Checking if source and destination are the same
    if (formattedSource.equals(formattedDestination)) {
      MessageCli.NO_CROSSBORDER_TRAVEL.printMessage();
      return;
    }

    // FINDING THE FASTEST ROUTE FROMS SOURCE TO DESTINATION ==
    List<String> pathway = graph.findShortestPath(formattedSource, formattedDestination);
    if (pathway.isEmpty()) {
      MessageCli.NO_CROSSBORDER_TRAVEL.printMessage();
      return;
    }

    // Show the pathway found
    MessageCli.ROUTE_INFO.printMessage(pathway.toString());

    // Show the fuel consumption
    int totalFuelCost = 0;
    for (int i = 1; i < pathway.size() - 1; i++) {
      String countryName = pathway.get(i);
      Country country = graph.getCountry(countryName);
      totalFuelCost += country.getFuelCost();
    }
    MessageCli.FUEL_INFO.printMessage(String.valueOf(totalFuelCost));

    // Show the continents visited
    List<String> continentsOrder = new ArrayList<>();
    Map<String, Integer> continentFuelUse = new HashMap<>();

    for (int i = 0; i < pathway.size(); i++) {
      String countryName = pathway.get(i);
      Country country = graph.getCountry(countryName);
      String continent = country.getContinent();
      if (!continentsOrder.contains(continent)) {
        continentsOrder.add(continent);
        continentFuelUse.put(continent, 0); // Initialize with 0
      }

      if (i != 0 && i != pathway.size() - 1) {

        int fuelCost = country.getFuelCost();
        continentFuelUse.put(continent, continentFuelUse.getOrDefault(continent, 0) + fuelCost);
      }
    }
    List<String> continentStrings = new ArrayList<>();
    for (String continent : continentsOrder) {
      int fuel = continentFuelUse.get(continent);
      continentStrings.add(continent + " (" + fuel + ")");
    }

    // Show continents visited
    MessageCli.CONTINENT_INFO.printMessage(continentStrings.toString());

    String mostFuelContinent = "";
    int maxFuel = -1;
    for (String continent : continentsOrder) {
      int fuel = continentFuelUse.get(continent);
      if (fuel > maxFuel) {
        maxFuel = fuel;
        mostFuelContinent = continent;
      }
    }

    MessageCli.FUEL_CONTINENT_INFO.printMessage(mostFuelContinent + " (" + maxFuel + ")");
  }
}
