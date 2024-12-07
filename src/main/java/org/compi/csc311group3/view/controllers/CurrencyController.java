package org.compi.csc311group3.view.controllers;

import java.util.ArrayList;
import java.util.Locale;

public class CurrencyController {

    private static String currencyType = "USD"; //default

    private double multiplier = 1; //default multiplier

    //exchange rates
    double EurExchangeRate = 1.06; //this is how much one euro is worth in USD
    double UsdExchangeRate = 1.00; //this is how much one USD is worth in USD

    public CurrencyController() {
        this.currencyType = currencyType;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public double convertCurrency(double value) {
        if(currencyType.equals("EUR"))
        {
            multiplier = EurExchangeRate;
            return value * multiplier;
        }
        if(currencyType.equals("USD"))
        {
            multiplier = UsdExchangeRate;
            return value * multiplier;
        }

        return value;
    }

    public String convertCurrencyWithFormat(double value){
        String formatedWithSymbol = "";
        if(currencyType.equals("EUR"))
        {
            multiplier = EurExchangeRate;
            double convertedValue =  value * multiplier;
            String formatedDecimalPlaces = String.format("%.2f", convertedValue);
            formatedWithSymbol = "€" + formatedDecimalPlaces;

        }

        if(currencyType.equals("USD"))
        {
            multiplier = UsdExchangeRate;
            double convertedValue =  value * multiplier;
            String formatedDecimalPlaces = String.format("%.2f", convertedValue);
            formatedWithSymbol = "$" + formatedDecimalPlaces;

        }

        return formatedWithSymbol;
    }

}
