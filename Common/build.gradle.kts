architectury {
    common("fabric")
    platformSetupLoomIde()
}

val minecraftVersion = project.properties["minecraft_version"] as String

loom.accessWidenerPath.set(file("src/main/resources/corgilib.accesswidener"))

sourceSets.main.get().resources.srcDir("src/main/generated/resources")

dependencies {
    // We depend on fabric loader here to use the fabric @Environment annotations and get the mixin dependencies
    // Do NOT use other classes from fabric loader
    modImplementation("net.fabricmc:fabric-loader:${project.properties["fabric_loader_version"]}")

    modImplementation("com.electronwill.night-config:toml:${project.properties["nightconfig_version"]}")
    modImplementation("blue.endless:jankson:${project.properties["jankson_version"]}")

    modImplementation("io.github.spair:imgui-java-binding:${project.properties["imgui_version"]}")
    modImplementation("io.github.spair:imgui-java-lwjgl3:${project.properties["imgui_version"]}")

    modImplementation("io.github.spair:imgui-java-natives-windows:${project.properties["imgui_version"]}")
    modImplementation("io.github.spair:imgui-java-natives-linux:${project.properties["imgui_version"]}")
}

publishing {
    publications.create<MavenPublication>("mavenCommon") {
        artifactId = "${project.properties["archives_base_name"]}" + "-common"
        version = "$minecraftVersion-" + project.version.toString()
        from(components["java"])
    }

    repositories {
        mavenLocal()
        maven {
            val releasesRepoUrl = "https://maven.jt-dev.tech/releases"
            val snapshotsRepoUrl = "https://maven.jt-dev.tech/snapshots"
            url = uri(if (project.version.toString().endsWith("SNAPSHOT") || project.version.toString().startsWith("0")) snapshotsRepoUrl else releasesRepoUrl)
            name = "ExampleRepo"
            credentials {
                username = project.properties["repoLogin"]?.toString()
                password = project.properties["repoPassword"]?.toString()
            }
        }
    }
}