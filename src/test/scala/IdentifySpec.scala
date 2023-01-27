import org.scalatest._
import flatspec._
import matchers._

class IdentifySpec extends AnyFlatSpec with should.Matchers:

  "Identify" should "return correct path and parameters with 'login'" in {
    val identifyLogin = new Identify("visma-identity://login?source=severa")
    identifyLogin.getPath should be ("login")
    identifyLogin.getParameters should be (Map[String, String]("source" -> "severa"))
  }

  it should "return correct path and parameters with 'confirm'" in {
    val indentifyConfirm = new Identify("visma-identity://confirm?source=netvisor&paymentnumber=102226")
    indentifyConfirm.getPath should be ("confirm")
    indentifyConfirm.getParameters should be (Map[String, String]("source" -> "netvisor", "paymentnumber" -> "102226"))
  }

  it should "return correct path and parameters with 'sign'" in {
    val indentifyConfirm = new Identify("visma-identity://sign?source=vismasign&documentid=105ab44")
    indentifyConfirm.getPath should be("sign")
    indentifyConfirm.getParameters should be(Map[String, String]("source" -> "vismasign", "documentid" -> "105ab44"))
  }

  it should "throw correct error if schema is wrong" in {
    val expectedMessage = "Invalid URI scheme"
    the [IllegalArgumentException] thrownBy(new Identify("vism-identity://login?source=severa")) should have message expectedMessage
  }

  it should "throw correct error if path is wrong" in {
    val expectedMessage = "Invalid path"
    the[IllegalArgumentException] thrownBy (new Identify("visma-identity://loginn?source=severa")) should have message expectedMessage
  }

  it should "throw correct error if paymentnumber is not an integer" in {
    val expectedMessage = "Payment number must be an integer"
    the[IllegalArgumentException] thrownBy (new Identify("visma-identity://confirm?source=netvisor&paymentnumber=102.5")) should have message expectedMessage
    the[IllegalArgumentException] thrownBy (new Identify("visma-identity://confirm?source=netvisor&paymentnumber=hundredone")) should have message expectedMessage
  }

end IdentifySpec
