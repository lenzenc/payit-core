package com.payit

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._

class SprayActor extends Actor with DefaultService {
  def actorRefFactory = context
  def receive = runRoute(defaultRoute)
}

trait DefaultService extends HttpService {
  val defaultRoute =
    path("") {
      get {
        respondWithMediaType(`text/html`) {
          complete {
            <html>
              <body>
                <h2>Welcome to the future site of PayIt!</h2>
              </body>
            </html>
          }
        }
      }
    }
}