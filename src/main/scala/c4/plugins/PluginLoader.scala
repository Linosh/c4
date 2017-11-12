package c4.plugins

import java.io.{File, FileInputStream}
import java.net.URLClassLoader
import java.util.jar.JarInputStream

class PluginLoader {
  def loadPlugin(path: String): Runnable = {
    val cl = new URLClassLoader(Array(new File(path).toURI.toURL), this.getClass.getClassLoader)
    val jis = new JarInputStream(new FileInputStream(path))
    val clazz = cl.loadClass(jis.getManifest.getMainAttributes.getValue("PluginClass"))
    jis.close()
    clazz.newInstance().asInstanceOf[Runnable]

  }
}

object PluginLoader {
  def apply(): PluginLoader = new PluginLoader()
}