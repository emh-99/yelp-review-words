package hw6;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.util.Comparator;
import java.util.Collections;
import java.text.DecimalFormat;

public class YelpAnalysis {

	public static void main(String[] args) throws IOException {
		
		InputStream is = new FileInputStream ("yelpDatasetParsed_full.txt"); //read the file using a buffered reader
		Reader r = new InputStreamReader (is);
		BufferedReader br = new BufferedReader (r);

		int savePositionForName; //int variables to keep track of position in the line that is read
		int savePositionForAddress;
		int savePositionForReview;
		
		List<Business> businessList = new ArrayList <Business>(); //create arraylist to keep track of all the Businesses
		Map <String,Integer> corpusDFCount = new HashMap<>(); //create hashmap to keep track of number of documents a certain word appears in

		while (true) //creating Business objects and adding them to businessList
		{ 
			Business Business1 = new Business();
			String line = ""; //creating String variables to hold information for fields
			String id = "";
			String name = "";
			String address = "";
			String review = "";
			line = br.readLine (); //read one line
			
			if(line ==null) //break loop if end of file is reached
				break;
	
			for (int i = 1 ;; i++)
			{
				char oneID = line.charAt(i); //get business ID
				if (line.charAt(i) == ',')
				{
					savePositionForName = i+2; //at the end of the business ID save position and break
					break;
				}
				id+= oneID;
			}
			
			for (int i = savePositionForName ;; i++) //get business name
			{
				char oneName = line.charAt(i);
				if (line.charAt(i) == ',')
				{
					savePositionForAddress = i+2; //at the end of the business name save position and break
					break;
				}
				name+= oneName;
			}
			
			for (int i = savePositionForAddress ;; i++) //get business address
			{
				char oneAddress = line.charAt(i); 
				if (line.charAt(i) == ',') 
				{
					savePositionForReview = i+2; //at the end of the business address save position and break
					break;
				}
				address+= oneAddress;
			}
			
			review = line.substring(savePositionForReview, line.length()-1); 
			
			Business1.reviews = review; //assign respective values to fields
			Business1.businessID = id;
			Business1.businessName = name;
			Business1.businessAddress = address;
			Business1.reviewCharCount = review.length();
			
			businessList.add(Business1); //append Business to the list
		}

		
		for(int i=0; i < businessList.size() ;i++) //for each word, count the number of documents it appears in
		{
			String[] arrOfReview = businessList.get(i).reviews.split(" ",0); //create an array of strings; each entry in the array is a word
			Set<String> set = new HashSet<String>(); //create a set of the words 
		
			for(int j = 0; j < arrOfReview.length; j++)
			{
			  set.add(arrOfReview[j]); //add words from array to the set (each word is added only if it hasn't appeared before)
			}
	
			for(String a : set) //for each word in the set
			{
				if(corpusDFCount.containsKey(a))
				{
					corpusDFCount.put(a, corpusDFCount.get(a)+1); //if it is already in corpusDFCount, increment
				}
				
				else
				{
					corpusDFCount.put(a, 1); //if it isn't already, add it
				}
			}
		}	
		
		Collections.sort(businessList, new Comparator<Business>() //sort businessList by descending order of review character count
		{
			public int compare ( Business b1 , Business b2 ) 
			{
				return b2.reviewCharCount - b1.reviewCharCount;
				
			}
		});

		DecimalFormat df2 = new DecimalFormat(".##"); //round tfidf scores to two decimal places
		
		for (int i=0; i<10; i++) //for the 10 businesses with the most reviews 
		{
			Map<String,Double> tfidfScoreMap = new HashMap<>(); //create hashmap to keep track of tfidf scores
			String[] arrOfReview = businessList.get(i).reviews.split(" "); //create array; each entry is a word of the review
			
			for (String a: arrOfReview)
			{
				if(tfidfScoreMap.containsKey(a)) //if hashmap already has the word, adjust the tfidfscore
				{
					tfidfScoreMap.put(a,  Double.valueOf(df2.format(((tfidfScoreMap.get(a)*corpusDFCount.get(a))+1)/corpusDFCount.get(a)) )); 
				}
				else //if it doesn't, add it as an entry
				{
					tfidfScoreMap.put(a, 1.0/ corpusDFCount.get(a));
				}
			}
				
			Iterator <Map.Entry<String, Double>> itr = tfidfScoreMap.entrySet().iterator(); //create iterator
			while (itr.hasNext()) //go through tfidfScoreMap
			{
				Map.Entry<String, Double> entry = itr.next(); 
				String key = entry.getKey();
				if (corpusDFCount.get(key) < 5) //if the word appears in less than 5 documents, remove the word 
				{
					itr.remove();
				}
			}	
		
			List <Map.Entry<String,Double>> tfidfScoreList = new ArrayList<>(tfidfScoreMap.entrySet()); //create list of tfidfscoremap entries
			Collections.sort(tfidfScoreList, (o1, o2) -> o2.getValue().compareTo(o1.getValue())); //sort them based on descending order of tfidf score
			
			
			System.out.println(businessList.get(i)); //print the info of the business
			for (int j = 0; j <30; j++)
			{
				System.out.print("(" + tfidfScoreList.get(j) + ") "); //print its top 30 words
			}
			System.out.println();
		}
	}

}
