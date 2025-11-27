package space.vectrix.ignite.game;

import org.jetbrains.annotations.NotNull;
import space.vectrix.ignite.Blackboard;
import space.vectrix.ignite.IgniteBootstrap;
import space.vectrix.ignite.agent.IgniteAgent;
import space.vectrix.ignite.util.BlackboardMap;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class LumiGameLocator implements GameLocatorService {
    private static final BlackboardMap.@NotNull Key<Path> LUMI_JAR = Blackboard.key("ignite.lumi.jar",Path.class, Paths.get("./lumi.jar"));
    public static final BlackboardMap.@NotNull Key<String> GAME_TARGET = Blackboard.key("ignite.target", String.class, "cn.nukkit.Nukkit");

    private LumiGameProvider provider;

    @Override
    public @NotNull String id() {
        return "lumi";
    }

    @Override
    public @NotNull String name() {
        return "Lumi";
    }

    @Override
    public int priority() {
        return 1;
    }

    @Override
    public boolean shouldApply() {
        return true;
    }

    @Override
    public void apply(@NotNull IgniteBootstrap bootstrap) throws Throwable {
        Blackboard.compute(LumiGameLocator.LUMI_JAR, () -> Paths.get(System.getProperty(LumiGameLocator.LUMI_JAR.name())));

        this.provider = new LumiGameProvider();

        try {
            IgniteAgent.addJar(Blackboard.raw(LumiGameLocator.LUMI_JAR));
        } catch(final IOException exception) {
            throw new IllegalStateException("Unable to add lumi jar to classpath!", exception);
        }
    }

    @Override
    public @NotNull GameProvider locate() {
        return provider;
    }

    static final class LumiGameProvider implements GameProvider {
        /* package */ LumiGameProvider() {
        }

        @Override
        public @NotNull Stream<Path> gameLibraries() {
            return Stream.empty();
        }

        @Override
        public @NotNull Path gamePath() {
            return Blackboard.get(Blackboard.GAME_JAR).orElseGet(() -> Paths.get("./lumi.jar"));
        }
    }
}
