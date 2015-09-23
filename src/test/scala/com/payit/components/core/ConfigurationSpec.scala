package com.payit.components.core

import com.typesafe.config.ConfigFactory
import org.specs2.mutable.Specification

class ConfigurationSpec extends Specification {

  lazy val config: Configuration = Configuration.load
  val RootPath = "configspec"
  val ConfigPath = "config"
  val ConfigKeys = Seq("string", "long", "boolean", "int", "milliseconds")

  validateConfig("getString", config.getString, cfg("string"), "value")
  validateConfig("getBoolean", config.getBoolean, cfg("boolean"), true)
  validateConfig("getInt", config.getInt, cfg("int"), 10)
  validateConfig("getLong", config.getLong, cfg("long"), 1000000000)
  validateConfig("getMilliseconds", config.getMilliseconds, cfg("milliseconds"), 1000000000)
  validateConfig("getConfig", config.getConfig, "sub", Configuration(ConfigFactory.load().getConfig(s"$RootPath.sub")))

  ".subKeys" >> {
    "when config has no sub key paths" >> {
      "it should return an Empty set" >> {
        config.getConfig("emptyconfig").get.subKeys must beEmpty
      }
    }
    "when config has sub key paths" >> {
      "it should return an expected set of sub key names" >> {
        config.getConfig(s"$RootPath.$ConfigPath").get.subKeys must containTheSameElementsAs(ConfigKeys)
      }
      "and there are multiple configs with the same sub key name" >> {
        "it should return a set of sub keys filtering out duplicate sub key names" >> {
          config.getConfig(s"$RootPath.sub").get.subKeys must contain(exactly("string", "v1", "v2"))
        }
      }
    }
  }

  private def cfg(path: String): String = s"$ConfigPath.$path"

  private def validateConfig[T](methodName: String, config: (String) => Option[T], configPath: String, expectedValue: T) = {
    s".$methodName" >> {
      "when config path is valid" >> {
        s"it should return expected value: '$expectedValue'" >> {
          config(s"$RootPath.$configPath") must beSome(expectedValue)
        }
      }
      "when config path is invalid" >> {
        "it should return None" >> {
          config("doesnotexist") must beNone
        }
      }
    }
  }

}