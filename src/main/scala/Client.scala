import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.*
import scalafx.scene.layout.{HBox, VBox}
import scalafx.geometry.Insets
import scalafx.geometry.Pos
import scalafx.event.ActionEvent


object Client extends JFXApp3:

  var path: String = ""
  var params: String = ""
  override def start(): Unit =
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
               
               val id = new Identify(text.value)
               path = id.getPath
               id.getParameters.foreach(param => params += s"${param._1}: ${param._2}\n")
               println(path)
               println(params)
             }
           },
           new HBox {
             padding = Insets(5)
             alignment = Pos.Center
             children = new Button {
               text = "Submit"
               focusTraversable = false
             }
           }
         )
       }
     }
   }
  end start

end Client
