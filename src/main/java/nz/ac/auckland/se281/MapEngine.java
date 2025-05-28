package nz.ac.auckland.se281;

import java.util.List;

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

      try {
        Country country = getCountryOrThrow(formattedName);
        String continent = country.getContinent();
        int fuelCost = country.getFuelCost();
        List<String> neighbours = new java.util.ArrayList<>(graph.getNeighbours(formattedName));
        MessageCli.COUNTRY_INFO.printMessage(
            formattedName,
            continent,
            String.valueOf(fuelCost),
            neighbours.toString()
        );
        break;
      } catch (CountryNotFoundException e) {
        MessageCli.INVALID_COUNTRY.printMessage(formattedName);
      }
    }
  }

  public Country getCountryOrThrow(String countryName) throws CountryNotFoundException {
    if (!graph.hasCountry(countryName)) {
      throw new CountryNotFoundException(countryName);
    }
    return graph.getCountry(countryName);
  }

  /** this method is invoked when the user run the command route. */
  public void showRoute() {}
}
