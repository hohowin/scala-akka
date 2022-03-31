//#full-example
package com.example

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit

import org.scalatest.wordspec.AnyWordSpecLike
import com.example.Notifier.Notification
import com.example.Shipper.Shipment

//#definition
class AkkaQuickstartSpec extends ScalaTestWithActorTestKit with AnyWordSpecLike {
//#definition

  "A Shipper" must {

    "notify to notifier" in {
      val replyProbe = createTestProbe[Notification]()
      val underTest = spawn(Shipper())
      underTest ! Shipment(0, "Jacket", 3, replyProbe.ref)
      replyProbe.expectMessage(Notification(0, true))
    }
  }

}
//#full-example
