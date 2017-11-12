package c4.plugins

import org.scalatest.FlatSpec

class PluginLoaderTest extends FlatSpec {
  behavior of "Plugin Loader"

  it should "load plugins" in {
    val loader = PluginLoader()
    loader.loadPlugin(getClass.getClassLoader.getResource("gitScm.jar").getPath).run()
  }
}
