package demo.quick.start.pojo;

import com.mongodb.Block;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.not;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static java.util.Arrays.asList;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * Created by lining on 2019/10/10.
 * http://mongodb.github.io/mongo-java-driver/3.11/driver/tutorials/connect-to-mongodb/
 */
public class PojoQuickTour {

    public static void main(String[] args) {

        /* Creating a Custom CodecRegistry */
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        // Using the CodecRegistry
        // You can set it when instantiating a MongoClient object:
        MongoClientSettings settings = MongoClientSettings.builder()
                .codecRegistry(pojoCodecRegistry)
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        // You can use an alternative CodecRegistry with a MongoDatabase:
        MongoDatabase database = mongoClient.getDatabase("mydb").withCodecRegistry(pojoCodecRegistry);
        // get a handle to the "people" collection
        MongoCollection<Person> collection = database.getCollection("people", Person.class);

        /* Inserting a POJO into MongoDB */
        // Insert a Person
        Person ada = new Person("Ada Byron", 20, new Address("St James Square", "London", "W1"));
        collection.insertOne(ada);

        // Insert Many Persons
        List<Person> people = asList(
                new Person("Charles Babbage", 45, new Address("5 Devonshire Street", "London", "W11")),
                new Person("Alan Turing", 28, new Address("Bletchley Hall", "Bletchley Park", "MK12")),
                new Person("Timothy Berners-Lee", 61, new Address("Colehill", "Wimborne", null))
        );
        collection.insertMany(people);

        /* Query the Collection */
        Block<Person> printBlock = new Block<Person>() {
            public void apply(final Person person) {
                System.out.println(person);
            }
        };
        collection.find().forEach(printBlock);

        /* Specify a Query Filter */
        // Get A Single Person That Matches a Filter
        Person somebody = collection.find(eq("address.city", "Wimborne")).first();
        System.out.println(somebody);

        // Get All Person Instances That Match a Filter
        collection.find(gt("age", 30)).forEach(printBlock);

        // Update Documents
        // Update a Single Person
        collection.updateOne(eq("name", "Ada Byron"), combine(set("age", 23), set("name", "Ada Lovelace")));
        // Update Multiple Persons
        UpdateResult updateResult = collection.updateMany(not(eq("zip", null)), set("zip", null));
        System.out.println(updateResult.getModifiedCount());
        // Replace a Single Person
        collection.replaceOne(eq("name", "Ada Lovelace"), ada);

        /* Delete Documents */
        // Delete a Single Person That Matches a Filter
        collection.deleteOne(eq("address.city", "Wimborne"));
        // Delete All Persons That Match a Filter
        DeleteResult deleteResult = collection.deleteMany(eq("address.city", "London"));
        System.out.println(deleteResult.getDeletedCount());
    }
}
