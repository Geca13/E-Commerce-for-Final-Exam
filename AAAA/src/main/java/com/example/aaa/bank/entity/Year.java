package com.example.aaa.bank.entity;

public enum Year {
	
	PRVA("2021"),
	VTORA("2022"),
	TRETA("2023"),
	CHETVRTA("2024"),
	PETTA("2025"),
	SHESTA("2026"),
	SEDMA("2027"),
	OSMA("2028"),
	DEVETA("2029"),
	DESETA("2030");
	
	
	private final String displayValue;
    
    private Year(String displayValue) {
        this.displayValue = displayValue;
    }
    
    public String getDisplayValue() {
        return displayValue;
    }
	
}
