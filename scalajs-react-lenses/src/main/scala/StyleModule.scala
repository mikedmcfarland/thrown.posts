package thrown.bootstrap
import scalacss.Defaults._

trait StyleModule {
 def style:BootstrapStyles
}

trait DefaultBoostrapStyles extends StyleModule {
  val bootstrapStyles = new BootstrapStyles
  def style = bootstrapStyles
}
