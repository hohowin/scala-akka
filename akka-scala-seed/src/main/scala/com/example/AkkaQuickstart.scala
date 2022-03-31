//#full-example
package com.example


import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.example.OrderProcessor.Order
import com.example.Shipper.Shipment
import com.example.Notifier.Notification

object Notifier {

  final case class Notification(orderId: Int, shipmentSuccess: Boolean)

  def apply(): Behavior[Notification] = Behaviors.receive {
    (context, message) =>

    context.log.info(message.toString())
    Behaviors.same
  }
}

object Shipper {

  final case class Shipment(orderId: Int, product: String, number: Int, replyTo: ActorRef[Notification])

  def apply(): Behavior[Shipment] = Behaviors.receive {
    (context, message) =>
    
    context.log.info(message.toString())
    message.replyTo ! Notification(message.orderId, true)

    Behaviors.same
  }
}

object OrderProcessor {

  final case class Order(id: Int, product: String, number: Int)

  def apply(): Behavior[Order] = Behaviors.setup { 
    (context) =>

    val shipperRef = context.spawn(Shipper(), "shipper")
    val notifierRef = context.spawn(Notifier(), "notifier")
    
    Behaviors.receiveMessage {
      message =>

      context.log.info(message.toString())
      shipperRef ! Shipment(message.id, message.product, message.number, notifierRef)

      Behaviors.same
    }
  }
}

//#main-class
object AkkaQuickstart extends App {
  //#actor-system
  val orderProcessor: ActorSystem[OrderProcessor.Order] = ActorSystem(OrderProcessor(), "orders")

  //#actor-system

  //#main-send-messages
  orderProcessor ! Order(0, "Jacket", 2)
  orderProcessor ! Order(1, "Sneakers", 1)
  orderProcessor ! Order(2, "Socks", 5)
 
  //#main-send-messages
}
//#main-class
//#full-example
