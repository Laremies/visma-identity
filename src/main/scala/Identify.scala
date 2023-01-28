import java.net.URI
import scala.collection.mutable.Map

/** This class identifies and verifies a URI request and offers methods
  * for getting the parameters and path of the request.
  *
  * @param uri the URI that needs to be identified
  */
class Identify(val uri: String):
  private val scheme: String = "visma-identity"
  private val parameters: Map[String, String] = Map()
  private var path: String = ""

  /** Parses through the URI. Saves the path and parameters to their respective
    * private variables defined above, if the request is valid. Otherwise throws
    * and exception. Utilizes Scala's powerful feature of having access to
    * Java's libraries.
    *
    * @throws IllegalArgumentException if the request is not valid
    * @throws Exception if there's a problem with parsing the query
    */
  private def parse(): Unit =
    // Create a new URI object from the input URI and check if the schemes match.
    val uriToParse = new URI(this.uri)
    if uriToParse.getScheme != this.scheme then
      throw new IllegalArgumentException("Invalid URI scheme")

    /** Split the query string into an array of key-value pairs, which represent the
      * request's parameters. Then convert the array to a Map to make accessing the pairs
      * easier. Add the pairs to the variable 'parameters' if the query is valid.
      */
    try
      this.parameters ++= uriToParse.getQuery
                                    .split("&")
                                    .map(_.split("="))
                                    .map(arr => arr(0) -> arr(1)).toMap
    catch
      case e: Exception => throw new Exception("Invalid query or parameters")

    // Helper for checking that the request includes a source
    val checkSource: Boolean = this.parameters.contains("source")
    // Get request's path and verify it using pattern matching. Throw an error if the path is invalid.
    this.path = uriToParse.getAuthority
    this.path match
      case "login" =>
        if !checkSource then
          throw new IllegalArgumentException("Missing parameter 'source' for path 'login'")

      case "confirm" =>
        if !checkSource || !this.parameters.contains("paymentnumber") then
          throw new IllegalArgumentException("Missing source or parameter 'paymentnumber' for path 'confirm'")
        try
          this.parameters("paymentnumber").toInt
        catch
          case _: NumberFormatException => throw new IllegalArgumentException("Payment number must be an integer")

      case "sign" =>
        if !checkSource || !this.parameters.contains("documentid") then
          throw new IllegalArgumentException("Missing source or parameter 'documentid' for path 'sign'")

      case _ => throw new IllegalArgumentException("Invalid path")
  end parse

  parse()

  /** Getter methods so that the client has access to the request's path and parameters.
    *
    * @return the uri's path and parameters
    */
  def getPath: String = this.path
  def getParameters: Map[String, String] = this.parameters

end Identify
