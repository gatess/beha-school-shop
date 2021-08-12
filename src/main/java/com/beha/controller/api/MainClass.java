package com.beha.controller.api;

import java.util.ArrayList;

import java.util.List;

import com.algolia.search.DefaultSearchClient;
import com.algolia.search.SearchClient;
import com.algolia.search.SearchIndex;
import com.algolia.search.models.indexing.BrowseIndexQuery;
import com.beha.dto.GetCustomerDTO;

public class MainClass {

	 public static void main(String[] args) {
//		 SearchClient client = 
//				 DefaultSearchClient.create("318X71CBOQ", "75a49fc2015bb143c29fd58e6982ac74");
//				 SearchIndex<Contact> index = client.initIndex("deneme", Contact.class);
//				 List<Contact> contacts = new ArrayList<Contact>();
//				 Contact contact1 = new Contact("Ayşe","koçaş",1,"122","Kitap","Kitap > Sanane");
//				 Contact contact2 = new Contact("betül","ateş",3,"123","Kitap","Kitap > Ben");
//				 Contact contact3 = new Contact("betül","yıldız",5,"124","Kitap","Kitap > Ben");
//				 Contact contact4 = new Contact("gökhan","ateş",12,"125","Oyuncak","Oyuncak > zzzzzz");
//				 contacts.add(contact2);
//				 contacts.add(contact1);
//				 contacts.add(contact3);
//				 contacts.add(contact4);
//
//						// Sync version
//						//index.saveObjects(contacts, true);
//				 index.replaceAllObjects(contacts); //Replace all objects
//						// Async version
//						//index.saveObjectsAsync(contacts, true);
//	        System.out.println("Hello World!"); // Display the string.
//	        List<Contact> hits = new ArrayList<>();
	        int[] finalArray = {1,0,2,3,0,4,5,0};
	        duplicateZeros(finalArray);

	       
	    }
	 
	 public static void duplicateZeros(int[] arr) {
	        int[] finalArray = new int[arr.length];
	        int length = arr.length;
	        int b = 0;
	        for(int i = 0 ; i < length; i++){
	            if(arr[i] == 0){
	               finalArray[b]=0;
	               finalArray[b+1]=0;
	               b = b + 2;
	               length --;
	            }else{
	                finalArray[b] = arr[i];
	                b++;
	            }
	        }
	        arr = finalArray;
	    }
}
