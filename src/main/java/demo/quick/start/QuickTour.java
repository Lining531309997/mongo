package demo.quick.start;

import com.mongodb.Block;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.sun.org.apache.xpath.internal.SourceTree;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Filters.lte;
import static com.mongodb.client.model.Updates.inc;

/**
 * Created by lining on 2019/10/9.
 */
public class QuickTour {
    public static void main(String[] args) {
        /* Connect to a Single MongoDB instance */
        // instantiate a MongoClient object without any parameters to connect to a MongoDB instance running on localhost on port 27017
        MongoClient mongoClient = MongoClients.create();

        // specify the hostname to connect to a MongoDB instance running on the specified host on port 27017
//        MongoClient mongoClient1 = MongoClients.create(MongoClientSettings.builder()
//                .applyToClusterSettings(builder ->
//                builder.hosts(Arrays.asList(new ServerAddress("localhost"))))
//                .build());

        // specify the ConnectionString
//        MongoClient mongoClient2 = MongoClients.create("mongodb://hostOne:27017,hostTwo:27018");

        /* Access a Database */
        MongoDatabase database = mongoClient.getDatabase("demo");

        /* Access a Collection */
        MongoCollection<Document> collection = database.getCollection("test");

        /* drop all the data in collection */
        collection.drop();

        /* Create a Document */
        Document document = new Document("name", "mongoDB")
                .append("type", "database")
                .append("count", 1)
                .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
                .append("info", new Document("x", 203).append("y", 102));

        /* Insert a Document */
        collection.insertOne(document);

        /* get it */
        Document myDoc = collection.find().first();
        System.out.println("myDoc : " + myDoc.toJson());

        /* Insert Multiple Documents */
        List<Document> documents = new ArrayList<Document>();
        for (int i = 0; i < 10; i++) {
            documents.add(new Document("i", i));
        }
        collection.insertMany(documents);

        /* Count Documents in A Collection */
        System.out.println("count ：" + collection.countDocuments());

        /* Query the Collection */
        // Find the First Document in a Collection
        Document firstDoc = collection.find().first();
        System.out.println("firstDoc ：" + firstDoc.toJson());
        // Find All Documents in a Collection
        // 不建议使用foreach遍历
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            System.out.println("-----------------------");
            while(cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
            System.out.println("-----------------------");
        } finally {
            cursor.close();
        }

        /* Specify a Query Filter */
        // Get A Single Document That Matches a Filter
        myDoc = collection.find(eq("i", 7)).first();
        System.out.println(myDoc.toJson());
        // Get All Documents That Match a Filter
        Block<Document> printBlock = new Block<Document>() {
            public void apply(Document document) {
                System.out.println(document.toJson());
            }
        };
        collection.find(gt("i", 5)).forEach(printBlock);
        collection.find(and(gt("i", 5), lte("i", 10))).forEach(printBlock);

        /* Update Documents */
        System.out.println("[Update a Single Document]");
        collection.updateOne(eq("i", 10), new Document("$set", new Document("i", 110)));

        System.out.println("[Update Multiple Documents]");
        UpdateResult updateResult = collection.updateMany(lt("i", 5), inc("i", 10));

        /* Delete Documents */
        System.out.println("[Delete a Single Document That Match a Filter]");
        collection.deleteOne(eq("i", 110));

        System.out.println("[Delete All Documents That Match a Filter]");
        DeleteResult deleteResult = collection.deleteMany(gte("i", 10));
        System.out.println(deleteResult.getDeletedCount());


    }
}
