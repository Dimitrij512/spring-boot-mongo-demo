package com.softserve.mongoDemo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.mongodb.BasicDBObject;
import com.softserve.mongoDemo.models.BadUser;
import com.softserve.mongoDemo.models.CollectionUserId;
import com.softserve.mongoDemo.models.User;
import com.softserve.mongoDemo.services.UserService;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import static com.softserve.mongoDemo.utils.UserGenerateUtil.prepareTestUsers;
import static com.softserve.mongoDemo.utils.UserGenerateUtil.printJson;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MongoDemoApplication.class)
public class MongoDemoApplicationTests {

	@Autowired UserService userService;
	@Autowired MongoOperations operations;
	@Autowired private GridFsTemplate gridFsTemplate;

	@Before
	public void setUp() {
		operations.dropCollection("user");
		operations.dropCollection("fs.chunks");
		operations.dropCollection("fs.files");
		operations.insert(prepareTestUsers(150), "user");
	}

	@Test
	public void testFindByName() {
		Aggregation agg = newAggregation(
			match(Criteria
				.where("name").is("Adela")
			)
		);
		AggregationResults<User> result = operations.aggregate(agg, "user", User.class);

		printJson(result.getMappedResults());
	}

	@Test
	public void testFindUserGt18Years (){
		Aggregation agg = newAggregation(
			match(Criteria.where("age").gt(18))

		);
		AggregationResults<User> result = operations.aggregate(agg, "user", User.class);

		printJson(result.getMappedResults());
	}

	@Test
	public void testFindUserWithEmptyCommentList () {
		Aggregation agg = newAggregation(
			match(Criteria
				.where("commentsList").size(1)
			)
		);
		AggregationResults<User> result = operations.aggregate(agg, "user", User.class);

		printJson(result.getMappedResults());
	}

	@Test
	public void testFindBadUser () {
		Aggregation agg = newAggregation(
			match(Criteria
				.where("age").lt(18)),
			unwind("commentsList"),
			match(Criteria.where("commentsList.description").regex("^bad word*")),
			group("_id","name","lastName").addToSet("commentsList").as("comments")
		);

		AggregationResults<BadUser> result = operations.aggregate(agg, "user", BadUser.class);

		printJson(result.getMappedResults());
	}

	@Test
	public void testFindBadUserLondon() {
		Aggregation agg = newAggregation(
			match(Criteria
				.where("age").lt(21)
			),
			unwind("contact"),
			match(Criteria.where("contact.address").is("London")),
			unwind("commentsList"),
			match(Criteria.where("commentsList.description").regex("^bad word*")),
			group("_id", "name", "lastName", "city")
				.addToSet("contact.address").as("city")
				.addToSet("commentsList.description").as("comments")
		);

		AggregationResults<BadUser> result = operations.aggregate(agg, "user", BadUser.class);

		printJson(result.getMappedResults());
	}

	@Test
	public void testFindBadUsersIdAndBan() {
		Aggregation agg = newAggregation(
			match(Criteria
				.where("age").lt(21)),
			unwind("contact"),
			match(Criteria.where("contact.address").is("London")),
			unwind("commentsList"),
			match(Criteria.where("commentsList.description").regex("^bad word*")),
			group().addToSet("_id").as("listIds")
			);

		AggregationResults<CollectionUserId> result = operations.aggregate(agg, "user", CollectionUserId.class);
		CollectionUserId ids = result.getUniqueMappedResult();

		Query query = new Query();
		query.addCriteria(Criteria.where("id").in(ids.getListIds()));

		Update update = new Update();
		update.set("isActive", false);

		operations.updateMulti(query, update, User.class);

		Aggregation checkAgg = newAggregation(match(Criteria
			.where("isActive").is(false)
		));

		AggregationResults<User> resultAfterUpdate = operations.aggregate(checkAgg, "user", User.class);

		printJson(resultAfterUpdate.getMappedResults());
	}

	@Test
	public void testShema_less(){
		//operations.dropCollection("user");
		operations.insert(prepareTestUsers(2), "user");
	}

	@Test
	public void testSaveFile()throws IOException {
		String newFileName = "mongoDemo-image";
		final File initialFile = new File("src/test/resources/typeDbs.png");
		InputStream targetStream = new FileInputStream(initialFile);
		gridFsTemplate.store(targetStream, newFileName, new BasicDBObject("user_id", "user_id_test"));
	}

	@Test
	public void testDeleteFile(){
		gridFsTemplate.delete(new Query(Criteria.where("metadata.user_id").is("user_id_test")));
	}

}

