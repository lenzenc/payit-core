package com.payit.components.mongo.daos

import com.mongodb.DBObject
import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.commons.MongoDBObject
import com.payit.components.core.Configuration
import com.payit.components.core.models.{ModelValidationException, Timestamps}
import com.payit.components.mongo.models.{MongoModel, MongoId}
import com.payit.components.validation._
import org.joda.time.DateTime
import org.specs2.execute.{Result, AsResult}
import org.specs2.matcher.{ThrownExpectations, Scope}
import org.specs2.mutable.Specification
import org.specs2.specification.AroundEach
import com.mongodb.casbah.Imports._

class MongoDAOSpec extends Specification with AroundEach {

  sequential

  lazy val config = Configuration.load
  lazy val conn = MongoClient(
    config.getString("mongo.specs.host").getOrElse("localhost"),
    config.getInt("mongo.specs.port").getOrElse(27017))
  lazy val db = conn(config.getString("mongo.specs.dbname").getOrElse("payit-specs"))
  lazy val coll = db("spec_crud_dao")

  def around[T : AsResult](t: => T): Result = {
    try { AsResult(t) } finally { db.getCollectionNames().foreach { c =>
      if (!c.startsWith("system")) db(c).remove(MongoDBObject.empty)
    }
    }
  }

  case class SpecModelId(value: ObjectId) extends MongoId
  case class SpecModel(
    name: String,
    id: Option[SpecModelId] = None,
    timestamps: Timestamps = Timestamps()) extends MongoModel[SpecModelId, SpecModel]
  {

    validations(
      prop("name", { m => m.name }).is(Required())
    )

    def withId(idValue: ObjectId) = copy(id = Some(SpecModelId(idValue)))

    def withUpdatedAt(now: DateTime = DateTime.now) = copy(timestamps = timestamps.withUpdatedAt(now))

  }

  val specModelMapper = new DocumentMapper[SpecModelId, SpecModel] {

    def asDBObject(model: SpecModel): DBObject = MongoDBObject (
      id(model),
      timestamps(model),
      "name" -> model.name
    )

    def fromDBObject(dbo: DBObject): SpecModel = SpecModel(
      name = dbo.as[String]("name"),
      id = Some(SpecModelId(id(dbo))),
      timestamps = timestamps(dbo)
    )

  }

  class SpecModelDAO extends MongoDAO[SpecModelId, SpecModel] {

    protected val collection = coll
    protected val mapper = specModelMapper

  }

  trait ExistingModelScope extends Scope with ThrownExpectations {

    val dao = new SpecModelDAO()
    var existingModel = SpecModel("Name", Some(SpecModelId(new ObjectId)))
    coll.insert(specModelMapper.asDBObject(existingModel))

  }

  ".insert" >> {
    "when Option model Id is None" >> {
      val model = SpecModel("No Id")
      "it should set a new model Id on the returned model" >> {
        new SpecModelDAO().insert(model).id must beSome[SpecModelId]
      }
      "it should return a new instance of the given model type" >> {
        new SpecModelDAO().insert(model) must not beTheSameAs(model)
      }
      "it should persist the given model" >> {
        new SpecModelDAO().insert(model).id must beSome.which { case id =>
          coll.findOneByID(id.value) must beSome
        }
      }
    }
    "when Option model Id is Some value" >> {
      val id = new ObjectId
      val model = SpecModel("Some Id", Some(SpecModelId(id)))
      "it should use the given Id value rather than create a new one" >> {
        new SpecModelDAO().insert(model).id must beSome[SpecModelId].like { case m => m.value must_== id }
      }
      "it should not return a new instance of the given model type" >> {
        new SpecModelDAO().insert(model) must beTheSameAs(model)
      }
      "it should persist the given model" >> {
        new SpecModelDAO().insert(model).id must beSome.which { case id =>
          coll.findOneByID(id.value) must beSome
        }
      }
    }
    "it should throw an exception if the given model does not pass validations" >> {
      new SpecModelDAO().insert(SpecModel("")) must throwA[ModelValidationException]
    }
  }

  ".update" >> {
    "when model has no Id" >> {
      "it should throw an exception" in new ExistingModelScope {
//        dao.update(existingModel.copy(id = None)) must throwA[RuntimeException](message = "because id is None")
        pending
      }
    }
    "when model has an Id" >> {
      "it should throw an exception if the model does not pass validations" in new ExistingModelScope {
        //        dao.update(existingModel.copy(name = "")) must throwA[ModelValidationException]
        pending
      }
      "it should return a new instance of the model that was passed to the update method" >> pending
      "it should update the updatedAt value on the given domain" >> pending
      "it should persist the updates to the given model" >> pending
    }
  }

  ".update" >> {
    "when model has no Id" >> {
      "it should throw an exception" in new ExistingModelScope {
//        dao.update(existingModel.copy(id = None)) must throwA[RuntimeException](message = "because id is None")
        pending
      }
    }
    "when model has an Id" >> {
      "it should throw an exception if the model does not pass validations" in new ExistingModelScope {
//        dao.update(existingModel.copy(name = "")) must throwA[ModelValidationException]
        pending
      }
      "it should return a new instance of the model that was passed to the update method" >> pending
      "it should update the updatedAt value on the given domain" >> pending
      "it should persist the updates to the given model" >> pending
    }
  }

}
