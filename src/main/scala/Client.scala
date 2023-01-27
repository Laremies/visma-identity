import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.layout.{HBox, VBox}
import scalafx.geometry.Insets
import scalafx.geometry.Pos
import scalafx.event.ActionEvent


object Client extends JFXApp3 {
  private def submitAction(text: String): Unit = {
    try {
      val id = new Identify(text)
      var params: String = ""
      id.getParameters.foreach(param => params += s"\n${param._1} = ${param._2}")
      new Alert(AlertType.Information) {
        title = "Success"
        headerText = "URI succesfully identified"
        contentText = s"Path:\n${id.getPath}\n\nParameters: ${params}"
      }.showAndWait()
    } catch {
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

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "Visma Identity"
      width = 600
      height = 400
      scene = new Scene {
        root = new VBox {
          padding = Insets(75)
          children = Seq(
            new TextField {
              promptText = "Input URI"
              focusTraversable = false
              onAction = ActionEvent => {
                submitAction(text.value)
              }
            },
            new HBox {
              padding = Insets(5)
              alignment = Pos.Center
              children = new Button {
                text = "Submit"
                focusTraversable = false
                onAction = ActionEvent => {
                  submitAction("placeholder")
                }
              }
            }
          )
        }
      }
    }
  }
}

end Client
