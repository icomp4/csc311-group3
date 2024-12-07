package org.compi.csc311group3.view.controllers;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        double returnValue = 0;
        if(currencyType.equals("EUR"))
        {
            multiplier = EurExchangeRate;
            returnValue = value * multiplier;
        }
        if(currencyType.equals("USD"))
        {
            multiplier = UsdExchangeRate;
            returnValue = value * multiplier;
        }

        /*BigDecimal bd = new BigDecimal(returnValue);
        bd = bd.setScale(2, RoundingMode.HALF_UP); //two decimal places*/
        String formatedDecimalPlaces = String.format("%.2f", returnValue);
        System.out.println("formated without symbol: " + formatedDecimalPlaces); //this prints value with two decimal places
        return Double.parseDouble(formatedDecimalPlaces); //convert string to double and return value.  Will only display with one decimal place since doubles are not formatted.
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

    public double convertToUSD(double value) {
        double returnValue  = 0;
        if(currencyType.equals("EUR")){
            multiplier = EurExchangeRate;
            double convertedValue = value / multiplier;
            returnValue = convertedValue;
        }
        if(currencyType.equals("USD")){
            multiplier = UsdExchangeRate;
            double convertedValue = value / multiplier;
            returnValue = convertedValue;
        }


        BigDecimal bd = new BigDecimal(returnValue);
        bd = bd.setScale(2, RoundingMode.HALF_UP); //two decimal places
        return bd.doubleValue();
    }

}
