// [[file:~/projects/thrown/src/posts/scalajs-react-lenses/scalajs-react-lenses.org::*Some%20initialization%20stuff][Some\ initialization\ stuff:1]]
package thrown
import japgolly.scalajs.react._
import org.scalajs.dom.raw.Element
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import monocle.Lens
import scala.scalajs.js.Dynamic.global
import org.scalajs.dom
import japgolly.scalajs.react.React
import japgolly.scalajs.react.vdom.prefix_<^._
import thrown.bootstrap.Modal._
import thrown.bootstrap.Icon
import thrown.bootstrap.Bootstrap.{CommonStyle,Button}
import thrown.bootstrap.Bootstrap

@JSExport("LensesDemo")
object LensesDemo extends js.JSApp {

  lazy val staticForm = Form.static()
  var config = Config.default
  lazy val configForm = ConfigForm.form()

  @JSExport
  def main(): Unit = {
    global.console.log("hello world")
  }

  @JSExport
  def demo(container:Element): Unit = {
    val button =Demo.demoButton(Demo.Props("demo",configForm))

    React.render(button, container)
  }


  @JSExport
  def formPreview(container:Element):Unit = {
    val button = Demo.demoButton(Demo.Props("static form",staticForm))
    React.render(button,container)
  }

}

object Demo {

  case class State(show:Boolean)
  case class Props(name:String, demo:ReactNode)
  class Backend(t:BackendScope[Props,State]){
    def show () = {
      t.modState(s=>s.copy(show=true))
    }
    def hide () = {
      t.modState(s=>s.copy(show=false))
    }

    def modal(props:Props) = {
      val modal = new Modal with ClosableHeader{
        def title = props.name
        def closed = hide
      }
      modal.render(props.demo)
    }
  }
  val demoButton = ReactComponentB[Props]("demo")
      .initialState(State(false))
      .backend(new Backend(_))
      .render((P,S,B)=>{
        <.div(
          Button(B.show,CommonStyle.warning)(Icon.eye," Show " + P.name),
          if(S.show) B.modal(P) else EmptyTag
        )



        // <.div(
        //   if(S.show){
        //     val modal = new Modal with ClosableHeader{
        //       def title = P.name
        //       def closed = B.hide
        //     }
        //     modal.render(P.demo)
        //   }else{
        //     EmptyTag
        //   }
        // )
      })
      .build
}
// Some\ initialization\ stuff:1 ends here
// [[file:~/projects/thrown/src/posts/scalajs-react-lenses/scalajs-react-lenses.org::*The%20model][The\ model:1]]
case class Config(restricted: String, mailConfig:MailConfig)

object Config{

  lazy val default = Config("thrownforaloop.com",MailConfig("localhost",9000,"mike"))
}

case class MailConfig(
  host:String,
  port:Int,
  user:String
)


// The\ model:1 ends here
// [[file:~/projects/thrown/src/posts/scalajs-react-lenses/scalajs-react-lenses.org::*The%20editable%20form][The\ editable\ form:1]]
object Form{
  case class Field(name:String,value:String,onChange:ReactEventI=>Unit)


  val field = ReactComponentB[Field]("form-field")
    .render(P=>{
      val Field(name,value,onChange) = P
      <.div(^.`class`:="form-group",
        <.label(
          ^.`for` :=name,
          name.capitalize),
        <.input(
          ^.tpe := "text",
          ^.`class`:="form-control",
          ^.id := name,
          ^.value := value,
          ^.onChange ==> onChange
        ))
    })
    .build

  val form = ReactComponentB[Seq[Field]]("form")
    .render(P=>{
      <.div(^.`class`:="panel panel-default",
        <.div(^.`class`:="panel",
          <.div(^.`class`:="panel-heading",
            "Config Edit"
          ),
          <.div(^.`class`:="panel-body",
            <.form(
              P.map(f => field.withKey(f.name)(f))
            )
          )
        )
      )
    })
  .build


  // // creates an input field for the form.
  // def field(name:String) = {
  // }

  // a static construction of our form
  def static() = {
    val Config(restricted,MailConfig(host,port,user)) = Config.default
    val noOp = (e:ReactEventI) => {}
    val fields = Seq(
        Field("restricted" ,restricted    ,noOp),
        Field("host"       ,host          ,noOp),
        Field("user"       ,user          ,noOp),
        Field("port"       ,port.toString ,noOp))

    form(fields)
  }
}
// The\ editable\ form:1 ends here
// [[file:~/projects/thrown/src/posts/scalajs-react-lenses/scalajs-react-lenses.org::*Making%20it%20dynamic][Making\ it\ dynamic:1]]
object ConfigForm{
  case class Props(onSubmit:Config=>Unit)
  case class State(config:Config)

  class Backend(t:BackendScope[Unit,State]){
    def modifyRestricted(e:ReactEventI) = {
      t.modState(s=>s.copy(config=s.config.copy(restricted=e.currentTarget.value)))
    }
    def modifyHost(e:ReactEventI) = {
      t.modState(s=>s.copy(config=s.config.copy(mailConfig=s.config.mailConfig.copy(host=e.currentTarget.value))))
    }
    def modifyUser(e:ReactEventI) = {
      t.modState(s=>s.copy(config=s.config.copy(mailConfig=s.config.mailConfig.copy(user=e.currentTarget.value))))
    }

  }

  val form = ReactComponentB[Unit]("config-form")
  .initialState(State(Config.default))
  .backend(new Backend(_))
    .render((P,S,B) =>{
      val Config(restricted,MailConfig(host,port,user)) = S.config
      val Field = Form.Field

      val fields = Seq(
        Field("restricted" ,restricted ,B.modifyRestricted),
        Field("host"       ,host       ,B.modifyHost),
        Field("user"       ,user       ,B.modifyUser))

      Form.form(fields)
    })
  .buildU

}
// Making\ it\ dynamic:1 ends here
// [[file:~/projects/thrown/src/posts/scalajs-react-lenses/scalajs-react-lenses.org::*Maybe%20lenses%20will%20help][Maybe\ lenses\ will\ help:1]]
// val _config     = Lens[State,ConfigItem]      (_.config)     (v => s => s.copy(config=v))
// val _restricted = Lens[ConfigItem,String]     (_.restricted) (v => c => c.copy(restricted=v))
// val _mail       = Lens[ConfigItem,MailConfig] (_.mailConfig) (v => c => c.copy(mailConfig=v))
// val _host       = Lens[MailConfig,String]     (_.host)       (v => m => m.copy(host=v))
// val _port       = Lens[MailConfig,Int]        (_.port)       (v => m => m.copy(port=v))
// val _user       = Lens[MailConfig,String]     (_.user)       (v => m => m.copy(user=v))
// val _password   = Lens[MailConfig,String]     (_.password)   (v => m => m.copy(password=v))

// val _eventV     = Lens[ReactEventI,String]    (_.currentTarget.value)   (v => m => m.copy(password=v))
// val _mailconfig = _config composeLens _mail
// Maybe\ lenses\ will\ help:1 ends here
