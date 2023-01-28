import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.layout.{HBox, VBox}
import scalafx.geometry.{Insets, Pos}
import scalafx.event.ActionEvent

/** Simple client that has an input field for a URI request. If the request succeeds it displays
  * information releveant to the request, otherwise it describes the error that happened.
  */
object Client extends JFXApp3 {
  /** Defines the behaviour when the user clicks the "Identify" button or presses "Enter".
   *
   * @param text the URI the user writes in to the input field
   */
  private def submitAction(text: String): Unit = {
    try {
      // create new Identify object form the user input URI
      val uri = new Identify(text)
      var params: String = ""
      // build a string with the parameters of the URI
      uri.getParameters.foreach(param => params += s"\n${param._1} = ${param._2}")
      // display an alert with the path and parameters of the URI
      new Alert(AlertType.Information) {
        initOwner(stage)
        title = "Success"
        headerText = "Request succesfully identified"
        contentText = s"Path:\n${uri.getPath}\n\nParameters: ${params}"
      }.showAndWait()
    } catch {
      // catch any exception that might be thrown by the Identify class and display an error alert
      case e: Exception => {
        new Alert(AlertType.Error) {
          initOwner(stage)
          title = "Error"
          headerText = "An error occurred"
          contentText = e.getMessage
        }.showAndWait()
      }
    }
  }

  // Starts the client.
  override def start(): Unit = {
    // create a text field for the user to input the URI
    val textField = new TextField {
      promptText = "Input URI"
      focusTraversable = false
      onAction = ActionEvent => {
        submitAction(text.value)
      }
    }
    // create a container and a button for the user to submit the URI
    val submitButton = new HBox {
      padding = Insets(5)
      alignment = Pos.Center
      children = new Button {
        text = "Identify"
        focusTraversable = false
        onAction = ActionEvent => {
          submitAction(textField.text.value)
        }
      }
    }
    // create the main window for the client
    stage = new JFXApp3.PrimaryStage {
      title = "Visma Identity"
      width = 600
      height = 350
      scene = new Scene {
        root = new VBox {
          padding = Insets(75)
          alignment = Pos.Center
          children = Seq(textField, submitButton)
        }
      }
    }
  }
}
