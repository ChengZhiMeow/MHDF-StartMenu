package cn.chengzhimeow.mhdfstartmenu;


import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;

public final class PluginLoader implements io.papermc.paper.plugin.loader.PluginLoader {
    @Override
    public void classloader(PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();
        resolver.addRepository(new RemoteRepository.Builder("alibaba-central", "default", "https://maven.aliyun.com/repository/public").build());
        resolver.addRepository(new RemoteRepository.Builder("catnies-repo", "default", "https://repo-eo.catnies.top/releases/").build());
        resolver.addRepository(new RemoteRepository.Builder("chengzhimeow-repo-mirror", "default", "https://repo-eo.catnies.top/mhdf/").build());

        resolver.addDependency(new Dependency(new DefaultArtifact("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.1.21"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("org.jetbrains.kotlin:kotlin-reflect:2.1.21"), null));

        resolver.addDependency(new Dependency(new DefaultArtifact("cn.chengzhimeow:CC-Scheduler:2.0.4"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("cn.chengzhimeow:CC-Yaml:2.1.22"), null));

        classpathBuilder.addLibrary(resolver);
    }
}
