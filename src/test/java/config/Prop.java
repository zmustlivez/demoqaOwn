package config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.ConfigFactory;

public class Prop {

    private static Class<? extends PropInterface> getPropertySource() {
        String env = System.getProperty("env");

        if (env == null || env.equals("null") || env.equals("test")) {
            return PropInterfaceTest.class;
        } else {
            throw new RuntimeException("Invalid value for system property 'env'! Expected one of:[null, 'test']");
        }
    }

    public static final PropInterface PROP = ConfigFactory.create(getPropertySource());

    @LoadPolicy(LoadType.MERGE)
    @Sources({"system:properties", "classpath:variables.values"})
    interface PropInterfaceTest extends PropInterface {
    }

    public interface PropInterface extends Config {

        // Web tests properties
        @Key("url")
        String getURL();

        @Key("login")
        String getLogin();

        @Key("password")
        String getPassword();

    }
}
