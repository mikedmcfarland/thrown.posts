package thrown.bootstrap

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import scalacss.ScalaCssReact._

object Modal extends DefaultBoostrapStyles{
  trait Modal{
    type Render = (Bootstrap.Modal.Backend) => ReactNode

    def header:Render
    def footer:Render
    def closed: () => Unit
    def backdrop:Boolean = true
    def keyboard:Boolean = true

    def render(children:ReactNode*):ReactNode = Bootstrap.Modal(
      Bootstrap.Modal.Props(header,footer,closed,backdrop,keyboard),
      children)

  }

  trait ClosableHeader{
    self:Modal =>
    def title:String
    def header:Render = backend =>
    <.span(<.button(^.tpe := "button", style.close, ^.onClick --> backend.hide(), Icon.close), <.h4(title))

    override def footer:Render = be => <.span(Bootstrap.Button(be.hide)("Ok"))
  }
}
