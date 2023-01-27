import java.net.URI
import scala.collection.mutable.Map

class Identify(val uri: String):
  private val scheme: String = "visma-identity"
  private val parameters: Map[String, String] = Map()
  private var path: String = ""

  private def parse(): Unit =
    val uriToParse = new URI(this.uri)
    if uriToParse.getScheme != this.scheme then
      throw new IllegalArgumentException("Invalid URI scheme")

    try
      this.parameters ++= uriToParse.getQuery
                                    .split("&")
                                    .map(_.split("="))
                                    .map(arr => arr(0) -> arr(1)).toMap
    catch
      case e: ArrayIndexOutOfBoundsException => throw new ArrayIndexOutOfBoundsException("Invalid parameters")

    val checkSource: Boolean = this.parameters.contains("source")
    this.path = uriToParse.getAuthority
    this.path match
      case "login" =>
        if !checkSource then
          throw new IllegalArgumentException("Missing source parameter for login path")
      case "confirm" =>
        if !checkSource || !this.parameters.contains("paymentnumber") then
          throw new IllegalArgumentException("Missing source or 'paymentnumber' parameter for confirm path")
        try
          this.parameters("paymentnumber").toInt
        catch
          case _: NumberFormatException => throw new IllegalArgumentException("Payment number must be an integer")
      case "sign" =>
        if !checkSource || !this.parameters.contains("documentid") then
          throw new IllegalArgumentException("Missing source or 'documentid' parameter for sign path")
      case _ => throw new IllegalArgumentException("Invalid path")
  end parse

  parse()

  def getPath: String = this.path

  def getParameters: Map[String, String] = this.parameters

end Identify
