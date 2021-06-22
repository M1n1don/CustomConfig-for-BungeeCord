import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.util.logging.Level;

@SuppressWarnings("all")
public class CustomConfig
{
    private Configuration config = null;
    private final File configFile;
    private final String file;
    private final Plugin plugin;

    public CustomConfig(Plugin plugin)
    {
        this(plugin, "config.yml");
    }

    public CustomConfig(Plugin plugin, String fileName)
    {
        this.plugin = plugin;
        this.file = fileName;
        configFile = new File(plugin.getDataFolder(), file);
    }

    public void saveDefault()
    {
        if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdir();
        if (!configFile.exists()) copyResource(plugin.getResourceAsStream(file), configFile);
    }

    public Configuration getConfiguration()
    {
        if (config == null) reload();
        return config;
    }

    public void save()
    {
        if (config == null) return;
        try { ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configFile); }
        catch (IOException ex) { plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex); }
    }

    public void reload()
    {
        try { config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile); }
        catch (IOException ex) { plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex); }
    }

    public void copyResource(InputStream in, File file)
    {
        try
        {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len=in.read(buf))>0) out.write(buf,0,len);
            out.close();
            in.close();
        }
        catch (Exception e) { e.printStackTrace(); }
    }
}