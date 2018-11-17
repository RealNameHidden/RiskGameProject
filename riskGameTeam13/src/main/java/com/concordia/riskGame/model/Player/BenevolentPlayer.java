package com.concordia.riskGame.model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.concordia.riskGame.model.Continent.Continent;
import com.concordia.riskGame.model.Country.Country;

public class BenevolentPlayer implements PlayerStrategy {

	@Override
	public Player reinforcePhase(Player player) {
		player.setDomination();
		player.setCurrentPhase(Player.reinforcePhase);
		int armiesContControl = 0;
		int assignedArmies = 0;
		int armiesToBeGiven = 0;
		player.setPhase("#### Benevolent Player Reinforcement Phase");
		List<Continent> currcontControlList = new ArrayList();
		 currcontControlList = player.contienentControlList(player);
		if(currcontControlList!=null)
		{
			for(Continent cont : currcontControlList)
			{
				player.setPhase(" ##### continent name is ###### "+cont.getContinentName()  +" and     control value is "+cont.getContinentControlValue() );
				armiesContControl = armiesContControl + cont.getContinentControlValue();
			}
		}
		player.setPhase("#### The armies to be assigned from control value of continent is ###### : "+armiesContControl);
		assignedArmies = assignedArmies + armiesContControl;
		player.setPhase("#### The total number of armies to be reinforced are  #### :" + assignedArmies);
		Country weakestCountry = getWeakestCountry(player);
		weakestCountry.setArmies(weakestCountry.getArmies()+assignedArmies);
		player.setPhase("######### Player army count after reinforcment  ####### ");
		player.setPhase("######## Player Name ########### : " + player.getName());
		player.printAllCountriesOfaPlayer(player);
		return player;
	}

	/**
	 * This method is to get the strongest country of a player
	 * @param player
	 * @return country with most number of armies
	 */
	private Country getWeakestCountry(Player player) {
		List<Country> playerOwnedCountries = player.getAssignedCountries();
		Country weakestCountry = null;
		int armyCount = 9999;
		for(Country country : playerOwnedCountries) {
			if(country.getArmies()<armyCount) {
				armyCount = country.getArmies();
				weakestCountry = country;
			}
		}
		return weakestCountry;
	}
	@Override
	public Player attackPhase(Player player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Player forfeitPhase(Player player) {
		player.setCurrentPhase(Player.fortificationPhase);
		player.setDomination();
		player.setPhase("#### Benevolent Player Forfeit Phase");
		player.setPhase("#### Before Fortification ####");
		player.printAllCountriesOfaPlayer(player);
		boolean fortificationDone = false;
		List<Country> sortedCountryList = getSortedCountryListBasedOnDescArmy(player);
		Country sourceCountry = null;
		Country destinationCountry = null;
		int countriesSize = sortedCountryList.size();
		while(!fortificationDone && countriesSize>0 ) {
			destinationCountry = sortedCountryList.get(countriesSize);
			for(Country country2 : destinationCountry.getNeighbouringCountries()) {
				sourceCountry = player.getSourceCountryFromPlayerUsingString(country2.getCountryName(), player);
				if(sourceCountry!=null && sourceCountry.getArmies()>3) {
					fortificationDone = true;
					break;
				}
			}
			countriesSize--;
		}
		player.setPhase("					###########    Source country      	     ###############       : "
				+ sourceCountry);
		player.setPhase(
				"					###########  Destination Country   	 ###############       : " + destinationCountry);
		player.setPhase("					############   Armies to be moved    ###############      : "
				+ (sourceCountry.getArmies()-2));
		destinationCountry.setArmies(destinationCountry.getArmies() + sourceCountry.getArmies()-2);
		sourceCountry.setArmies(sourceCountry.getArmies()-2);
		player.setPhase("#### After Fortification ####");
		player.printAllCountriesOfaPlayer(player);
		return player;
	}

	/**
	 * This method is to sort the country list of a player based on the army count of the countries in descending order
	 * @param player the player object is passed
	 * @return a sorted list of countries
	 */
	public List<Country> getSortedCountryListBasedOnDescArmy(Player player) {
		List<Integer> armiesList = new ArrayList<>();
		for(Country country : player.getAssignedCountries()) {
			Country playerCountry = player.getSourceCountryFromString(country.getCountryName());
			armiesList.add(playerCountry.getArmies());
		}
		List<Country> sortedCountryList = new ArrayList<>();
		Collections.sort(armiesList);
		Collections.reverse(armiesList);
		for(Integer army : armiesList) {
			for(Country country : player.getAssignedCountries()) {
			Country playerCountry = player.getSourceCountryFromString(country.getCountryName());
				if( playerCountry.getArmies() == army  && !sortedCountryList.contains(playerCountry) ) {
				sortedCountryList.add(playerCountry);
				break;
				}
			}
		}
		return sortedCountryList;
	}
}