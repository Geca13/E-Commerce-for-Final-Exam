package com.example.aaa.bank.entity;

public enum Month {
	
	JANUARY("January"),
	FEBRUARY("February"),
	MARCH("March"),
	APRIL("April"),
	MAY("May"),
	JUNE("June"),
	JULY("July"),
	AUGUST("August"),
	SEPTEMBER("September"),
	OCTOMBER("Octomber"),
	NOVEMBER("November"),
	DECEMBER("December");
	
	 private final String displayValue;
    
    private Month(String displayValue) {
        this.displayValue = displayValue;
    }
    
    public String getDisplayValue() {
        return displayValue;
    }

}
