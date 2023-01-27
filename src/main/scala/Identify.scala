import java.net.URI
import scala.collection.mutable.Map

class Identify(val uri: String):
  private val scheme: String = "visma-identity"
  private val allowedPaths: Vector[String] = Vector("login", "confirm", "sign")
  private val parameters: Map[String, String] = Map()
  private var path = ""

  private def parse(): Unit =
    val uriToParse = new URI(this.uri)
    if uriToParse.getScheme != this.scheme then
      throw new IllegalArgumentException("Invalid URI sheme")

    this.path = uriToParse.getAuthority
    if !allowedPaths.contains(this.path) then
      throw new IllegalArgumentException("Invalid path")

    this.parameters ++= uriToParse.getQuery
                                  .split("&")
                                  .map(_.split("="))
                                  .map(arr => arr(0) -> arr(1)).toMap

    path match
      case "login" =>
        if !this.parameters.contains("source") then
          throw new IllegalArgumentException("Missing source parameter for login path")
      case "confirm" =>
        if !this.parameters.contains("source") || !this.parameters.contains("paymentnumber") then
          throw new IllegalArgumentException("Missing source or 'paymentnumber' parameter for confirm path")
        try
          this.parameters("paymentnumber").toInt
        catch
          case _: NumberFormatException => throw new IllegalArgumentException("Payment must be an integer")
      case "sign" =>
        if !this.parameters.contains("source") || !this.parameters.contains("documentid") then
          throw new IllegalArgumentException("Missing source or 'documentid' parameter for sign path")
  end parse

  parse()

  def getPath: String = path

  def getParameters: Map[String, String] = parameters


end Identify

