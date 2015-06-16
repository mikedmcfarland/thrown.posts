package thrown.bootstrap

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom.raw.Element

import org.scalajs.dom.raw.Event

import scala.language.implicitConversions
import scala.scalajs.js
import scalacss.ScalaCssReact._
import scalacss.Defaults._
import scala.scalajs.js.Dynamic.global

/**
 * Common Bootstrap components for scalajs-react
 */
object Bootstrap extends DefaultBoostrapStyles{

  // shorthand for styles
  def bss = bootstrapStyles


  lazy val jQuery = global.$.asInstanceOf[JQueryStatic]
  implicit def jq2bootstrap(jq: JQuery): BootstrapJQuery = jq.asInstanceOf[BootstrapJQuery]

  // implicit def jq2bootstrap(jq: JQuery): BootstrapJQuery = jq.asInstanceOf[BootstrapJQuery]

  // Common Bootstrap contextual styles
  object CommonStyle extends Enumeration {
    val default, primary, success, info, warning, danger = Value
  }

  object Button {

    case class Props(onClick: () => Unit, style: CommonStyle.Value , addStyles: Seq[StyleA] )

    val component = ReactComponentB[Props]("Button")
      .render { (P, C) =>
      <.button(bss.buttonOpt(P.style), P.addStyles, ^.tpe := "button", ^.onClick --> P.onClick())(C)
    }.build

    def apply(onClick:() => Unit, style: CommonStyle.Value = CommonStyle.default, addStyles: Seq[StyleA] = Seq())(children:ReactNode*) =
      component(Props(onClick,style,addStyles),children)
    def apply() = component
  }

  object ButtonGroup{
    val component = ReactComponentB[Seq[StyleA]]("ButtonGroup")
      .render { (P, C) =>
      <.div(bss.buttonGroup, P )(C)
    }.build

    def apply(props: Seq[StyleA])(children: ReactNode*) = component(props, children)
  }

  object Panel {

    case class Props(heading: Option[String], style: CommonStyle.Value)

    val component = ReactComponentB[Props]("Panel")
      .render { (P, C) =>
      <.div(bss.panelOpt(P.style))(
        P.heading.map( heading =>
          <.div(bss.panelHeading)(P.heading)
        ),
        // <.div(bss.panelHeading)(P.heading),
        <.div(bss.panelBody)(C)
      )
    }.build

    def apply(heading:String,style:CommonStyle.Value = CommonStyle.default)(children:ReactNode*) = component(Props(Some(heading),style),children)
    def apply(children:ReactNode*) = component(Props(None,CommonStyle.default),children)
    // def apply(props: Props, children: ReactNode*) = component(props, children)
    // def apply(children: ReactNode*) = component(Props(), children)
    // def apply() = component
  }

  object Modal {

    // header and footer are functions, so that they can get access to the the hide() function for their buttons
    case class Props(header: (Backend) => ReactNode, footer: (Backend) => ReactNode, closed: () => Unit, backdrop: Boolean = true,
                     keyboard: Boolean = true)

    class Backend(t: BackendScope[Props, Unit]) {
      def hide(): Unit = {
        // instruct Bootstrap to hide the modal
        jQuery(t.getDOMNode()).modal("hide")
      }

      // jQuery event handler to be fired when the modal has been hidden
      def hidden(e: JQueryEventObject): js.Any = {
        // inform the owner of the component that the modal was closed/hidden
        t.props.closed()
      }
    }

    val component = ReactComponentB[Props]("Modal")
      .stateless
      .backend(new Backend(_))
      .render((P, C, _, B) => {
      val modalStyle = bss.modal
      <.div(modalStyle.modal, modalStyle.fade, ^.role := "dialog", ^.aria.hidden := true,
        <.div(modalStyle.dialog,
          <.div(modalStyle.content,
            <.div(modalStyle.header, P.header(B)),
            <.div(modalStyle.body, C),
            <.div(modalStyle.footer, P.footer(B))
          )
        )
      )
    })
      .componentDidMount(scope => {
      val P = scope.props
      // instruct Bootstrap to show the modal
      jQuery(scope.getDOMNode()).modal(js.Dynamic.literal("backdrop" -> P.backdrop, "keyboard" -> P.keyboard, "show" -> true))
      // register event listener to be notified when the modal is closed
      jQuery(scope.getDOMNode()).on("hidden.bs.modal", null, null, scope.backend.hidden _)
    })
      .build

    def apply(props: Props, children: ReactNode*) = component(props, children)
    def apply() = component
  }
}

trait JQueryEventObject extends Event {
  var data: js.Any = js.native
}

trait JQueryStatic extends js.Object {
  def apply(element: Element): JQuery = js.native
}

trait JQuery extends js.Object {
  def on(events: String, selector: js.Any, data: js.Any, handler: js.Function1[JQueryEventObject, js.Any]): JQuery = js.native
  def off(events: String): JQuery = js.native
}

trait BootstrapJQuery extends JQuery with JQueryStatic {
  def modal(action: String): BootstrapJQuery = js.native
  def modal(options: js.Any): BootstrapJQuery = js.native
}
