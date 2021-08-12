package com.beha.controller.api;

import java.util.ArrayList;
import java.util.List;

import com.algolia.search.DefaultSearchClient;
import com.algolia.search.SearchClient;
import com.algolia.search.SearchIndex;
import com.algolia.search.models.indexing.BrowseIndexQuery;

public class NewVersion {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SearchClient client = 
				DefaultSearchClient.create("318X71CBOQ", "75a49fc2015bb143c29fd58e6982ac74");

				SearchIndex<Contact> index = client.initIndex("deneme", Contact.class);
				
				List<Contact> hits = new ArrayList<>();

				for (Contact hit : index.browseObjects(new BrowseIndexQuery())) {
					  hits.add(hit);
				}
				System.out.println(hits);
	}

}
