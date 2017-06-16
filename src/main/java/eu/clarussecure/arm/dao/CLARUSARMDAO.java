/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.clarussecure.arm.dao;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UpdateOptions;
import static com.mongodb.client.model.Updates.*;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.model.Sorts;

import org.bson.Document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.clarussecure.proxy.access.CLARUSUserOperations;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author diegorivera
 */
public class CLARUSARMDAO {
	// Singleton implementation
	private static CLARUSARMDAO instance = null;
	private Gson g;
	private MongoDatabase db;
	private MongoClient mongoClient;
	private int instancesNumber;

	private CLARUSARMDAO(){
		// Create the GsonBuilder Object
		this.g = new GsonBuilder().setPrettyPrinting().create();
		// Create a new client connecting to "localhost" on port 
		this.mongoClient = new MongoClient("localhost", 27017);

		// Get the database (will be created if not present)
		this.db = mongoClient.getDatabase("CLARUS");

		this.instancesNumber++;
	}

	public void deleteInstance(){
		this.instancesNumber--;

		if(this.instancesNumber <= 0){
			this.mongoClient.close();
			CLARUSARMDAO.instance = null;
		}
	}

	public static CLARUSARMDAO getInstance(){
		if (CLARUSARMDAO.instance == null)
			CLARUSARMDAO.instance = new CLARUSARMDAO();
		return CLARUSARMDAO.instance;
	}

	/*
		// Get the collection of BSON documents that contain the users
		MongoCollection<Document> collection = db.getCollection("users");
	*/

	public boolean setRights(String username, String dataspace, String rights) {
		// Get the collection of BSON documents that contain the usersrights
		MongoCollection<Document> collection = db.getCollection("userrights");
		
		// Prepare the document for the update
		Document doc = new Document("username", username);
		doc.append("dataspace", dataspace);
		doc.append("rights", rights);
		
		// Update the entry or insert one if needed
		long modified = collection.replaceOne(and(eq("username", username), eq("dataspace", dataspace)), doc, new UpdateOptions().upsert(true)).getModifiedCount();
		
		return modified > 0;
	}
	
	public String listUsers(){
		// Get the List of the users
		CLARUSUserOperations ops = CLARUSUserOperations.getInstance();
		Set<String> users = ops.listUsers();
		
		// Format the final String
		String ret = "";
		for(String u : users){
			ret += u + "\n";
		}
		return ret;
	}
	
	public String showRights(String username){
		// Get the collection of BSON documents that contain the usersrights
		MongoCollection<Document> collection = db.getCollection("userrights");
		
		// Find all the rights the user has
		MongoCursor<Document> cursor = collection.find(eq("username", username)).iterator();
		
		String results = String.format("Rights granted for user '%s'.", username);
		
		while(cursor.hasNext()){
			Document doc = cursor.next();
			results += String.format("dataspace = %s, rights = %s\n", doc.getString("dataspace"), doc.getString("rights"));
		}
		
		return results;
	}
	
	public boolean setUserProfile(){
		// TODO
		return false;
	}
}
