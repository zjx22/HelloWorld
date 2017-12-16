
import akka.actor.{Actor, ActorSystem, Props}
import scala.io.Source


class Worker extends Actor {
  override def receive: Receive = {

    case Task(file) => {

      //读取文件，然后进行单词计数
      val b = Source.fromFile(file).getLines().toList.flatMap ( _.split("\t") )
        .map ((_, 1) ).groupBy(_._1).mapValues(_.map((_._2)).reduce(_ + _))

      //将结果返回给发送者
      sender() ! Result(b)
    }
  }
}
