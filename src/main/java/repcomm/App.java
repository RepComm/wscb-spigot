package repcomm;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Hello world!
 *
 */
public class App extends JavaPlugin {
  public Class<?> getWSServerClass () {
    return WSServer.class;
  }
  @Override
  public void onEnable() {
    getLogger().info("Hello world!");
    
    System.out.println(WSServer.class);
  }

  

  @Override
  public void onDisable() {}
}
