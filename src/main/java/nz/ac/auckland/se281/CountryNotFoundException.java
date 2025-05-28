package nz.ac.auckland.se281;

public class CountryNotFoundException extends Exception {
  public CountryNotFoundException(String countryName) {
    super(countryName);
  }
}
