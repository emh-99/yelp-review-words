package hw6;

public class Business {
	String businessID;
	  String businessName;
	  String businessAddress;
	  String reviews;
	  int reviewCharCount; 
	  
	  public String toString() {
	    return "-------------------------------------------------------------------------------\n"
	          + "Business ID: " + businessID + "\n"
	          + "Business Name: " + businessName + "\n"
	          + "Business Address: " + businessAddress + "\n"
	          //+ "Reviews: " + reviews + "\n"
	          + "Character Count: " + reviewCharCount;
	  }
}
