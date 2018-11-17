package com.concordia.riskGame.model.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.concordia.riskGame.model.Card.Card;
import com.concordia.riskGame.model.Country.Country;

public class CheaterPlayer implements PlayerStrategy {

	@Override
	public Player reinforcePhase(Player player) {
		player.setDomination();
		player.setCurrentPhase(Player.reinforcePhase);
		player.setPhase("#### Cheater Player Reinforcement Phase");
		player.printAllCountriesOfaPlayer(player);
		for(Country country : player.getAssignedCountries()) {
			Country playerCountry = player.getSourceCountryFromString(country.getCountryName());
			playerCountry.setArmies(playerCountry.getArmies()*2);
		}
		player.setPhase("#### After Reinforcement Phase");
		return player;
	}

	@Override
	public Player attackPhase(Player player) {
		player.setDomination();
		player.setCurrentPhase(Player.reinforcePhase);
		player.setPhase("#### Cheater Player Attack Phase");
		player.printAllCountriesOfaPlayer(player);
		Iterator<Country> iterator = player.getAssignedCountries().iterator();
		while(iterator.hasNext()) {
			Country playerCountry = player.getSourceCountryFromString(iterator.next().getCountryName());
			for(Country country2 : playerCountry.getNeighbouringCountries()) {
				Country belongsToPlayer = player.getSourceCountryFromPlayerUsingString(country2.getCountryName(), player);
				if(belongsToPlayer == null) {
					Country destinationCountry = player.getSourceCountryFromString(country2.getCountryName());
					playerLosesTheCountry(playerCountry,destinationCountry);
				}
			}
		}
		player.setPhase("#### After Cheater Player Attack Phase");
		player.printAllCountriesOfaPlayer(player);
		return player;
	}
	
	/**
	 * This method transfer country from one player to another player when player losses the country 
	 * @param sourceCountryObject Source Country Object
	 * @param destinationCountryObject destination country object
	 */
	public void playerLosesTheCountry(Country sourceCountryObject, Country destinationCountryObject) {
		destinationCountryObject.getBelongsToPlayer().getAssignedCountries().remove(destinationCountryObject);
		sourceCountryObject.getBelongsToPlayer().getAssignedCountries().add(destinationCountryObject);
		if (destinationCountryObject.getBelongsToPlayer().getAssignedCountries().size() == 0) {
			playerHasLost(sourceCountryObject, destinationCountryObject);
		}
		destinationCountryObject.setBelongsToPlayer(sourceCountryObject.getBelongsToPlayer());
		int movableArmies = 1;
		if (movableArmies > 0) {
			sourceCountryObject.setArmies(sourceCountryObject.getArmies() - movableArmies);
			destinationCountryObject.setArmies(destinationCountryObject.getArmies() + movableArmies);
		}
	}
	
	/**
	 * This method do needed operations after player has lost
	 * @param sourceCountryObject source country object
	 * @param destinationCountryObject destination country object
	 */
	public void playerHasLost(Country sourceCountryObject, Country destinationCountryObject) {
		destinationCountryObject.getBelongsToPlayer().setHasLost(true);
		destinationCountryObject.getBelongsToPlayer().setEndGameForThisPlayer(true);
		List<Card> listOfDefenderCards = destinationCountryObject.getBelongsToPlayer().getCardList();
		for (Card card : listOfDefenderCards)
			sourceCountryObject.getBelongsToPlayer().getCardList().add(card);
		destinationCountryObject.getBelongsToPlayer().setCardList(new ArrayList<Card>());
	}


	@Override
	public Player forfeitPhase(Player player) {
		player.setDomination();
		player.setCurrentPhase(Player.fortificationPhase);
		player.setPhase("#### Cheater Player Fortification Phase");
		player.printAllCountriesOfaPlayer(player);
		for(Country country : player.getAssignedCountries()) {
			Country playerCountry = player.getSourceCountryFromString(country.getCountryName());
			for(Country country2 : playerCountry.getNeighbouringCountries()) {
				Country belongsToPlayer = player.getSourceCountryFromPlayerUsingString(country2.getCountryName(), player);
				if(belongsToPlayer == null) {
					playerCountry.setArmies(playerCountry.getArmies()*2);
					break;
				}
			}
			playerCountry.setArmies(playerCountry.getArmies()*2);
		}
		player.setPhase("#### After Fortification Phase");
		player.printAllCountriesOfaPlayer(player);
		return player;
	}

}