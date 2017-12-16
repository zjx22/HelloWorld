import java.io.File
import java.util.concurrent.TimeUnit

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.util.Timeout
import com.typesafe.config.ConfigFactory

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future
import scala.sys.Prop
import scala.concurrent.duration._

case class Task(fileName: String)

case class SubmitTask(path: String)

case class Result(r: Map[String, Int])
/**
  * 继承Akka的actor
  */
class Master extends Actor {

  //重写receive方法用于接收消息
  override def receive: Receive = {

    case SubmitTask(path) => {
      val files = new ArrayBuffer[String]
      //获取文件夹下所有文件的方法
    getlocal(path,files)

      //for循环，有几个文件就创建几个Worker



      //创建Worker过程
      for(f <- files) {
        //通过Master Actor的上下文创建Worker Actor
        val workerRef = context.actorOf(Props[Worker], "Worker")
        //向Work发送计算某个文件的任务
        workerRef .? Task(f)

        //将每个Worker返回的结果进行聚合

        //然后再讲结果放回给Driver
      }
    }
      val getlocal = (path:String,arr:ArrayBuffer[String]) => {

        val file = new File(path)
        if (file.exists) {
          val files = file.listFiles
             if (files.length == 0) {
               println("文件夹是空的!")
              }
              else {
                 for (file2 <- files) {
                     if (file2.isDirectory)
                       getlocal(file2.getAbsolutePath)
                     else arr.append(file2.getAbsolutePath)
                    }
               }

            }
        else
          println("文件不存在!")

      }


    //    case "Hello" => {
    //      //master接收了一个消息
    //      println("a msg received")
    //      println("Hello")
    //      //给消息的发送者返回一个消息（响应）
    //      Thread.sleep(3000)
    //      sender() ! "success12341234"
    //      println("返回消息成功！")
    //    }
    //
    //    case "Bye" => {
    //      println("a msg received")
    //      println("Bye")
    //    }


  }
}

//伴生对象，用于创建ActorSystem
object Master {

  def main(args: Array[String]): Unit = {

    //1.创建ActorSystem
    val actorSystem: ActorSystem = ActorSystem("ActorSystem")

    //2.创建Actor（Master）,并给Actor取一个名字
    //放回的是Actor的引用
    //启动进程并一直运行用来接收消息
    val masterRef: ActorRef = actorSystem.actorOf(Props(new Master), "Master")

    //3.拿到了Master的引用，并向Master发消息
    //一个感叹号相当于发送异步消息
    // !
    // ?
    //达到发送同步消息还是异步消息，取决于发送消息的方式

    //发送异步消息，不阻塞
   // masterRef.!("Hello")

    //传入一个隐式参数
    implicit val timeout = Timeout.apply(1L, TimeUnit.HOURS)

    //发送异步消息，但是返回一个Future

    val fucture: Future[Any] = masterRef ? SubmitTask("c://files")
  }



}
