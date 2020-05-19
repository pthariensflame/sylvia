package com.pthariensflame.sylvia.shell;

import org.graalvm.launcher.AbstractLanguageLauncher;
import org.graalvm.launcher.Launcher;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

/**
 * Implementation quirk, due to module system issues; will be removed as soon as possible
 */
@ApiStatus.Internal
public abstract class SylviaLauncherSuper extends AbstractLanguageLauncher {
    @NotNull
    protected static OutputStream newLogStreamImpl(@NotNull Path path) throws IOException {
        return Launcher.newLogStream(path);
    }

    SylviaLauncherSuper() {
        super();
    }
}
